package com.practicum.playlistmarker.media_library.data.db.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmarker.media_library.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Update(PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylists(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity
}