package com.practicum.playlistmarker

import com.google.gson.Gson

class SearchHistory {

    var tracksBufferSaved = mutableListOf<Track>()
    fun getSavedTracks(): MutableList<Track> {
        return tracksBufferSaved
    }

    fun saveTrack(item: Track) {
        if (tracksBufferSaved.size >= 10) {
            return
        }
        val trackIterator = tracksBufferSaved.iterator()
        while (trackIterator.hasNext()) {
            val track = trackIterator.next()
            if (item.trackId == track.trackId) {
                trackIterator.remove()
            }
        }
        tracksBufferSaved.add(item)
    }

    fun clearHistory() {
        tracksBufferSaved.clear()
    }

    fun createJsonFromTracksList(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    fun createTrackFromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }
}