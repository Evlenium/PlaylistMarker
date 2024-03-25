package com.practicum.playlistmarker.search.data.converters

import com.practicum.playlistmarker.media_library.data.db.AppDatabase
import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.search.data.dto.TrackDto
import com.practicum.playlistmarker.search.domain.MapperDto

class TrackFavoriteConvertorFromDatabase(private val appDatabase: AppDatabase) {

    suspend fun getFavoriteTracks(tracks: List<TrackDto>): List<Track> {
        val trackIdListFavorites = appDatabase.trackDao().getTracksId()
        val trackList =
            tracks.map { trackDto -> MapperDto.mapFromTrackToTrackDto(trackDto) }
        trackList.forEach { track: Track ->
            trackIdListFavorites.forEach { trackId ->
                if (track.trackId == trackId) {
                    track.isFavorite = true
                }
            }
        }
        return trackList
    }
}