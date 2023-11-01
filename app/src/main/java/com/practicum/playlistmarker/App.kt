package com.practicum.playlistmarker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App:Application() {
    var darkTheme = false
    override fun onCreate() {
        val sharedPreferences =
            getSharedPreferences(PRACTICUM_PLAYLISTMARKER_PREFERENCES, MODE_PRIVATE)
        (applicationContext as App).switchTheme(
            sharedPreferences.getBoolean(
                EDIT_THEME,
                (applicationContext as App).darkTheme
            )
        )
        super.onCreate()
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    companion object{
        const val PRACTICUM_PLAYLISTMARKER_PREFERENCES = "practicum_theme_preferences"
        const val EDIT_THEME = "key_for_edit_theme"
        const val TRACKS_LIST_KEY = "key_for_tracks_list"
    }
}