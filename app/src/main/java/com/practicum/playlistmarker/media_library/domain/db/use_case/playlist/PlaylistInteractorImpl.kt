package com.practicum.playlistmarker.media_library.domain.db.use_case.playlist

import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistInteractor
import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistRepository
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun addPlaylist(
        playlistName: String,
        playlistDescription: String?,
        uri: String?,
    ) {
        playlistRepository.addPlaylist(playlistName, playlistDescription, uri)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
            .map { playlists: List<Playlist> -> playlists.reversed() }
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistRepository.addTrackToPlaylist(playlist, track)
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override suspend fun getTracksInPlaylist(tracksId: List<String>): Flow<List<Track>> {
        return playlistRepository.getTracksInPlaylist(tracksId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, playlist: Playlist) {
        playlistRepository.deleteTrackFromPlaylist(trackId, playlist)
    }
}