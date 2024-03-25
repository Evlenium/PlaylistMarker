package com.practicum.playlistmarker.media_library.data.repository

import com.practicum.playlistmarker.media_library.data.converters.TrackDbConvertor
import com.practicum.playlistmarker.media_library.data.db.AppDatabase
import com.practicum.playlistmarker.media_library.data.db.entity.TrackEntity
import com.practicum.playlistmarker.media_library.domain.db.api.FavoriteRepository
import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteRepository {
    override suspend fun addTrack(track: Track) {
        appDatabase.trackDao().insertTrack(convertFromTrack(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(convertFromTrack(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

    private fun convertFromTrack(track: Track): TrackEntity {
        return trackDbConvertor.map(track)
    }
}