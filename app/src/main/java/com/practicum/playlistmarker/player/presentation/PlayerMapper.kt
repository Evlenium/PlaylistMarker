package com.practicum.playlistmarker.player.presentation

import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem

object PlayerMapper {
    fun mapToTrackForPlayer(track: TrackSearchItem.Track): Track {
        return Track(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
    }
}