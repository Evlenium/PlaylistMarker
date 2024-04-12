package com.practicum.playlistmarker.media_library.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmarker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist


class PlaylistConvertor(private val gson: Gson) {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            uri = playlist.uri,
            trackIdList = gson.fromJson(
                playlist.trackIdList,
                object : TypeToken<ArrayList<String?>?>() {}.type
            ),
            counterTracks = playlist.arrayNumber,
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            uri = playlist.uri,
            trackIdList = gson.toJson(playlist.trackIdList),
            arrayNumber = playlist.counterTracks,
        )
    }
}