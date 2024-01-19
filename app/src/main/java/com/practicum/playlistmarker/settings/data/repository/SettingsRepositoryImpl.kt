package com.practicum.playlistmarker.settings.data.repository

import com.practicum.playlistmarker.settings.data.LocalStorageThemeApp
import com.practicum.playlistmarker.settings.domain.api.SettingsRepository
import com.practicum.playlistmarker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val localStorageThemeApp: LocalStorageThemeApp):
    SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return localStorageThemeApp.getThemeSettings()
    }
    override fun updateThemeSetting(settings: ThemeSettings){
        localStorageThemeApp.updateThemeSetting(settings)
    }
}