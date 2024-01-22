package com.practicum.playlistmarker.util

import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmarker.App
import com.practicum.playlistmarker.player.data.repository.PlayerImpl
import com.practicum.playlistmarker.player.domain.api.Player
import com.practicum.playlistmarker.player.domain.api.PlayerInteractor
import com.practicum.playlistmarker.player.domain.use_case.PlayerInteractorImpl
import com.practicum.playlistmarker.search.data.LocalStorageTrackHistory
import com.practicum.playlistmarker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmarker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmarker.search.domain.api.TracksInteractor
import com.practicum.playlistmarker.search.domain.api.TracksRepository
import com.practicum.playlistmarker.search.domain.use_case.TracksInteractorImpl
import com.practicum.playlistmarker.settings.data.LocalStorageThemeApp
import com.practicum.playlistmarker.settings.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmarker.settings.domain.api.SettingsRepository
import com.practicum.playlistmarker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmarker.settings.domain.use_case.SettingsInteractorImpl
import com.practicum.playlistmarker.sharing.data.ExternalNavigator
import com.practicum.playlistmarker.sharing.data.ResourceProvider
import com.practicum.playlistmarker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmarker.sharing.domain.use_case.SharingInteractorImpl

object Creator {
    private fun getPlayer(): Player {
        return PlayerImpl(MediaPlayer())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayer())
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

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigator(context)
    }

    private fun getResourceProvider(context: Context): ResourceProvider {
        return ResourceProvider(context)
    }

    fun getSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context), getResourceProvider(context))
    }

    private fun getLocalStorageThemeApp(context: Context): LocalStorageThemeApp {
        return LocalStorageThemeApp(
            context.getSharedPreferences(
                App.EDIT_THEME,
                Context.MODE_PRIVATE
            )
        )
    }

    private fun getSettingsRepositoryImpl(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getLocalStorageThemeApp(context))
    }

    fun getSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepositoryImpl(context))
    }
}