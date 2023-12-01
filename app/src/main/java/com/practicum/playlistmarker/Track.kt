package com.practicum.playlistmarker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class TrackSearchItem : Parcelable {
    @Parcelize
    object Button : TrackSearchItem()

    @Parcelize
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
        val previewUrl: String,
    ) : TrackSearchItem()
}