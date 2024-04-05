package com.practicum.playlistmarker.media_library.data.db.entity.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    @ColumnInfo
    val playlistName: String,
    val playlistDescription: String?,
    val uri: String?,
    val trackIdList: String?,
    val arrayNumber: Int?,
)

