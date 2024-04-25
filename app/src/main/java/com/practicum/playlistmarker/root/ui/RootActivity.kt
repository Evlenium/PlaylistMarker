package com.practicum.playlistmarker.root.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.ActivityRootBinding
import java.util.Locale


class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioPlayerFragment -> {
                    binding.bottomNavigationView.isVisible = false
                }

                R.id.newPlaylistFragment -> {
                    binding.bottomNavigationView.isVisible = false
                }

                R.id.playlistFragment -> {
                    binding.bottomNavigationView.isVisible = false
                }

                else -> {
                    binding.bottomNavigationView.isVisible = true
                }
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateLocale(base))
        applyOverrideConfiguration(base.resources.configuration)
    }

    private fun updateLocale(context: Context): Context? {
        val ruLocale = Locale("ru")
        Locale.setDefault(ruLocale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(ruLocale)
        configuration.setLayoutDirection(ruLocale)
        return context.createConfigurationContext(configuration)
    }
}