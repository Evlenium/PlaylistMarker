package com.practicum.playlistmarker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmarker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmarker.media_library.presentation.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val favoritesViewModel by viewModel<FavoritesViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.phFragmentPlaylistsEmpty
        binding.tvMediaLibraryEmpty
    }

    companion object {
        fun newInstance() = FavoritesFragment().apply {
            arguments = Bundle()
        }
    }
}