package com.practicum.playlistmarker.di

import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmarker.media_library.data.converters.TrackDbConvertor
import com.practicum.playlistmarker.media_library.data.converters.PlaylistConvertor
import com.practicum.playlistmarker.media_library.data.repository.FavoriteRepositoryImpl
import com.practicum.playlistmarker.media_library.data.repository.playlist.PlaylistRepositoryImpl
import com.practicum.playlistmarker.media_library.domain.db.api.FavoriteRepository
import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistRepository
import com.practicum.playlistmarker.player.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmarker.player.domain.api.PlayerRepository
import com.practicum.playlistmarker.search.data.converters.TrackFavoriteConvertorFromDatabase
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
        TracksRepositoryImpl(get(), get())
    }

    single<SearchHistoryStorage> {
        LocalSearchHistoryStorage(get(), get(), get())
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

    factory { TrackDbConvertor() }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    factory { TrackFavoriteConvertorFromDatabase(get()) }

    factory { PlaylistConvertor(Gson()) }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get(), get())
    }
}