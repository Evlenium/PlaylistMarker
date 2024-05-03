package com.practicum.playlistmarker.media_library.data.repository.playlist

import com.practicum.playlistmarker.media_library.data.converters.PlaylistConvertor
import com.practicum.playlistmarker.media_library.data.converters.TrackDbConvertor
import com.practicum.playlistmarker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmarker.media_library.data.db.entity.TrackEntity
import com.practicum.playlistmarker.media_library.data.db.playlist.PlaylistDatabase
import com.practicum.playlistmarker.media_library.data.db.playlist.TrackPlaylistDataBase
import com.practicum.playlistmarker.media_library.domain.db.api.FavoriteRepository
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
    private val favoriteRepository: FavoriteRepository
) : PlaylistRepository {
    override suspend fun addPlaylist(
        playlistName: String,
        playlistDescription: String?,
        uri: String?,
    ) {
        val playlist = Playlist(
            playlistId = 0,
            playlistName = playlistName,
            playlistDescription = playlistDescription,
            uri = uri,
            trackIdList = emptyList(),
            counterTracks = 0
        )
        playlistDatabase.playlistDao().insertPlaylist(convertFromPlaylist(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao().deletePlaylist(convertFromPlaylist(playlist))
        playlist.trackIdList.forEach { trackId ->
            deleteTrackFromPlaylist(trackId, playlist)
        }
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, playlist: Playlist) {
        playlist.trackIdList = playlist.trackIdList.filter { it != trackId }
        if (playlist.counterTracks > 0) {
            playlist.counterTracks = playlist.counterTracks.minus(1)
        }
        playlistDatabase.playlistDao().updatePlaylists(convertFromPlaylist(playlist))
        if (!getIdAllTracks().contains(trackId)) {
            trackPlaylistDatabase.trackPlaylistDao().deleteTrack(trackId)
        }
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlist.trackIdList = playlist.trackIdList + track.trackId
        playlist.counterTracks = playlist.counterTracks.plus(1)
        playlistDatabase.playlistDao().updatePlaylists(convertFromPlaylist(playlist))
        trackPlaylistDatabase.trackPlaylistDao().insertTrack(convertFromTrackEntity(track))
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        val playlist = playlistDatabase.playlistDao().getPlaylistById(playlistId)
        return playlistConvertor.map(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao().updatePlaylists(convertFromPlaylist(playlist))
    }

    override suspend fun getTracksInPlaylist(tracksId: List<String>): Flow<List<Track>> = flow {
        val favoriteTrackList = favoriteRepository.getFavoriteTrackList()
        val trackList =
            convertFromTrackEntityMap(trackPlaylistDatabase.trackPlaylistDao().getTracks())
        val trackListResult = mutableListOf<Track>()
        trackList.forEach {
            if (it.trackId in tracksId) {
                trackListResult.add(it)
            }
        }
        trackListResult.forEach { track: Track ->
            favoriteTrackList.forEach { trackId ->
                if (trackId == track.trackId) {
                    track.isFavorite = true
                }
            }
        }
        emit(trackListResult.toList())
    }

    private suspend fun getIdAllTracks(): MutableList<String> {
        val tracksId = mutableListOf<String>()
        val playlists = convertFromPlaylistEntity(playlistDatabase.playlistDao().getPlaylists())
        playlists.forEach { playlist: Playlist -> tracksId += playlist.trackIdList }
        return tracksId
    }

    private fun convertFromTrackEntity(track: Track): TrackEntity {
        return trackDbConvertor.map(track)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistConvertor.map(playlist) }
    }

    private fun convertFromTrackEntityMap(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

    private fun convertFromPlaylist(playlist: Playlist): PlaylistEntity {
        return playlistConvertor.map(playlist)
    }
}