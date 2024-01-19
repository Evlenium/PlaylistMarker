package com.practicum.playlistmarker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.App.Companion.TRACK
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.player.ui.AudioPlayerActivity
import com.practicum.playlistmarker.search.presentation.TrackMapper
import com.practicum.playlistmarker.search.presentation.TracksSearchViewModel
import com.practicum.playlistmarker.search.presentation.TracksState
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem
import com.practicum.playlistmarker.search.ui.TrackAdapter.TrackClickListener

class SearchActivity : AppCompatActivity() {

    private var inputTextFromSearch: String? = null

    private var flagSuccessfulDownload: Boolean = false

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var twSearch: TextWatcher
    private lateinit var rvTrack: RecyclerView
    private lateinit var tvYouSearched: TextView
    private lateinit var inputEditTextSearch: EditText
    private lateinit var phLayoutError: LinearLayout
    private lateinit var bUpdateSearch: Button
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var pbSearch: ProgressBar

    private lateinit var tvErrorSearch: TextView
    private lateinit var ivErrorConnection: ImageView

    private lateinit var viewModel: TracksSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(
            this,
            TracksSearchViewModel.getViewModelFactory()
        )[TracksSearchViewModel::class.java]

        rvTrack = findViewById(R.id.rvTracks)
        tvYouSearched = findViewById(R.id.tvYouSearched)

        inputEditTextSearch = findViewById(R.id.inputEditTextSearch)
        val toolbarSearch = findViewById<Toolbar>(R.id.toolbarSearchActivity)

        phLayoutError = findViewById(R.id.phLayoutError)
        bUpdateSearch = findViewById(R.id.bUpdateSearch)

        tvErrorSearch = findViewById(R.id.tvErrorSearch)
        ivErrorConnection = findViewById(R.id.ivErrorConnection)

        pbSearch = findViewById(R.id.progressBarSearch)

        trackAdapter = TrackAdapter(mutableListOf())

        val trackClickListener = TrackClickListener {
            if (clickDebounce()) {
                val track = TrackMapper.mapToTrack(it)
                viewModel.addToHistory(track)
                val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java)
                audioPlayerIntent.putExtra(TRACK, it)
                startActivity(audioPlayerIntent)
            }
        }
        val buttonClearHistoryClickListener = TrackAdapter.ButtonClickListener {
            viewModel.clearHistoryData()
            hideMenuHistory()
        }

        trackAdapter =
            TrackAdapter(mutableListOf(), trackClickListener, buttonClearHistoryClickListener)

        rvTrack.adapter = trackAdapter
        showHistory()


        toolbarSearch.setNavigationIcon(R.drawable.bt_arrow_back_mode)
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

        twSearch = object : TextWatcher {
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
                if (s != null) {
                    if (s.isNotEmpty()) {
                        inputTextFromSearch = s.toString()
                        viewModel.searchDebounce(inputTextFromSearch!!)
                    }
                    else if(inputEditTextSearch.hasFocus()&&s.isEmpty()){
                        showHistory()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditTextSearch.addTextChangedListener(twSearch)
        inputEditTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && inputEditTextSearch.text.isNotEmpty()) {
                if (!flagSuccessfulDownload) {
                    inputTextFromSearch?.let { viewModel.searchDebounce(it) }
                }
                true
            }
            pbSearch.visibility = View.GONE
            false
        }
        bUpdateSearch.setOnClickListener {
            if (inputTextFromSearch != null) {
                viewModel.searchDebounce(inputTextFromSearch!!)
            }
        }

        inputEditTextSearch.setOnFocusChangeListener { view, hasFocus ->
            showHistory()
        }
        viewModel.observeState().observe(this) {
            render(it)
        }
    }

    private fun hideMenuHistory() {
        rvTrack.visibility = View.GONE
        tvYouSearched.visibility = View.GONE
    }

    private fun showLoading() {
        hideMenuHistory()
        phLayoutError.visibility = View.GONE
        pbSearch.visibility = View.VISIBLE
    }

    private fun showErrorConnection(errorMessage: String) {
        hideMenuHistory()
        phLayoutError.visibility = View.VISIBLE
        pbSearch.visibility = View.GONE
        tvErrorSearch.text = errorMessage
        bUpdateSearch.visibility = View.VISIBLE
        ivErrorConnection.setImageResource(R.drawable.ph_internet_error)
    }

    private fun showEmptyTrackList(emptyMessage: String) {
        hideMenuHistory()
        phLayoutError.visibility = View.VISIBLE
        pbSearch.visibility = View.GONE
        tvErrorSearch.text = emptyMessage
        bUpdateSearch.visibility = View.GONE
        ivErrorConnection.setImageResource(R.drawable.nothing_found)
    }

    private fun showHistory() {
        val tracks = viewModel.getTracksHistory()
        if (tracks.isNotEmpty()) {
            val searchSaved = mutableListOf<TrackSearchItem>()
            val trackList = tracks.map { track ->
                TrackMapper.mapToTrackSearchWithButton(track)
            }
            if (trackList.isNotEmpty()) {
                searchSaved.addAll(trackList.asReversed())
                searchSaved.add(TrackSearchItem.Button)
            }
            trackAdapter.setUpTracks(searchSaved)
            tvYouSearched.visibility = View.VISIBLE
            rvTrack.visibility = View.VISIBLE
            phLayoutError.visibility = View.GONE
            pbSearch.visibility = View.GONE
        }
        else{
            hideMenuHistory()
        }
    }

    private fun showContent(tracks: ArrayList<Track>) {
        tvYouSearched.visibility = View.GONE
        rvTrack.visibility = View.VISIBLE
        phLayoutError.visibility = View.GONE
        pbSearch.visibility = View.GONE
        val trackList = tracks.map { track ->
            TrackMapper.mapToTrackSearchItem(track)
        }
        trackAdapter.setUpTracks(trackList)
        trackAdapter.notifyDataSetChanged()
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Empty -> showEmptyTrackList(state.message)
            is TracksState.Error -> showErrorConnection(state.errorMessage)
            is TracksState.Loading -> showLoading()
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

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val INPUT_TEXT_SEARCH = "INPUT_TEXT_SEARCH"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
