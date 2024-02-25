package com.practicum.playlistmarker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.FragmentMediaLibraryBinding
import com.practicum.playlistmarker.media_library.presentation.MediaLibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryFragment : Fragment() {
    private var _binding: FragmentMediaLibraryBinding? = null
    private val binding: FragmentMediaLibraryBinding
        get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator
    private val mediaLibraryViewModel by viewModel<MediaLibraryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPagerMediaLibrary.adapter = FavoritesTracklistsViewPagerAdapter(
            childFragmentManager,
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tabMediator.detach()
    }
}