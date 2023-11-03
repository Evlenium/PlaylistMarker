package com.practicum.playlistmarker

import com.google.gson.Gson

class SearchHistory {

    var tracksBufferSaved = mutableListOf<TrackSearchItem.Track>()
    var searchSaved = mutableListOf<TrackSearchItem>()
    fun getSavedTracks(): MutableList<TrackSearchItem> {
        val trackIterator = tracksBufferSaved.iterator()
        while (trackIterator.hasNext()) {
            val track = trackIterator.next()
            if (track.trackId == null) {
                trackIterator.remove()
            }
        }
        searchSaved.clear()
        if (tracksBufferSaved.isNotEmpty()) {
            searchSaved.addAll(tracksBufferSaved.asReversed())
            searchSaved.add(TrackSearchItem.Button)
        }
        return searchSaved
    }

    fun saveTrack(item: TrackSearchItem.Track) {
        val trackIterator = tracksBufferSaved.iterator()
        while (trackIterator.hasNext()) {
            val track = trackIterator.next()
            if (item.trackId == track.trackId||tracksBufferSaved.size > 10) {
                trackIterator.remove()
            }
        }
        tracksBufferSaved.add(item)
    }

    fun clearHistory() {
        tracksBufferSaved.clear()
    }

    fun createJsonFromTracksList(tracks: ArrayList<TrackSearchItem.Track>): String {
        return Gson().toJson(tracks)
    }

    fun createTrackFromJson(json: String): Array<TrackSearchItem.Track> {
        return Gson().fromJson(json, Array<TrackSearchItem.Track>::class.java)
    }
}