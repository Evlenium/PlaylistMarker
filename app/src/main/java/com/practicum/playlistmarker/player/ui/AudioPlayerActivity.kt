package com.practicum.playlistmarker.player.ui

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmarker.App
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.player.domain.model.StatesPlayer
import com.practicum.playlistmarker.player.presentation.AudioPlayerViewModel
import com.practicum.playlistmarker.player.presentation.AudioPlayerViewModelFactory
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var btPlay: ImageButton
    private lateinit var tvPreLength: TextView
    private lateinit var tvTrackName: TextView
    private lateinit var tvNameArtist: TextView
    private lateinit var tvAlbum: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvGenre: TextView
    private lateinit var tvCountry: TextView
    private lateinit var tvLength: TextView
    private lateinit var ivTrackImage: ImageView

    private lateinit var audioPlayerViewModel: AudioPlayerViewModel

    val track by lazy(LazyThreadSafetyMode.NONE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(App.TRACK, TrackSearchItem.Track::class.java)
        } else {
            intent.getParcelableExtra(App.TRACK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        audioPlayerViewModel =
            ViewModelProvider(this, AudioPlayerViewModelFactory())[AudioPlayerViewModel::class.java]

        val toolbarMediaLibraryActivity = findViewById<Toolbar>(R.id.toolbarMediaLibraryActivity)
        toolbarMediaLibraryActivity.setNavigationIcon(R.drawable.bt_arrow_back_mode)
        toolbarMediaLibraryActivity.setNavigationOnClickListener { finish() }
        toolbarMediaLibraryActivity.setTitleTextAppearance(
            this,
            R.style.SecondsActivityMediumTextAppearance
        )

        ivTrackImage = findViewById(R.id.ivTrackImage)

        btPlay = findViewById(R.id.btPlay)

        tvTrackName = findViewById(R.id.tvTrackName)
        tvNameArtist = findViewById(R.id.tvNameArtist)
        tvAlbum = findViewById(R.id.tvAlbum)
        tvYear = findViewById(R.id.tvYear)
        tvGenre = findViewById(R.id.tvGenre)
        tvCountry = findViewById(R.id.tvCountry)
        tvLength = findViewById(R.id.tvLength)

        tvPreLength = findViewById(R.id.tvPrelength)
        observeLengthComposition()
        parseTrackInfo(track)
    }

    private fun parseTrackInfo(track: TrackSearchItem.Track?) {
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
                this@AudioPlayerActivity.audioPlayerViewModel.playerState = playerState
                audioPlayerViewModel.mainThreadHandler.post { checkState(playerState) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        audioPlayerViewModel.preparePlayer(track)
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerViewModel.reset()
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (audioPlayerViewModel.playerState == StatesPlayer.STATE_PLAYING) {
                    observeLengthComposition()
                    audioPlayerViewModel.mainThreadHandler.postDelayed(this, DELAY_UPDATE)
                }
            }
        }
    }

    fun observeLengthComposition() {
        audioPlayerViewModel.observePosition().observe(this) {
            tvPreLength.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(it)
            Log.d("MyLog", it.toString())
        }
    }

    private fun checkState(playerState: StatesPlayer) {
        when (playerState) {
            StatesPlayer.STATE_PLAYING -> {
                audioPlayerViewModel.mainThreadHandler.post(createUpdateTimerTask())
                audioPlayerViewModel.startPlayer()
                btPlay.setImageResource(R.drawable.bt_paused)
            }

            StatesPlayer.STATE_PREPARED -> {
                btPlay.setImageResource(R.drawable.bt_play)
                audioPlayerViewModel.removeTimerPlayer()
                tvPreLength.text = resources.getString(R.string.fixed_time_track)
            }

            StatesPlayer.STATE_PAUSED -> {
                audioPlayerViewModel.pausePlayer()
                btPlay.setImageResource(R.drawable.bt_play)
            }

            StatesPlayer.STATE_DEFAULT -> {

            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    companion object {
        private const val DELAY_UPDATE = 300L
    }
}