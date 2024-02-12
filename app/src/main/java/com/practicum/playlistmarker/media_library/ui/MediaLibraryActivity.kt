package com.practicum.playlistmarker.media_library.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.ActivityMediaLibraryBinding
import com.practicum.playlistmarker.media_library.presentation.MediaLibraryViewModel
import com.practicum.playlistmarker.player.presentation.AudioPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val mediaLibraryViewModel by viewModel<MediaLibraryViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMediaLibrary.setNavigationIcon(R.drawable.bt_arrow_back_mode)
        binding.toolbarMediaLibrary.setNavigationOnClickListener { finish() }
        binding.toolbarMediaLibrary.setTitleTextAppearance(
            this,
            R.style.SecondsActivityMediumTextAppearance
        )

        binding.viewPagerMediaLibrary.adapter = FavoritesTracklistsViewPagerAdapter(
            supportFragmentManager,
            lifecycle
        )

        tabMediator = TabLayoutMediator(
            binding.tlMediaLibrary,
            binding.viewPagerMediaLibrary
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}