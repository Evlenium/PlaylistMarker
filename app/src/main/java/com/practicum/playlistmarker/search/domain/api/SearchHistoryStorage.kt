package com.practicum.playlistmarker.search.domain.api

import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.search.data.dto.TrackDto

interface SearchHistoryStorage {
    suspend fun addTrackToHistory(track: TrackDto)
    fun clearHistory()
    suspend fun getSavedHistory(): List<Track>
}