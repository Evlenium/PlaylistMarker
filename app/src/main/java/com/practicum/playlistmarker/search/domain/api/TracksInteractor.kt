package com.practicum.playlistmarker.search.domain.api

import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}