package com.practicum.playlistmarker.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmarker.util.Creator

class AudioPlayerViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioPlayerViewModel(
            playerInteractor = Creator.providePlayerInteractor()
        ) as T
    }
}
