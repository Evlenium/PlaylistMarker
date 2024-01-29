package com.practicum.playlistmarker.search.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmarker.search.data.dto.TrackDto
import com.practicum.playlistmarker.search.domain.api.SearchHistoryStorage

class LocalSearchHistoryStorage(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : SearchHistoryStorage {
    private companion object {
        const val TRACKS_LIST_KEY = "key_for_tracks_list"
    }

    private var tracksBufferSaved = mutableListOf<TrackDto>()

    override fun addTrackToHistory(track: TrackDto) {
        changeHistory(track)
    }

    override fun clearHistory() {
        sharedPreferences.edit().clear().apply()
        tracksBufferSaved.clear()
    }

    override fun getSavedHistory(): MutableList<TrackDto> {
        val tracks = sharedPreferences.getString(TRACKS_LIST_KEY, null)
        if (tracks != null) {
            tracksBufferSaved =
                createTrackFromJson(tracks).toMutableList()
        }
        return tracksBufferSaved
    }

    private fun changeHistory(track: TrackDto) {
        tracksBufferSaved = getSavedHistory().toMutableList()
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