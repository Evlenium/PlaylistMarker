package com.practicum.playlistmarker.media_library.domain.model.playlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String?,
    val uri: String?,
    var trackIdList: List<String>,
    var counterTracks: Int,
) : Parcelable