package com.practicum.playlistmarker.settings.domain.api

import com.practicum.playlistmarker.settings.domain.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}