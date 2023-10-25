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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    enum class SearchError {
        INTERNET_CONNECTION_ERROR,
        EMPTY_SEARCH_ERROR
    }

    private val iTunesSearchBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesSearchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()
        )
        .build()

    private
    val iTunesService = retrofit.create(iTunesApi::class.java)

    private val tracks = ArrayList<Track>()

    private var inputTextFromSearch: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val trackAdapter = TrackAdapter(ArrayList())
        val rvTrack: RecyclerView = findViewById(R.id.rv_tracks)
        rvTrack.adapter = trackAdapter

        val inputEditTextSearch = findViewById<EditText>(R.id.inputEditTextSearch)
        val toolbarSearch = findViewById<Toolbar>(R.id.toolbarSearchActivity)

        val phLayoutError = findViewById<LinearLayout>(R.id.phLayoutError)
        val bUpdateSearch = findViewById<Button>(R.id.bUpdateSearch)

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
                if (s != null) {
                    inputTextFromSearch = s.toString()
                }
                clearTextSearchIcon.visibility = clearButtonVisibility(s)
                rvTrack.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditTextSearch.addTextChangedListener(searchTextWatcher)

        fun trackSearch() {
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
                                tracks.clear()
                                trackAdapter.setUpTracks(tracks)
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    tracks.addAll(response.body()?.results!!)
                                    trackAdapter.setUpTracks(tracks)
                                } else {
                                    bindErrors(
                                        SearchError.EMPTY_SEARCH_ERROR,
                                        bUpdateSearch,
                                        phLayoutError
                                    )
                                }
                            } else {
                                bindErrors(
                                    SearchError.INTERNET_CONNECTION_ERROR,
                                    bUpdateSearch,
                                    phLayoutError
                                )
                            }
                        }

                        override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                            bindErrors(
                                SearchError.INTERNET_CONNECTION_ERROR,
                                bUpdateSearch,
                                phLayoutError
                            )
                        }
                    })
            } else {
                tracks.clear()
                trackAdapter.setUpTracks(tracks)
            }
        }
        inputEditTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackSearch()
                true
            }
            false
        }
        bUpdateSearch.setOnClickListener { trackSearch() }
    }

    private fun bindErrors(error: SearchError, bUpdateSearch: View, phLayoutError: View) {
        tracks.clear()
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
