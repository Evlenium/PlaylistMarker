package com.practicum.playlistmarker.di

import com.practicum.playlistmarker.media_library.domain.db.api.FavoriteInteractor
import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistInteractor
import com.practicum.playlistmarker.media_library.domain.db.use_case.FavoriteInteractorImpl
import com.practicum.playlistmarker.media_library.domain.db.use_case.playlist.PlaylistInteractorImpl
import com.practicum.playlistmarker.player.domain.api.PlayerInteractor
import com.practicum.playlistmarker.player.domain.use_case.PlayerInteractorImpl
import com.practicum.playlistmarker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmarker.search.domain.api.TracksInteractor
import com.practicum.playlistmarker.search.domain.use_case.SearchHistoryInteractorImpl
import com.practicum.playlistmarker.search.domain.use_case.TracksInteractorImpl
import com.practicum.playlistmarker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmarker.settings.domain.use_case.SettingsInteractorImpl
import com.practicum.playlistmarker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmarker.sharing.domain.use_case.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}