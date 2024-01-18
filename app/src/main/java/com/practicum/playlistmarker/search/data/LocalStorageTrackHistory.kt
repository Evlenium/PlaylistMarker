package com.practicum.playlistmarker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmarker.App
import com.practicum.playlistmarker.search.data.dto.TrackDto

class LocalStorageTrackHistory(private val sharedPreferences: SharedPreferences) {
    private companion object {
        const val TRACKS_LIST_KEY = "key_for_tracks_list"
    }

    private var tracksBufferSaved = mutableListOf<TrackDto>()

    fun addTrackToHistory(track: TrackDto) {
        changeHistory(track)
    }

    fun clearHistory() {
        sharedPreferences.edit().clear().apply()
        tracksBufferSaved.clear()
    }

    fun getSavedHistory(): MutableList<TrackDto> {
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
            if (iterator.trackId == track.trackId||tracksBufferSaved.size >= 10) {
                trackIterator.remove()
            }
        }
        tracksBufferSaved.add(track)
        sharedPreferences.edit()
            .putString(
                App.TRACKS_LIST_KEY,
                createJsonFromTracksList(tracksBufferSaved as ArrayList<TrackDto>)
            )
            .apply()
    }

    private fun createJsonFromTracksList(tracks: ArrayList<TrackDto>): String {
        return Gson().toJson(tracks)
    }

    private fun createTrackFromJson(json: String): Array<TrackDto> {
        return Gson().fromJson(json, Array<TrackDto>::class.java)
    }
}