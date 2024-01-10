package com.practicum.playlistmarker.data

import com.practicum.playlistmarker.domain.model.TrackSearchItem
import kotlinx.coroutines.flow.Flow

interface Player {
    fun onPause()
    fun onDestroy()
    fun preparePlayer(track: TrackSearchItem.Track)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun getPlayerStateFlow(): Flow<Int>
    fun getPlayerCurrentPosition(): Int
}