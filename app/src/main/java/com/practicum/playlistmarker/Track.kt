package com.practicum.playlistmarker

import java.io.Serializable

sealed class TrackSearchItem: Serializable {
    object Button : TrackSearchItem()
    data class Track(
        val trackName: String,
        val artistName: String,
        val trackTimeMillis: String,
        val artworkUrl100: String,
        val trackId: String,
        val collectionName: String,
        val releaseDate: String,
        val primaryGenreName: String,
        val country: String,
    ) : TrackSearchItem()
}