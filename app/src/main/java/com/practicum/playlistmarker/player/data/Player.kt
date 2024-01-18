package com.practicum.playlistmarker.player.data

import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface Player {
    fun onPause()
    fun onDestroy()
    fun preparePlayer(track: TrackDto)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun getPlayerStateFlow(): Flow<StatesPlayer>
    fun getPlayerCurrentPosition(): Int
    fun reset()
}