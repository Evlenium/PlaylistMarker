package com.practicum.playlistmarker.media_library.domain.db.api

import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun getFavoriteTrackList(): List<String>
}