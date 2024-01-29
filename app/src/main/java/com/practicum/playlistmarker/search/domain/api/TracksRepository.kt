package com.practicum.playlistmarker.search.domain.api

import com.practicum.playlistmarker.search.data.dto.TrackDto
import com.practicum.playlistmarker.util.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<TrackDto>>
}