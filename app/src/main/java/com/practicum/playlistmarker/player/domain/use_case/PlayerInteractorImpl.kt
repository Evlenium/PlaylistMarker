package com.practicum.playlistmarker.player.domain.use_case

import com.practicum.playlistmarker.player.domain.api.PlayerInteractor
import com.practicum.playlistmarker.player.domain.api.PlayerRepository
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {
    override fun onPause() {
        playerRepository.onPause()
    }

    override fun onDestroy() {
        playerRepository.onDestroy()
    }

    override fun preparePlayer(track: Track) {
        playerRepository.preparePlayer(track)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun playbackControl() {
        playerRepository.playbackControl()
    }

    override fun getPlayerStateFlow(): Flow<StatesPlayer> {
        return playerRepository.getPlayerStateFlow()
    }

    override fun getPlayerCurrentPosition(): Int {
        return playerRepository.getPlayerCurrentPosition()
    }

    override fun reset() {
        playerRepository.reset()
    }

}