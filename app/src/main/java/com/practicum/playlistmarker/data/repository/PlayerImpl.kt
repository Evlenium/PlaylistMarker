package com.practicum.playlistmarker.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmarker.data.Player
import com.practicum.playlistmarker.domain.model.States
import com.practicum.playlistmarker.domain.model.TrackSearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerImpl(private val mediaPlayer: MediaPlayer) : Player {
    private val playerState = MutableStateFlow(States.STATE_DEFAULT.state)
    override fun onPause() {
        pausePlayer()
    }

    override fun onDestroy() {
        mediaPlayer.release()
    }

    override fun preparePlayer(track: TrackSearchItem.Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.value = States.STATE_PREPARED.state
            mediaPlayer.seekTo(0)
        }
        mediaPlayer.setOnCompletionListener {
            playerState.value = States.STATE_PREPARED.state
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState.value = States.STATE_PLAYING.state
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState.value = States.STATE_PAUSED.state
    }

    override fun playbackControl() {
        when (playerState.value) {
            States.STATE_PLAYING.state -> {
                pausePlayer()
            }

            States.STATE_PREPARED.state, States.STATE_PAUSED.state -> {
                startPlayer()
            }
        }
    }

    override fun getPlayerStateFlow(): Flow<Int> {
        return playerState
    }

    override fun getPlayerCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}