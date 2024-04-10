package com.practicum.playlistmarker.player.ui

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.player.domain.model.TrackPlaylistState
import com.practicum.playlistmarker.player.presentation.AudioPlayerViewModel
import com.practicum.playlistmarker.search.presentation.TrackMapper.mapToTrack
import com.practicum.playlistmarker.search.presentation.model.StateFavorite
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding: FragmentAudioPlayerBinding
        get() = _binding!!

    private val audioPlayerViewModel by viewModel<AudioPlayerViewModel>()

    private lateinit var buttonLike: ImageButton

    private var adapter: PlaylistButtomAdapter? = null
    private lateinit var playlistList: RecyclerView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    val track by lazy(LazyThreadSafetyMode.NONE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARGS_TRACK_ID, TrackSearchItem.Track::class.java)
        } else {
            requireArguments().getParcelable(ARGS_TRACK_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarAudioPlayer.apply {
            setNavigationIcon(R.drawable.bt_arrow_back_mode)
            setNavigationOnClickListener { findNavController().popBackStack() }
            setTitleTextAppearance(
                requireContext(),
                R.style.SecondsActivityMediumTextAppearance
            )
        }
        val bottomSheetContainer = binding.playlistBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        audioPlayerViewModel.observeTrackInPlaylistState().observe(viewLifecycleOwner) {
            checkPlayerInPlaylistState(it)
        }

        val playlistClickListener = PlaylistButtomAdapter.PlaylistClickListener { playlist ->
            if (track != null) {
                audioPlayerViewModel.addTrackToPlaylist(playlist, mapToTrack(track!!))
            }
        }
        adapter = PlaylistButtomAdapter(emptyList(), playlistClickListener)
        playlistList = binding.recyclerViewPlaylists
        playlistList.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        playlistList.adapter = adapter
        binding.btAddPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val overlay = binding.overlay
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.buttonNewPlaylist.setOnClickListener {
                            findNavController().navigate(
                                R.id.action_audioPlayerFragment_to_playlistFragment
                            )
                        }
                        audioPlayerViewModel.fillDataPlaylists()
                        audioPlayerViewModel.observePlaylistState().observe(viewLifecycleOwner) {
                            adapter?.setUpPlaylists(it)
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
        observeLengthComposition()
        parseTrackInfo(track)
    }

    private fun checkPlayerInPlaylistState(trackPlaylistState: TrackPlaylistState) {
        when (trackPlaylistState) {
            is TrackPlaylistState.NotInPlaylist -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                Snackbar
                    .make(
                        requireView(),
                        "${trackPlaylistState.message} ${trackPlaylistState.name}",
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }

            is TrackPlaylistState.InPlaylist -> {
                Snackbar
                    .make(
                        requireView(),
                        "${trackPlaylistState.message} ${trackPlaylistState.name}",
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }
        }
    }

    private fun parseTrackInfo(track: TrackSearchItem.Track?) {
        binding.apply {
            buttonLike = btLike
            if (track != null) {
                Glide.with(ivTrackImage)
                    .load(track.getResizeUrlArtwork())
                    .placeholder(R.drawable.empty_track_image)
                    .transform(RoundedCorners(dpToPx(8)))
                    .into(ivTrackImage)
                tvTrackName.text = track.trackName
                tvNameArtist.text = track.artistName
                tvAlbum.text = track.collectionName
                if (track.releaseDate != null) {
                    tvYear.text = track.dateYearFormat
                } else {
                    tvYear.text = ""
                }
                tvGenre.text = track.primaryGenreName
                tvCountry.text = track.country
                tvLength.text = track.trackTimeFormat
            }
            btPlay.setOnClickListener {
                audioPlayerViewModel.playbackControl()
            }
            viewLifecycleOwner.lifecycleScope.launch {
                audioPlayerViewModel.getPlayerStateFlow().collect { playerState ->
                    audioPlayerViewModel.playerState = playerState
                    checkState(playerState)
                }
            }
        }
        if (track?.isFavorite == true) {
            buttonLike.setImageResource(R.drawable.bt_red_like)
        } else buttonLike.setImageResource(R.drawable.bt_like)

        audioPlayerViewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            renderFavoriteState(it)
        }
        buttonLike.setOnClickListener {
            val trackTmp = mapToTrack(track!!)
            audioPlayerViewModel.onFavoriteClicked(trackTmp)
        }
    }

    private fun observeLengthComposition() {
        audioPlayerViewModel.observePosition().observe(viewLifecycleOwner) {
            if (audioPlayerViewModel.playerState == StatesPlayer.STATE_PLAYING) {
                binding.tvPrelength.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(it)
                Log.d("MyLog", it.toString())
            }
        }
    }

    private fun checkState(playerState: StatesPlayer) {
        when (playerState) {
            StatesPlayer.STATE_PLAYING -> {
                observeLengthComposition()
                audioPlayerViewModel.startPlayer()
                binding.btPlay.setImageResource(R.drawable.bt_paused)
            }

            StatesPlayer.STATE_PREPARED -> {
                binding.btPlay.setImageResource(R.drawable.bt_play)
                binding.tvPrelength.text = resources.getString(R.string.fixed_time_track)
            }

            StatesPlayer.STATE_PAUSED -> {
                audioPlayerViewModel.pausePlayer()
                binding.btPlay.setImageResource(R.drawable.bt_play)
            }

            StatesPlayer.STATE_DEFAULT -> {
                binding.tvPrelength.text = resources.getString(R.string.fixed_time_track)
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun renderFavoriteState(state: StateFavorite) {
        when (state) {
            is StateFavorite.Favorite -> {
                buttonLike.setImageResource(R.drawable.bt_red_like)
                track?.isFavorite = true
            }

            is StateFavorite.NotFavorite -> {
                buttonLike.setImageResource(R.drawable.bt_like)
                track?.isFavorite = false
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        audioPlayerViewModel.reset()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        audioPlayerViewModel.preparePlayer(track)
    }

    companion object {
        private const val ARGS_TRACK_ID = "track_id"

        fun createArgs(trackId: TrackSearchItem.Track): Bundle =
            bundleOf(
                ARGS_TRACK_ID to trackId,
            )
    }
}