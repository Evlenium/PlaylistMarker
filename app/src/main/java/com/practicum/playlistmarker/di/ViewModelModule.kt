package com.practicum.playlistmarker.di

import com.practicum.playlistmarker.media_library.presentation.FavoritesViewModel
import com.practicum.playlistmarker.media_library.presentation.MediaLibraryViewModel
import com.practicum.playlistmarker.new_playlist.presentation.NewPlaylistViewModel
import com.practicum.playlistmarker.media_library.presentation.PlaylistsViewModel
import com.practicum.playlistmarker.player.presentation.AudioPlayerViewModel
import com.practicum.playlistmarker.playlist.presentation.PlaylistViewModel
import com.practicum.playlistmarker.search.presentation.TracksSearchViewModel
import com.practicum.playlistmarker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        TracksSearchViewModel(get(), get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get(), get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        MediaLibraryViewModel()
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get(), get())
    }
}