package com.practicum.playlistmarker.search.presentation

import com.practicum.playlistmarker.search.data.dto.TrackDto

sealed interface TracksState {
    object Loading : TracksState

    data class Content(
        val tracks: ArrayList<TrackDto>
    ) : TracksState

    data class Error(
        val errorMessage: String
    ) : TracksState

    data class Empty(
        val message: String
    ) : TracksState
}