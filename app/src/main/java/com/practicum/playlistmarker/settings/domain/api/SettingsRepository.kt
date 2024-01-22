package com.practicum.playlistmarker.settings.domain.api

import com.practicum.playlistmarker.settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}