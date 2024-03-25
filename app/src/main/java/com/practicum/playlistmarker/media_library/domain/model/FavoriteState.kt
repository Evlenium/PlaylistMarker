package com.practicum.playlistmarker.media_library.domain.model

import com.practicum.playlistmarker.player.domain.model.Track

sealed interface FavoriteState {
    data class Content(
        val tracks: List<Track>,
    ) : FavoriteState

    object Empty : FavoriteState
}