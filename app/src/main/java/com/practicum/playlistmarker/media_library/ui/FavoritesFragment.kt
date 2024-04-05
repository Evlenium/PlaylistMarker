package com.practicum.playlistmarker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmarker.media_library.domain.model.FavoriteState
import com.practicum.playlistmarker.media_library.presentation.FavoritesViewModel
import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.player.ui.AudioPlayerFragment
import com.practicum.playlistmarker.search.presentation.TrackMapper
import com.practicum.playlistmarker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding!!
    private val favoritesViewModel by viewModel<FavoritesViewModel>()
    private var adapter: TrackAdapter? = null
    private lateinit var placeholderEmptyListMedia: LinearLayout
    private lateinit var favoritelist: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackClickListener = TrackAdapter.TrackClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(it)
            )
        }
        adapter = TrackAdapter(mutableListOf(), trackClickListener)
        placeholderEmptyListMedia = binding.phEmptyListMedia
        favoritelist = binding.rvTracksMedia
        favoritelist.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favoritelist.adapter = adapter
        favoritesViewModel.fillData()
        favoritesViewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Content -> showContent(state.tracks)
            is FavoriteState.Empty -> showPlaceholder()
        }
    }

    private fun showPlaceholder() {
        favoritelist.isVisible = false
        placeholderEmptyListMedia.isVisible = true
    }

    private fun showContent(tracks: List<Track>) {
        favoritelist.isVisible = true
        placeholderEmptyListMedia.isVisible = false
        val trackListFavorite = tracks.map { track ->
            TrackMapper.mapToTrackSearchWithButton(track)
        }
        adapter?.setUpTracks(tracks = trackListFavorite)
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        favoritelist.adapter = null
        _binding=null
    }

    companion object {
        fun newInstance() = FavoritesFragment().apply {
            arguments = Bundle()
        }
    }
}