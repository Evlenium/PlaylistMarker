package com.practicum.playlistmarker.media_library.data.db.playlist

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmarker.media_library.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class], exportSchema = false)
abstract class TrackPlaylistDataBase : RoomDatabase() {
    abstract fun trackPlaylistDao(): TrackPlaylistDAO
}