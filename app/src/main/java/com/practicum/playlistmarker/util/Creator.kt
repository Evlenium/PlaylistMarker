package com.practicum.playlistmarker.util

import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmarker.App
import com.practicum.playlistmarker.player.data.repository.PlayerImpl
import com.practicum.playlistmarker.player.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmarker.player.domain.use_case.PlayerInteractorImpl
import com.practicum.playlistmarker.search.data.LocalStorageTrackHistory
import com.practicum.playlistmarker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmarker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmarker.search.domain.api.TracksInteractor
import com.practicum.playlistmarker.search.domain.api.TracksRepository
import com.practicum.playlistmarker.search.domain.use_case.TracksInteractorImpl

object Creator {
    private val player by lazy(LazyThreadSafetyMode.NONE) {
        PlayerImpl(MediaPlayer())
    }
    private val playerRepository by lazy(LazyThreadSafetyMode.NONE) {
        PlayerRepositoryImpl(player)
    }
    val playerInteractor by lazy(LazyThreadSafetyMode.NONE) {
        PlayerInteractorImpl(playerRepository)
    }

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(context),
            LocalStorageTrackHistory(
                context.getSharedPreferences(
                    App.TRACKS_LIST_KEY,
                    Context.MODE_PRIVATE
                )
            ),
        )
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }
}