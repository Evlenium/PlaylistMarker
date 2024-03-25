package com.practicum.playlistmarker.search.domain.api

import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}