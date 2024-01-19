package com.practicum.playlistmarker.settings.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmarker.util.Creator

class SettingsViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            sharingInteractor = Creator.getSharingInteractor(context = context),
            settingsInteractor = Creator.getSettingsInteractor(context = context)
        ) as T
    }
}