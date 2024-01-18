package com.practicum.playlistmarker.search.domain.api

import com.practicum.playlistmarker.search.data.dto.TrackDto

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    fun addTrackToHistory(track: TrackDto)
    fun clearHistory()
    fun getTracksHistory(): MutableList<TrackDto>
    interface TracksConsumer {
        fun consume(foundTracks: List<TrackDto>?, errorMessage: String?)
    }
}