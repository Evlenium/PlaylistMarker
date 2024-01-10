package com.practicum.playlistmarker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
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

        @IgnoredOnParcel
        val dateYearFormat: String by lazy {
            SimpleDateFormat("yyyy").format(
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(releaseDate)
            )
        }

        @IgnoredOnParcel
        val trackTimeFormat: String by lazy {
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(trackTimeMillis.toLong())
        }

        fun getResizeUrlArtwork(): String {
            return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        }
    }
}