package com.practicum.playlistmarker.player.domain.api

import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun onPause()
    fun onDestroy()
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun getPlayerStateFlow(): Flow<StatesPlayer>
    fun getPlayerCurrentPosition(): Int
    fun reset()
}