package com.practicum.playlistmarker.settings.data

import android.content.SharedPreferences
import com.practicum.playlistmarker.settings.domain.model.ThemeSettings

class LocalStorageThemeApp(private val sharedPreferences: SharedPreferences) {
    private companion object {
        const val EDIT_THEME = "key_for_edit_theme"
    }
    private var isDark:Boolean = false
    fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(sharedPreferences.getBoolean(EDIT_THEME,isDark))
    }
    fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(EDIT_THEME,settings.isDark).apply()
    }
}