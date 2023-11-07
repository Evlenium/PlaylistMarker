package com.practicum.playlistmarker

sealed class TrackSearchItem{
    object Button : TrackSearchItem()
    data class Track(
        val trackName: String,
        val artistName: String,
        val trackTimeMillis: String,
        val artworkUrl100: String,
        val trackId: String,
    ) : TrackSearchItem()
}