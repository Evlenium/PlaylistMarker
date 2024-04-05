package com.practicum.playlistmarker.media_library.domain.db.use_case.playlist

import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistInteractor
import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistRepository
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun addPlaylist(playlistName: String, playlistDescription: String?, uri: String?) {
        playlistRepository.addPlaylist(playlistName, playlistDescription, uri)
    }

    override fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
            .map { playlists: List<Playlist> -> playlists.reversed() }
    }

    override fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }
}