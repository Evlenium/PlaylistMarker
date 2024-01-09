package com.practicum.playlistmarker.domain.api

import com.practicum.playlistmarker.domain.model.TrackSearchItem
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun onPause()
    fun onDestroy()
    fun preparePlayer(track: TrackSearchItem.Track)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun getPlayerStateFlow(): Flow<Int>
    fun getPlayerCurrentPosition(): Int
}