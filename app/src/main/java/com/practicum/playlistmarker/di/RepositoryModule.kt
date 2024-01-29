package com.practicum.playlistmarker.di

import android.media.MediaPlayer
import com.practicum.playlistmarker.player.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmarker.player.domain.api.PlayerRepository
import com.practicum.playlistmarker.search.data.repository.LocalSearchHistoryStorage
import com.practicum.playlistmarker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmarker.search.domain.api.SearchHistoryStorage
import com.practicum.playlistmarker.search.domain.api.TracksRepository
import com.practicum.playlistmarker.settings.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmarker.settings.domain.api.SettingsRepository
import com.practicum.playlistmarker.sharing.data.ExternalNavigator
import com.practicum.playlistmarker.sharing.data.ResourceProvider
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<SearchHistoryStorage> {
        LocalSearchHistoryStorage(get(), get())
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl(MediaPlayer())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigator(get())
    }

    single<ResourceProvider> {
        ResourceProvider(get())
    }
}