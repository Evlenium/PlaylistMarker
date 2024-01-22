package com.practicum.playlistmarker.settings.domain.use_case

import com.practicum.playlistmarker.settings.domain.api.SettingsRepository
import com.practicum.playlistmarker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmarker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}