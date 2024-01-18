package com.practicum.playlistmarker.settings.domain.use_case

import com.practicum.playlistmarker.settings.domain.api.SettingRepository
import com.practicum.playlistmarker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmarker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(private val settingRepository: SettingRepository) :
    SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingRepository.updateThemeSetting(settings)
    }
}