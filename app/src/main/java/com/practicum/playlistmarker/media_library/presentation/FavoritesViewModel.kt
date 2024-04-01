package com.practicum.playlistmarker.media_library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarker.media_library.domain.db.api.FavoriteInteractor
import com.practicum.playlistmarker.media_library.domain.model.FavoriteState
import com.practicum.playlistmarker.player.domain.model.Track
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {
    private val favoriteStateLiveData = MutableLiveData<FavoriteState>()
    fun observeFavoriteState(): LiveData<FavoriteState> = favoriteStateLiveData

    fun fillData() {
        viewModelScope.launch {
            favoriteInteractor.getFavoriteTracks().collect { trackList ->
                processResult(trackList)
            }
        }
    }

    private fun processResult(trackList: List<Track>) {
        if (trackList.isNotEmpty()) {
            renderState(FavoriteState.Content(trackList))
        }
        else{
            renderState(FavoriteState.Empty)
        }
    }

    private fun renderState(state: FavoriteState) {
        favoriteStateLiveData.postValue(state)
    }
}