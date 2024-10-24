package com.practicum.playlistmarker.playlist.ui

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import com.practicum.playlistmarker.new_playlist.ui.NewPlaylistFragment
import com.practicum.playlistmarker.player.ui.AudioPlayerFragment
import com.practicum.playlistmarker.playlist.presentation.PlaylistViewModel
import com.practicum.playlistmarker.search.presentation.TrackMapper
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem
import com.practicum.playlistmarker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding
        get() = _binding!!

    private val playlistViewModel by viewModel<PlaylistViewModel>()

    private var trackAdapter: TrackAdapter? = null
    private lateinit var trackList: RecyclerView
    private lateinit var bottomSheetBehaviorPlaylist: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<LinearLayout>

    val playlist by lazy(LazyThreadSafetyMode.NONE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARGS_PLAYLIST, Playlist::class.java)
        } else {
            requireArguments().getParcelable(ARGS_PLAYLIST)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (playlist != null) {
            playlistViewModel.fillDataInPlaylist(playlist!!)
        }
        val screenHeight = resources.displayMetrics.heightPixels
        binding.apply {
            toolbarPlaylist.apply {
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            imageButtonShare.setOnClickListener {
                sharePlaylist()
            }
        }
        val bottomSheetContainerPlaylist = binding.playlistBottomSheet
        bottomSheetBehaviorPlaylist = BottomSheetBehavior.from(bottomSheetContainerPlaylist).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehaviorPlaylist.peekHeight = screenHeight / 3
        playlistViewModel.observePlaylist().observe(viewLifecycleOwner) {
            parsePlaylistInfo(it)
        }
        parseTrackListAdapter()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        val bottomSheetContainerMenu = binding.playlistBottomSheetMenu
        bottomSheetBehaviorMenu = BottomSheetBehavior.from(bottomSheetContainerMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehaviorMenu.peekHeight = screenHeight / 2
        binding.imageButtonSettings.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val overlay = binding.overlay
        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        playlistViewModel.observePlaylist().observe(viewLifecycleOwner) {
                            setPlaylistInfo(it)
                        }
                        binding.textViewPlaylistName
                        binding.playlistShare.setOnClickListener {
                            sharePlaylist()
                        }
                        binding.playlistEditInformation.setOnClickListener {
                            findNavController().navigate(
                                R.id.action_playlistFragment_to_newPlaylistFragment,
                                NewPlaylistFragment.createArgs(playlist!!)
                            )
                        }
                        binding.playlistDeletePlaylist.setOnClickListener {
                            showDialogDeletePlaylist()
                        }
                        overlay.isVisible = true
                    }

                    else -> {
                        overlay.isVisible = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })
    }

    private fun sharePlaylist() {
        if (playlist!!.trackIdList.isEmpty()) {
            Snackbar
                .make(
                    requireView(),
                    getString(R.string.no_tracks_in_the_playlist),
                    Snackbar.LENGTH_LONG
                )
                .show()
        } else {
            playlistViewModel.sharePlaylist()
        }
    }

    private fun setPlaylistInfo(playlist: Playlist?) {
        if (playlist != null) {
            binding.menuPlaylist.namePlaylistItem.text = playlist.playlistName
            val textCounter = requireContext().resources.getQuantityString(
                R.plurals.plurals_track,
                playlist.counterTracks,
                playlist.counterTracks
            )
            binding.menuPlaylist.counterPlaylistItem.text = textCounter
            val filePath = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "myalbum"
            )
            val file = File(filePath, playlist.uri.toString())
            if (playlist.uri != null) {
                binding.menuPlaylist.imagePlaylistItem.setImageURI(file.toUri())
            } else {
                binding.menuPlaylist.imagePlaylistItem.setImageResource(R.drawable.ph_empty)
            }
        }
    }

    private fun parseTrackListAdapter() {
        val trackClickListener = TrackAdapter.TrackClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(it)
            )
        }
        val trackLongClickListener = TrackAdapter.TrackLongClickListener {
            showDialogDeleteTrack(it)
        }
        trackList = binding.recyclerViewPlaylists
        trackList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        trackAdapter = TrackAdapter(mutableListOf(), trackClickListener, trackLongClickListener)
        trackList.adapter = trackAdapter
        playlistViewModel.observeDataInPlaylist().observe(viewLifecycleOwner) { trackList ->
            if (trackList.isEmpty()) {
                Snackbar
                    .make(
                        requireView(),
                        getString(R.string.empty_track_list),
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            } else {
                trackAdapter!!.setUpTracks(
                    tracks = trackList.asReversed().map { track ->
                        TrackMapper.mapToTrackSearchItem(track)
                    }
                )
            }
        }
        trackList.isVisible = true
    }

    private fun parsePlaylistInfo(playlist: Playlist?) {
        binding.apply {
            if (playlist != null) {
                textViewPlaylistName.text = playlist.playlistName
                textViewPlaylistDescription.text = playlist.playlistDescription
                val textCounter = requireContext().resources.getQuantityString(
                    R.plurals.plurals_track,
                    playlist.counterTracks,
                    playlist.counterTracks
                )
                textViewSize.text = textCounter
                val filePath = File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "myalbum"
                )
                val file = File(filePath, playlist.uri.toString())
                if (playlist.uri != null) {
                    imageViewPlaylist.setImageURI(file.toUri())
                } else {
                    imageViewPlaylist.setImageResource(R.drawable.placeholder_empty_image)
                }
                parseLengthInfo()
            }
        }
    }

    private fun parseLengthInfo() {
        playlistViewModel.observeLengthPlaylist().observe(viewLifecycleOwner) {
            val textLengthCounter = SimpleDateFormat(
                "mm",
                Locale.getDefault()
            ).format(it)
            val textLength = requireContext().resources.getQuantityString(
                R.plurals.plurals_track_length,
                textLengthCounter.toInt(),
                textLengthCounter.toInt()
            )
            binding.textViewLength.text = textLength
        }
    }

    private fun showDialogDeleteTrack(track: TrackSearchItem.Track) {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogStyle)
            .setMessage(R.string.do_you_want_delete_track)
            .setNegativeButton(R.string.no) { dialog, which ->

            }
            .setPositiveButton(R.string.yes) { dialog, which ->
                playlistViewModel.deleteTrackFromPlaylist(track.trackId, playlist = playlist!!)
            }
            .show()
    }

    private fun showDialogDeletePlaylist() {
        val strFormatted: String =
            getString(R.string.do_you_want_delete_playlist, playlist!!.playlistName)
        MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogStyle)
            .setMessage(strFormatted)
            .setNegativeButton(R.string.no) { dialog, which ->

            }
            .setPositiveButton(R.string.yes) { dialog, which ->
                playlistViewModel.deletePlaylist(playlist!!)
                findNavController().navigate(R.id.action_playlistFragment_to_mediaLibraryFragment)
            }
            .show()
    }

    override fun onDestroyView() {
        trackAdapter = null
        trackList.adapter = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist_id"

        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(
                ARGS_PLAYLIST to playlist,
            )
    }
}