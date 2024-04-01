package com.practicum.playlistmarker.search.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmarker.media_library.data.db.AppDatabase
import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.search.data.converters.TrackFavoriteConvertorFromDatabase
import com.practicum.playlistmarker.search.data.dto.TrackDto
import com.practicum.playlistmarker.search.domain.MapperDto
import com.practicum.playlistmarker.search.domain.api.SearchHistoryStorage

class LocalSearchHistoryStorage(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val trackFavoriteConvertorFromDatabase: TrackFavoriteConvertorFromDatabase,
) : SearchHistoryStorage {
    private companion object {
        const val TRACKS_LIST_KEY = "key_for_tracks_list"
    }

    private var tracksBufferSaved = mutableListOf<TrackDto>()

    override suspend fun addTrackToHistory(track: TrackDto) {
        changeHistory(track)
    }

    override fun clearHistory() {
        sharedPreferences.edit().clear().apply()
        tracksBufferSaved.clear()
    }

    override suspend fun getSavedHistory(): List<Track> {
        val tracks = sharedPreferences.getString(TRACKS_LIST_KEY, null)
        if (tracks != null) {
            tracksBufferSaved =
                createTrackFromJson(tracks).toMutableList()
        }
        return trackFavoriteConvertorFromDatabase.getFavoriteTracks(tracksBufferSaved)
    }

    private suspend fun changeHistory(track: TrackDto) {
        val savedHistory =
            getSavedHistory().map { trackTmp -> MapperDto.mapToTrackFromTrackDto(trackTmp) }
        tracksBufferSaved = savedHistory.toMutableList()
        val trackIterator = tracksBufferSaved.iterator()
        while (trackIterator.hasNext()) {
            val iterator = trackIterator.next()
            if (iterator.trackId == track.trackId || tracksBufferSaved.size >= 10) {
                trackIterator.remove()
            }
        }
        tracksBufferSaved.add(track)
        sharedPreferences.edit()
            .putString(
                TRACKS_LIST_KEY,
                createJsonFromTracksList(tracksBufferSaved as ArrayList<TrackDto>)
            )
            .apply()
    }

    private fun createJsonFromTracksList(tracks: ArrayList<TrackDto>): String {
        return gson.toJson(tracks)
    }

    private fun createTrackFromJson(json: String): Array<TrackDto> {
        return gson.fromJson(json, Array<TrackDto>::class.java)
    }
}