package com.practicum.playlistmarker.media_library.data.db.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmarker.media_library.data.db.entity.TrackEntity

@Dao
interface TrackPlaylistDAO {
    @Insert(entity = TrackEntity::class, OnConflictStrategy.IGNORE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(trackEntity: TrackEntity)
}