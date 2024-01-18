package com.practicum.playlistmarker.player.domain.use_case

import com.practicum.playlistmarker.player.domain.api.PlayerInteractor
import com.practicum.playlistmarker.player.domain.api.PlayerRepository
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {
    override fun onPause() {
        repository.onPause()
    }

    override fun onDestroy() {
        repository.onDestroy()
    }

    override fun preparePlayer(track: TrackDto) {
        repository.preparePlayer(track)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun playbackControl() {
        repository.playbackControl()
    }

    override fun getPlayerStateFlow(): Flow<StatesPlayer> {
        return repository.getPlayerStateFlow()
    }

    override fun getPlayerCurrentPosition(): Int {
        return repository.getPlayerCurrentPosition()
    }

    override fun reset() {
        repository.reset()
    }

}