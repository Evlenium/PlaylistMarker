package com.practicum.playlistmarker.settings.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var themeSwitcher: SwitchCompat
    private val settingsViewModel by viewModel<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbarSearch = findViewById<Toolbar>(R.id.toolbarSettings)
        toolbarSearch.setNavigationIcon(R.drawable.bt_arrow_back_mode)
        toolbarSearch.setNavigationOnClickListener { finish() }
        toolbarSearch.setTitleTextAppearance(this, R.style.SecondsActivityMediumTextAppearance)

        val buttonShareApplication = findViewById<ImageButton>(R.id.buttonShareApplication)
        buttonShareApplication.setOnClickListener {
            settingsViewModel.shareApp()
        }

        val buttonSupport = findViewById<ImageButton>(R.id.buttonSupport)
        buttonSupport.setOnClickListener {
            settingsViewModel.openSupport()
        }

        val buttonTermsOfUse = findViewById<ImageButton>(R.id.buttonTermsOfUse)
        buttonTermsOfUse.setOnClickListener {
            settingsViewModel.openTerms()
        }

        themeSwitcher = findViewById(R.id.swTheme)
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            onThemeChanged(checked)
        }
        observeText()
    }

    private fun onThemeChanged(isDark: Boolean) {
        settingsViewModel.onThemeChanged(isDark)
    }

    private fun observeText() {
        settingsViewModel.observeThemeLiveData().observe(this) {
            themeSwitcher.isChecked = it
        }
    }
}