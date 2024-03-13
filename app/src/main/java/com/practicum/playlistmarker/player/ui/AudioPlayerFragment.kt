package com.practicum.playlistmarker.player.ui

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.player.presentation.AudioPlayerViewModel
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
        observeLengthComposition()
        parseTrackInfo(track)
    }

    private fun parseTrackInfo(track: TrackSearchItem.Track?) {
        binding.apply {
            if (track != null) {
                Glide.with(ivTrackImage)
                    .load(track.getResizeUrlArtwork())
                    .placeholder(R.drawable.empty_track_image)
                    .transform(RoundedCorners(dpToPx(8)))
                    .into(ivTrackImage)
                tvTrackName.text = track.trackName
                tvNameArtist.text = track.artistName
                tvAlbum.text = track.collectionName
                tvYear.text = track.dateYearFormat
                tvGenre.text = track.primaryGenreName
                tvCountry.text = track.country
                tvLength.text = track.trackTimeFormat
            }
            btPlay.setOnClickListener {
                audioPlayerViewModel.playbackControl()
            }
            lifecycleScope.launch(Dispatchers.Main) {
                audioPlayerViewModel.getPlayerStateFlow().collect { playerState ->
                    audioPlayerViewModel.playerState = playerState
                    checkState(playerState)
                }
            }
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