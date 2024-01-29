package com.practicum.playlistmarker.search.domain.api

import com.practicum.playlistmarker.player.domain.model.Track

interface SearchHistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getTracksHistory(): List<Track>
}