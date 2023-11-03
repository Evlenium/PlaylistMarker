package com.practicum.playlistmarker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.App.Companion.PRACTICUM_PLAYLISTMARKER_PREFERENCES_TRACKLIST
import com.practicum.playlistmarker.App.Companion.TRACKS_LIST_KEY
import com.practicum.playlistmarker.TrackAdapter.TrackClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    enum class SearchError {
        INTERNET_CONNECTION_ERROR,
        EMPTY_SEARCH_ERROR
    }

    private val trackList = ArrayList<TrackSearchItem.Track>()
    private val searchHistory = SearchHistory()
    private var inputTextFromSearch: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val rvTrack: RecyclerView = findViewById(R.id.rvTracks)
        val tvYouSearched = findViewById<TextView>(R.id.tvYouSearched)
        val sharedPreferences =
            getSharedPreferences(PRACTICUM_PLAYLISTMARKER_PREFERENCES_TRACKLIST, MODE_PRIVATE)

        val trackClickListener = TrackClickListener {
            searchHistory.saveTrack(it)
        }

        fun hideMenuHistory() {
            rvTrack.visibility = View.GONE
            tvYouSearched.visibility = View.GONE
        }

        var trackAdapter = TrackAdapter(mutableListOf())
        val buttonClickListener = TrackAdapter.ButtonClickListener {
            sharedPreferences.edit().clear().apply()
            searchHistory.clearHistory()
            trackAdapter.setUpTracks(searchHistory.tracksBufferSaved as ArrayList<TrackSearchItem.Track>)
            hideMenuHistory()
        }

        trackAdapter = TrackAdapter(mutableListOf(), trackClickListener, buttonClickListener)

        rvTrack.adapter = trackAdapter

        val inputEditTextSearch = findViewById<EditText>(R.id.inputEditTextSearch)
        val toolbarSearch = findViewById<Toolbar>(R.id.toolbarSearchActivity)

        val phLayoutError = findViewById<LinearLayout>(R.id.phLayoutError)
        val bUpdateSearch = findViewById<Button>(R.id.bUpdateSearch)

        val tracks = sharedPreferences.getString(TRACKS_LIST_KEY, null)
        if (tracks != null) {
//            rvTrack.visibility = View.VISIBLE
//            bClearHistory.visibility = View.VISIBLE
//            tvYouSearched.visibility = View.VISIBLE
            searchHistory.tracksBufferSaved =
                searchHistory.createTrackFromJson(tracks).toMutableList()
            trackAdapter.setUpTracks(searchHistory.getSavedTracks() as ArrayList<TrackSearchItem.Track>)
            if (searchHistory.getSavedTracks().isEmpty()) {
                hideMenuHistory()
            }
        }

        toolbarSearch.setNavigationIcon(R.drawable.arrow_back_mode)
        toolbarSearch.setNavigationOnClickListener { finish() }
        toolbarSearch.setTitleTextAppearance(
            this,
            R.style.SecondsActivityMediumTextAppearance
        )

        val clearTextSearchIcon = findViewById<ImageView>(R.id.clearTextSearchIcon)
        clearTextSearchIcon.setOnClickListener {
            inputEditTextSearch.setText("")
            this.currentFocus?.let { view ->
                val imm =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                phLayoutError.visibility = View.GONE
                hideMenuHistory()
            }
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int,
            ) {
                clearTextSearchIcon.visibility = clearButtonVisibility(s)
                rvTrack.visibility = clearButtonVisibility(s)
                tvYouSearched.visibility =
                    if (inputEditTextSearch.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                rvTrack.visibility =
                    if (inputEditTextSearch.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                if (s != null) {
                    inputTextFromSearch = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditTextSearch.addTextChangedListener(searchTextWatcher)

        fun trackSearch() {
            hideMenuHistory()
            if (inputEditTextSearch.text.isNotEmpty()) {
                iTunesService.search(inputEditTextSearch.text.toString())
                    .enqueue(object : Callback<TracksResponse> {
                        override fun onResponse(
                            call: Call<TracksResponse>,
                            response: Response<TracksResponse>,
                        ) {
                            if (response.code() == 200) {
                                phLayoutError.visibility = View.GONE
                                bUpdateSearch.visibility = View.GONE
                                hideMenuHistory()
                                rvTrack.visibility = View.VISIBLE
                                trackList.clear()
                                trackAdapter.setUpTracks(trackList)
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    trackList.addAll(response.body()?.results!!)
                                    trackAdapter.setUpTracks(trackList)
                                } else {
                                    hideMenuHistory()
                                    bindErrors(
                                        SearchError.EMPTY_SEARCH_ERROR,
                                        bUpdateSearch,
                                        phLayoutError
                                    )
                                }
                            } else {
                                hideMenuHistory()
                                bindErrors(
                                    SearchError.INTERNET_CONNECTION_ERROR,
                                    bUpdateSearch,
                                    phLayoutError
                                )
                            }
                        }

                        override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                            hideMenuHistory()
                            bindErrors(
                                SearchError.INTERNET_CONNECTION_ERROR,
                                bUpdateSearch,
                                phLayoutError
                            )
                        }
                    })
            } else {
                trackList.clear()
            }
        }

        inputEditTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && inputEditTextSearch.text.isNotEmpty()) {
                trackSearch()
                true
            }
            false
        }
        bUpdateSearch.setOnClickListener { trackSearch() }

        inputEditTextSearch.setOnFocusChangeListener { view, hasFocus ->
            tvYouSearched.visibility =
                if (hasFocus && inputEditTextSearch.text.isEmpty() && searchHistory.tracksBufferSaved
                        .isNotEmpty()
                ) View.VISIBLE else View.GONE
            rvTrack.visibility =
                if (hasFocus && inputEditTextSearch.text.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        val sharedPreferences =
            getSharedPreferences(PRACTICUM_PLAYLISTMARKER_PREFERENCES_TRACKLIST, MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(
                TRACKS_LIST_KEY,
                searchHistory.createJsonFromTracksList(searchHistory.tracksBufferSaved as ArrayList<TrackSearchItem.Track>)
            )
            .apply()
    }

    private fun bindErrors(error: SearchError, bUpdateSearch: View, phLayoutError: View) {
        trackList.clear()
        val tvErrorSearch = findViewById<TextView>(R.id.tvErrorSearch)
        val ivErrorConnection = findViewById<ImageView>(R.id.ivErrorConnection)

        when (error) {
            SearchError.INTERNET_CONNECTION_ERROR -> {
                phLayoutError.visibility = View.VISIBLE
                tvErrorSearch.text = getString(R.string.error_internet_connection)
                ivErrorConnection.setImageResource(R.drawable.internet_error)
                bUpdateSearch.visibility = View.VISIBLE
            }
            SearchError.EMPTY_SEARCH_ERROR -> {
                phLayoutError.visibility = View.VISIBLE
                tvErrorSearch.text = getString(R.string.nothing_found)
                ivErrorConnection.setImageResource(R.drawable.nothing_found)
                bUpdateSearch.visibility = View.GONE
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT_SEARCH, inputTextFromSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputTextFromSearch = savedInstanceState.getString(INPUT_TEXT_SEARCH, "")
    }

    companion object {
        const val INPUT_TEXT_SEARCH = "INPUT_TEXT_SEARCH"
    }
}
