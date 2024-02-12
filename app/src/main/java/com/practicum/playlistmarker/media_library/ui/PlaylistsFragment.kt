package com.practicum.playlistmarker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmarker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmarker.media_library.presentation.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistsBinding
    private val playlistsViewModel by viewModel<PlaylistsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.phFragmentPlaylistsEmpty
        binding.nothingCreated
    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle()
        }
    }
}