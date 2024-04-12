package com.practicum.playlistmarker.media_library.domain.model.playlist

sealed interface PlaylistState {
    data class Content(
        val playlists: List<Playlist>,
    ) : PlaylistState

    object Empty : PlaylistState
}