package com.practicum.playlistmarker.player.data.repository

import com.practicum.playlistmarker.player.data.Player
import com.practicum.playlistmarker.player.domain.api.PlayerRepository
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl(private val player: Player) : PlayerRepository {
    override fun onPause() {
        player.onPause()
    }

    override fun onDestroy() {
        player.onDestroy()
    }

    override fun preparePlayer(track: TrackDto) {
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

    override fun getPlayerStateFlow(): Flow<StatesPlayer> {
        return player.getPlayerStateFlow()
    }

    override fun getPlayerCurrentPosition(): Int {
        return player.getPlayerCurrentPosition()
    }

    override fun reset() {
        player.reset()
    }
}