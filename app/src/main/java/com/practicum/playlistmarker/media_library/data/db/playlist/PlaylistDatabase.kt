package com.practicum.playlistmarker.media_library.data.db.playlist

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmarker.media_library.data.db.entity.playlist.PlaylistEntity

@Database(version = 1, entities = [PlaylistEntity::class], exportSchema = false)
abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}