package com.practicum.playlistmarker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarker.player.domain.api.PlayerInteractor
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AudioPlayerViewModel(private val playerInteractor: PlayerInteractor) :
    ViewModel() {
    var playerState = StatesPlayer.STATE_DEFAULT
    private lateinit var url: String
    private var timerJob: Job? = null

    private val positionLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun observePosition(): LiveData<Int> {
        timerJob = viewModelScope.launch {
            while (playerState == StatesPlayer.STATE_PLAYING) {
                delay(DELAY_UPDATE)
                positionLiveData.value = getPlayerCurrentPosition()
            }
        }
        return positionLiveData
    }

    fun preparePlayer(track: TrackSearchItem.Track?) {
        if (track != null) {
            url = track.previewUrl
            val trackItem = PlayerMapper.mapToTrackForPlayer(track)
            playerInteractor.preparePlayer(trackItem)
        }
    }

    fun startPlayer() {
        playerInteractor.startPlayer()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        timerJob?.cancel()
    }

    fun playbackControl() {
        playerInteractor.playbackControl()
    }

    fun getPlayerStateFlow(): Flow<StatesPlayer> {
        return playerInteractor.getPlayerStateFlow()
    }

    private fun getPlayerCurrentPosition(): Int {
        return playerInteractor.getPlayerCurrentPosition()
    }

    fun reset() {
        playerInteractor.reset()
    }

    companion object {
        private const val DELAY_UPDATE = 300L
    }
}