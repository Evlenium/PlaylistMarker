package com.practicum.playlistmarker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

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
    ) : TrackSearchItem() {
        fun getResizeUrlArtwork(): String {
            return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        }

        fun getYearRelease(): String {
            return SimpleDateFormat("yyyy").format(
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(releaseDate)
            )
        }

        fun getTrackTime(): String {
            return SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(trackTimeMillis.toLong())
        }
    }
}