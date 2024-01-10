package com.practicum.playlistmarker.data.repository

import com.practicum.playlistmarker.data.Player
import com.practicum.playlistmarker.domain.api.PlayerRepository
import com.practicum.playlistmarker.domain.model.TrackSearchItem
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl(private val player: Player) : PlayerRepository {
    override fun onPause() {
        player.onPause()
    }

    override fun onDestroy() {
        player.onDestroy()
    }

    override fun preparePlayer(track: TrackSearchItem.Track) {
        player.preparePlayer(track)
    }

    override fun startPlayer() {
        player.startPlayer()
    }

    override fun pausePlayer() {
        player.pausePlayer()
    }

    override fun playbackControl() {
        player.playbackControl()
    }

    override fun getPlayerStateFlow(): Flow<Int> {
        return player.getPlayerStateFlow()
    }

    override fun getPlayerCurrentPosition(): Int {
        return player.getPlayerCurrentPosition()
    }
}