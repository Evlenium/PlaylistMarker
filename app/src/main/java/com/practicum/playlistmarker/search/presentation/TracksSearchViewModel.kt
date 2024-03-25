package com.practicum.playlistmarker.search.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmarker.search.domain.api.TracksInteractor
import com.practicum.playlistmarker.util.debounce
import kotlinx.coroutines.launch

class TracksSearchViewModel(
    application: Application,
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val stateLiveData = MutableLiveData<TracksState>()

    fun observeState(): LiveData<TracksState> = stateLiveData

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            searchRequest(changedText)
        }

    fun searchDebounce(changedText: String) {
        trackSearchDebounce(changedText)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(newSearchText)
                    .collect { pair ->
                        val tracks = ArrayList<Track>()
                        if (pair.first != null) {
                            tracks.addAll(pair.first!!)
                        }

                        when {
                            pair.second != null -> {
                                renderState(
                                    TracksState.Error(
                                        errorMessage = getApplication<Application>().getString(R.string.error_internet_connection),
                                    )
                                )
                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    TracksState.Empty(
                                        message = getApplication<Application>().getString(R.string.nothing_found),
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    TracksState.Content(
                                        tracks = tracks,
                                    )
                                )
                            }
                        }

                    }
            }
        }
    }


    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun addToHistory(track: Track) {
        viewModelScope.launch{
            searchHistoryInteractor.addTrackToHistory(track)
        }
    }

    fun clearHistoryData() {
        searchHistoryInteractor.clearHistory()
    }

    suspend fun getTracksHistory(): List<Track> {
        return searchHistoryInteractor.getTracksHistory()
    }
}