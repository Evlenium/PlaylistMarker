package com.practicum.playlistmarker.media_library.domain.db.api.playlist

import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlistName: String, playlistDescription: String?, uri: String?)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun deleteTrackFromPlaylist(trackId: String, playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)

    suspend fun getPlaylistById(playlistId: Int): Playlist

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun getTracksInPlaylist(tracksId: List<String>): Flow<List<Track>>
}