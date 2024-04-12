package com.practicum.playlistmarker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import com.practicum.playlistmarker.media_library.domain.model.playlist.PlaylistState
import com.practicum.playlistmarker.media_library.presentation.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding
        get() = _binding!!
    private val playlistsViewModel by viewModel<PlaylistsViewModel>()
    private var adapter: PlaylistAdapter? = null
    private lateinit var playlistlist: RecyclerView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PlaylistAdapter(emptyList(),requireContext())
        playlistlist = binding.recyclerViewPlaylists
        playlistlist.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistlist.adapter = adapter
        playlistsViewModel.fillDataPlaylists()
        playlistsViewModel.observePlaylistState().observe(viewLifecycleOwner) {
            render(it)
        }

        placeholderImage = binding.phFragmentPlaylistsEmpty
        placeholderText = binding.nothingCreated
        binding.btNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_playlistFragment)
        }

    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.playlists)
            is PlaylistState.Empty -> showPlaceholder()
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        placeholderImage.isVisible = false
        placeholderText.isVisible = false
        playlistlist.isVisible = true
        adapter?.setUpPlaylists(playlists)
    }

    private fun showPlaceholder() {
        playlistlist.isVisible = false
        placeholderImage.isVisible = true
        placeholderText.isVisible = true
    }

    override fun onDestroyView() {
        adapter = null
        playlistlist.adapter = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle()
        }
    }
}