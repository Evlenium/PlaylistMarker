package com.practicum.playlistmarker.player.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmarker.player.domain.api.PlayerInteractor
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem
import kotlinx.coroutines.flow.Flow

class AudioPlayerViewModel(private val playerInteractor: PlayerInteractor) :
    ViewModel() {
    var playerState = StatesPlayer.STATE_DEFAULT
    private lateinit var url: String
    var mainThreadHandler = Handler(Looper.getMainLooper())

    private val positionLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun observePosition(): LiveData<Int> {
        positionLiveData.value = getPlayerCurrentPosition()
        return positionLiveData
    }

    fun removeTimerPlayer() {
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    fun onDestroy() {
        playerState = StatesPlayer.STATE_DEFAULT
        reset()
        removeTimerPlayer()
        playerInteractor.onDestroy()
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
}