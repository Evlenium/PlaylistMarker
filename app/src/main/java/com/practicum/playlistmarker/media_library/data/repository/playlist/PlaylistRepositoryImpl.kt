package com.practicum.playlistmarker.media_library.data.repository.playlist

import com.practicum.playlistmarker.media_library.data.converters.playlist.PlaylistConvertor
import com.practicum.playlistmarker.media_library.data.db.entity.playlist.PlaylistEntity
import com.practicum.playlistmarker.media_library.data.db.playlist.PlaylistDatabase
import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistRepository
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDatabase: PlaylistDatabase,
    private val playlistConvertor: PlaylistConvertor,
) : PlaylistRepository {
    override suspend fun addPlaylist(playlistName: String, playlistDescription: String?, uri: String?) {
        val playlist = Playlist(
            0,
            playlistName,
            playlistDescription,
            uri,
            emptyList(),
            0
        )
        playlistDatabase.playlistDao().insertPlaylist(convertFromPlaylist(playlist))
    }

    override fun deletePlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao().deletePlaylist(convertFromPlaylist(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun updatePlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao().updatePlaylists(convertFromPlaylist(playlist))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistConvertor.map(playlist) }
    }

    private fun convertFromPlaylist(playlist: Playlist): PlaylistEntity {
        return playlistConvertor.map(playlist)
    }
}