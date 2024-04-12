package com.practicum.playlistmarker.media_library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistInteractor
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import com.practicum.playlistmarker.media_library.domain.model.playlist.PlaylistState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun observePlaylistState(): LiveData<PlaylistState> = playlistStateLiveData

    fun fillDataPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlist ->
                processResult(playlist)
            }
        }
    }

    private fun processResult(playlist: List<Playlist>) {
        if (playlist.isNotEmpty()) {
            renderState(PlaylistState.Content(playlist))
        } else {
            renderState(PlaylistState.Empty)
        }
    }

    private fun renderState(state: PlaylistState) {
        playlistStateLiveData.postValue(state)
    }
}