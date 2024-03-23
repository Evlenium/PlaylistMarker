package com.practicum.playlistmarker.media_library.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmarker.media_library.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table")
    fun getTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<String>
}