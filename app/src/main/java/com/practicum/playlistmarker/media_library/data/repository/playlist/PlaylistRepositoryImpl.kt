package com.practicum.playlistmarker.media_library.data.repository.playlist

import com.practicum.playlistmarker.media_library.data.converters.TrackDbConvertor
import com.practicum.playlistmarker.media_library.data.converters.PlaylistConvertor
import com.practicum.playlistmarker.media_library.data.db.entity.TrackEntity
import com.practicum.playlistmarker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmarker.media_library.data.db.playlist.PlaylistDatabase
import com.practicum.playlistmarker.media_library.data.db.playlist.TrackPlaylistDataBase
import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistRepository
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDatabase: PlaylistDatabase,
    private val playlistConvertor: PlaylistConvertor,
    private val trackPlaylistDatabase: TrackPlaylistDataBase,
    private val trackDbConvertor: TrackDbConvertor,
) : PlaylistRepository {
    override suspend fun addPlaylist(
        playlistName: String,
        playlistDescription: String?,
        uri: String?,
    ) {
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

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) {
        playlistDatabase.playlistDao().updatePlaylists(convertFromPlaylist(playlist))
        trackPlaylistDatabase.trackPlaylistDao().insertTrack(convertFromTrackEntity(track))
    }

    private fun convertFromTrackEntity(track: Track): TrackEntity {
        return trackDbConvertor.map(track)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistConvertor.map(playlist) }
    }

    private fun convertFromPlaylist(playlist: Playlist): PlaylistEntity {
        return playlistConvertor.map(playlist)
    }
}