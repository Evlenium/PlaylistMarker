package com.practicum.playlistmarker.domain.use_case

import com.practicum.playlistmarker.domain.api.PlayerInteractor
import com.practicum.playlistmarker.domain.api.PlayerRepository
import com.practicum.playlistmarker.domain.model.TrackSearchItem
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {
    override fun onPause() {
        repository.onPause()
    }

    override fun onDestroy() {
        repository.onDestroy()
    }

    override fun preparePlayer(track: TrackSearchItem.Track) {
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

    override fun getPlayerStateFlow(): Flow<Int> {
        return repository.getPlayerStateFlow()
    }

    override fun getPlayerCurrentPosition(): Int {
        return repository.getPlayerCurrentPosition()
    }

}