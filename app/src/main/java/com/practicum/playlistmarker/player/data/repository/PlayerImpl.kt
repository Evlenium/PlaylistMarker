package com.practicum.playlistmarker.player.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmarker.player.data.Player
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerImpl(private val mediaPlayer: MediaPlayer) : Player {
    private val playerState = MutableStateFlow(StatesPlayer.STATE_DEFAULT)
    override fun onPause() {
        pausePlayer()
    }

    override fun onDestroy() {
        mediaPlayer.release()
    }

    override fun preparePlayer(track: TrackDto) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.value = StatesPlayer.STATE_PREPARED
            mediaPlayer.seekTo(0)
        }
        mediaPlayer.setOnCompletionListener {
            playerState.value = StatesPlayer.STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState.value = StatesPlayer.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState.value = StatesPlayer.STATE_PAUSED
    }

    override fun playbackControl() {
        when (playerState.value) {
            StatesPlayer.STATE_PLAYING -> {
                pausePlayer()
            }

            StatesPlayer.STATE_PREPARED, StatesPlayer.STATE_PAUSED -> {
                startPlayer()
            }

            StatesPlayer.STATE_DEFAULT -> {}
        }
    }

    override fun getPlayerStateFlow(): Flow<StatesPlayer> {
        return playerState
    }

    override fun getPlayerCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun reset() {
        mediaPlayer.reset()
    }
}