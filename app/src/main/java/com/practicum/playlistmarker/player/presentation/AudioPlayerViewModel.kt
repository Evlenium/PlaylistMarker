package com.practicum.playlistmarker.player.presentation

import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem
import com.practicum.playlistmarker.util.Creator
import kotlinx.coroutines.flow.Flow

class AudioPlayerViewModel :
    ViewModel() {
    var playerState = StatesPlayer.STATE_DEFAULT
    private lateinit var url: String
    private val mediaPlayer = Creator.playerInteractor
    var mainThreadHandler = Handler(Looper.getMainLooper())
    fun removeTimerPlayer() {
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    fun onDestroy() {
        playerState = StatesPlayer.STATE_DEFAULT
        reset()
        removeTimerPlayer()
        mediaPlayer.onDestroy()
    }

    fun preparePlayer(track: TrackSearchItem.Track?) {
        if (track != null) {
            url = track.previewUrl
            val trackItem = PlayerMapper.mapToTrackForPlayer(track)
            mediaPlayer.preparePlayer(trackItem)
        }
    }

    fun startPlayer() {
        mediaPlayer.startPlayer()
    }

    fun pausePlayer() {
        mediaPlayer.pausePlayer()
    }

    fun playbackControl() {
        mediaPlayer.playbackControl()
    }

    fun getPlayerStateFlow(): Flow<StatesPlayer> {
        return mediaPlayer.getPlayerStateFlow()
    }

    fun getPlayerCurrentPosition(): Int {
        return mediaPlayer.getPlayerCurrentPosition()
    }

    fun reset() {
        mediaPlayer.reset()
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}