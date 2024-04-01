package com.practicum.playlistmarker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarker.media_library.domain.db.api.FavoriteInteractor
import com.practicum.playlistmarker.player.domain.api.PlayerInteractor
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.search.presentation.model.StateFavorite
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
) :
    ViewModel() {
    var playerState = StatesPlayer.STATE_DEFAULT
    private val stateFavoriteLiveData = MutableLiveData<StateFavorite>()

    private lateinit var url: String
    private var timerJob: Job? = null

    fun observeFavoriteState(): LiveData<StateFavorite> = stateFavoriteLiveData

    private val positionLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun observePosition(): LiveData<Int> {
        timerJob = viewModelScope.launch {
            while (playerState == StatesPlayer.STATE_PLAYING) {
                delay(DELAY_UPDATE_MILLIS)
                positionLiveData.value = getPlayerCurrentPosition()
            }
        }
        return positionLiveData
    }

    fun preparePlayer(track: TrackSearchItem.Track?) {
        if (track != null) {
            if (track.previewUrl != null) {
                url = track.previewUrl
                val trackItem = PlayerMapper.mapToTrackForPlayer(track)
                playerInteractor.preparePlayer(trackItem)
            }
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

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteInteractor.deleteTrack(track)
                renderState(StateFavorite.NotFavorite)
            } else {
                favoriteInteractor.addTrack(track)
                renderState(StateFavorite.Favorite)
            }
        }
    }

    private fun renderState(state: StateFavorite) {
        stateFavoriteLiveData.postValue(state)
    }

    companion object {
        private const val DELAY_UPDATE_MILLIS = 300L
    }
}