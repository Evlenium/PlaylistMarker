package com.practicum.playlistmarker.media_library.domain.db.use_case

import com.practicum.playlistmarker.media_library.domain.db.api.FavoriteRepository
import com.practicum.playlistmarker.media_library.domain.db.api.FavoriteInteractor
import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository) :
    FavoriteInteractor {
    override suspend fun addTrack(track: Track) {
        favoriteRepository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteRepository.deleteTrack(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.getFavoriteTracks()
            .map { tracks: List<Track> -> tracks.reversed() }
    }
}