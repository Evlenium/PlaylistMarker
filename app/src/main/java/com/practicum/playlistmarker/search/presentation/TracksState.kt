package com.practicum.playlistmarker.search.presentation

import com.practicum.playlistmarker.player.domain.model.Track

sealed interface TracksState {
    object Loading : TracksState

    data class Content(
        val tracks: ArrayList<Track>,
    ) : TracksState

    data class Error(
        val errorMessage: String,
    ) : TracksState

    data class Empty(
        val message: String,
    ) : TracksState
}