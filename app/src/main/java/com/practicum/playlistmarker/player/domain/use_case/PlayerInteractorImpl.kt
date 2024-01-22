package com.practicum.playlistmarker.player.domain.use_case

import com.practicum.playlistmarker.player.domain.api.Player
import com.practicum.playlistmarker.player.domain.api.PlayerInteractor
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(private val player: Player) : PlayerInteractor {
    override fun onPause() {
        player.onPause()
    }

    override fun onDestroy() {
        player.onDestroy()
    }

    override fun preparePlayer(track: Track) {
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