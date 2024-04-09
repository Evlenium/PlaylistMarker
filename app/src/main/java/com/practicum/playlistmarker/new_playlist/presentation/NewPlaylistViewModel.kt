package com.practicum.playlistmarker.new_playlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    fun savePlaylist(name: String, description: String?, uri: String?) {
        viewModelScope.launch { playlistInteractor.addPlaylist(name, description, uri) }
    }
}