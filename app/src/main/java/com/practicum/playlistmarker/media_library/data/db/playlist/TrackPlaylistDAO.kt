package com.practicum.playlistmarker.media_library.data.db.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmarker.media_library.data.db.entity.TrackEntity

@Dao
interface TrackPlaylistDAO {
    @Insert(entity = TrackEntity::class, OnConflictStrategy.IGNORE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Query("DELETE FROM track_table WHERE trackId in (:track)")
    suspend fun deleteTrack(track: String)

    @Query("SELECT * FROM track_table")
    suspend fun getTracks(): List<TrackEntity>
}