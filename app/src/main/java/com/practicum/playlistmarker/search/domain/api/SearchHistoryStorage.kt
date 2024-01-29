package com.practicum.playlistmarker.search.domain.api

import com.practicum.playlistmarker.search.data.dto.TrackDto

interface SearchHistoryStorage {
    fun addTrackToHistory(track: TrackDto)
    fun clearHistory()
    fun getSavedHistory(): MutableList<TrackDto>
}