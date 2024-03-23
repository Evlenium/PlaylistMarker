package com.practicum.playlistmarker.search.domain.api

import com.practicum.playlistmarker.player.domain.model.Track

interface SearchHistoryInteractor {
    suspend fun addTrackToHistory(track: Track)
    fun clearHistory()
    suspend fun getTracksHistory(): List<Track>
}