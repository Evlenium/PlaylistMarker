package com.practicum.playlistmarker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmarker.di.dataModule
import com.practicum.playlistmarker.di.interactorModule
import com.practicum.playlistmarker.di.repositoryModule
import com.practicum.playlistmarker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    private var darkTheme = false
    override fun onCreate() {
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        instance = this
        val sharedPreferences =
            getSharedPreferences(EDIT_THEME, MODE_PRIVATE)
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

    companion object {
        const val EDIT_THEME = "key_for_edit_theme"
        lateinit var instance: App private set
    }
}