package com.practicum.playlistmarker.creator

import android.media.MediaPlayer
import com.practicum.playlistmarker.data.repository.PlayerImpl
import com.practicum.playlistmarker.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmarker.domain.api.PlayerInteractor
import com.practicum.playlistmarker.domain.api.PlayerRepository
import com.practicum.playlistmarker.domain.use_case.PlayerInteractorImpl

object Creator {
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(PlayerImpl(MediaPlayer()))
    }
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}