package com.practicum.playlistmarker.settings.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmarker.App
import com.practicum.playlistmarker.settings.data.LocalStorageThemeApp
import com.practicum.playlistmarker.settings.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmarker.settings.domain.use_case.SettingsInteractorImpl
import com.practicum.playlistmarker.sharing.data.ExternalNavigator
import com.practicum.playlistmarker.sharing.data.ResourceProvider
import com.practicum.playlistmarker.sharing.domain.use_case.SharingInteractorImpl

class SettingsViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val externalNavigator by lazy(LazyThreadSafetyMode.NONE) {
        ExternalNavigator(context)
    }
    private val resourceProvider by lazy(LazyThreadSafetyMode.NONE) {
        ResourceProvider(context)
    }
    private val sharingInteractor by lazy(LazyThreadSafetyMode.NONE) {
        SharingInteractorImpl(externalNavigator, resourceProvider)
    }
    private val localStorageThemeApp by lazy(LazyThreadSafetyMode.NONE) {
        LocalStorageThemeApp(
            context.getSharedPreferences(
                App.EDIT_THEME,
                Context.MODE_PRIVATE
            )
        )
    }
    private val settingsRepository by lazy(LazyThreadSafetyMode.NONE) {
        SettingsRepositoryImpl(localStorageThemeApp)
    }
    private val settingsInteractor by lazy(LazyThreadSafetyMode.NONE) {
        SettingsInteractorImpl(settingsRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            sharingInteractor = sharingInteractor,
            settingsInteractor = settingsInteractor
        ) as T
    }
}