package com.practicum.playlistmarker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmarker.App
import com.practicum.playlistmarker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmarker.settings.domain.model.ThemeSettings
import com.practicum.playlistmarker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    private val themeLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    fun observeThemeLiveData(): LiveData<Boolean> {
        themeLiveData.value = getThemeSettings()
        return themeLiveData
    }

    fun onThemeChanged(isDark: Boolean) {
        updateThemeSetting(isDark)
    }

    private fun getThemeSettings(): Boolean {
        return settingsInteractor.getThemeSettings().isDark
    }

    private fun updateThemeSetting(isDark: Boolean) {
        App.instance.switchTheme(isDark)
        settingsInteractor.updateThemeSetting(ThemeSettings(isDark))
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }
}