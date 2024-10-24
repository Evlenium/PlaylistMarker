package com.practicum.playlistmarker.search.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.FragmentSearchBinding
import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.player.ui.AudioPlayerFragment
import com.practicum.playlistmarker.search.presentation.TrackMapper
import com.practicum.playlistmarker.search.presentation.TracksSearchViewModel
import com.practicum.playlistmarker.search.presentation.TracksState
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private var inputTextFromSearch: String? = null
    private var flagSuccessfulDownload: Boolean = false

    private lateinit var twSearch: TextWatcher
    private lateinit var recyclerViewTrack: RecyclerView
    private lateinit var textViewYouSearched: TextView
    private lateinit var inputEditTextSearch: EditText
    private lateinit var placeHolderLayoutError: LinearLayout
    private lateinit var buttonUpdateSearch: Button
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var progressBarSearch: ProgressBar
    private lateinit var textViewErrorSearch: TextView
    private lateinit var imageViewErrorConnection: ImageView

    private val viewModel by viewModel<TracksSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            textViewYouSearched = binding.tvYouSearched
            inputEditTextSearch = binding.etSearch
            placeHolderLayoutError = binding.phLayoutError
            buttonUpdateSearch = binding.bUpdateSearch
            textViewErrorSearch = binding.tvErrorSearch
            imageViewErrorConnection = binding.ivErrorConnection
            progressBarSearch = binding.pbSearch
            recyclerViewTrack = binding.rvTracks
            trackAdapter = TrackAdapter(mutableListOf())
        }

        var isClickAllowed = true

        fun clickDebounce(): Boolean {
            val current = isClickAllowed
            if (isClickAllowed) {
                isClickAllowed = false
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(CLICK_DEBOUNCE_DELAY)
                    isClickAllowed = true
                }
            }
            return current
        }

        val trackClickListener = TrackAdapter.TrackClickListener {
            if (clickDebounce()) {
                val track = TrackMapper.mapToTrack(it)
                viewModel.addToHistory(track)
                findNavController().navigate(
                    R.id.action_searchFragment_to_audioPlayerFragment,
                    AudioPlayerFragment.createArgs(it)
                )
            }

        }

        val buttonClearHistoryClickListener = TrackAdapter.ButtonClickListener {
            viewModel.clearHistoryData()
            hideMenuHistory()
        }
        trackAdapter =
            TrackAdapter(mutableListOf(), trackClickListener, null, buttonClearHistoryClickListener)
        recyclerViewTrack.adapter = trackAdapter
        showHistory()

        binding.iClearText.setOnClickListener {
            inputEditTextSearch.setText("")
            activity?.window?.currentFocus?.let { view ->
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                placeHolderLayoutError.isVisible = false
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
                binding.iClearText.visibility = clearButtonVisibility(s)
                if (s != null) {
                    if (s.isNotEmpty() && viewModel.lastText != s.toString()) {
                        inputTextFromSearch = s.toString()
                        viewModel.searchDebounce(inputTextFromSearch!!)
                    } else if (inputEditTextSearch.hasFocus() && s.isEmpty()) {
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
            progressBarSearch.visibility = View.GONE
            false
        }
        buttonUpdateSearch.setOnClickListener {
            if (inputTextFromSearch != null) {
                viewModel.searchDebounce(inputTextFromSearch!!)
            }
        }
        inputEditTextSearch.setOnFocusChangeListener { view, hasFocus ->
            showHistory()
        }
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun hideMenuHistory() {
        recyclerViewTrack.isVisible = false
        textViewYouSearched.isVisible = false
    }

    private fun showLoading() {
        hideMenuHistory()
        placeHolderLayoutError.isVisible = false
        buttonUpdateSearch.isVisible = false
        progressBarSearch.isVisible = true
    }

    private fun showErrorConnection(errorMessage: String) {
        hideMenuHistory()
        hideKeyboard(requireActivity())
        placeHolderLayoutError.isVisible = true
        progressBarSearch.isVisible = false
        textViewErrorSearch.text = errorMessage
        buttonUpdateSearch.isVisible = true
        imageViewErrorConnection.setImageResource(R.drawable.ph_internet_error)
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showEmptyTrackList(emptyMessage: String) {
        hideMenuHistory()
        placeHolderLayoutError.isVisible = true
        progressBarSearch.isVisible = false
        textViewErrorSearch.text = emptyMessage
        buttonUpdateSearch.isVisible = false
        imageViewErrorConnection.setImageResource(R.drawable.nothing_found)
    }

    private fun showHistory() {
        lifecycleScope.launch {
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
                textViewYouSearched.isVisible = true
                recyclerViewTrack.isVisible = true
                placeHolderLayoutError.isVisible = false
                progressBarSearch.isVisible = false
            } else {
                hideMenuHistory()
            }
        }

    }

    private fun showContent(tracks: ArrayList<Track>) {
        textViewYouSearched.isVisible = false
        recyclerViewTrack.isVisible = true
        placeHolderLayoutError.isVisible = false
        progressBarSearch.isVisible = false
        val trackList = tracks.map { track ->
            TrackMapper.mapToTrackSearchItem(track)
        }
        trackAdapter.setUpTracks(trackList)
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


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}