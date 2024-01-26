package com.practicum.playlistmarker.search.domain.use_case

import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.search.data.repository.LocalSearchHistoryStorage
import com.practicum.playlistmarker.search.domain.MapperDto
import com.practicum.playlistmarker.search.domain.api.SearchHistoryInteractor

class SearchHistoryInteractorImpl(private val localSearchHistoryStorage: LocalSearchHistoryStorage) :
    SearchHistoryInteractor {
    override fun addTrackToHistory(track: Track) {
        localSearchHistoryStorage.addTrackToHistory(MapperDto.mapToTrackFromTrackDto(track))
    }

    override fun clearHistory() {
        localSearchHistoryStorage.clearHistory()
    }

    override fun getTracksHistory(): List<Track> {
        val trackList = localSearchHistoryStorage.getSavedHistory().map { track ->
            MapperDto.mapFromTrackToTrackDto(track)
        }
        return trackList
    }
}