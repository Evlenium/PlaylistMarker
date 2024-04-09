package com.practicum.playlistmarker.player.domain.model

sealed interface TrackPlaylistState {
    data class InPlaylist(val name: String) : TrackPlaylistState
    data class NotInPlaylist(val name: String) : TrackPlaylistState
}