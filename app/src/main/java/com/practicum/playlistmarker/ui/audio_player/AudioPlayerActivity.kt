package com.practicum.playlistmarker.ui.audio_player

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmarker.App
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.creator.Creator
import com.practicum.playlistmarker.domain.model.States
import com.practicum.playlistmarker.domain.model.TrackSearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var btPlay: ImageButton
    private lateinit var tvPreLength: TextView
    private val mediaPlayer = Creator.providePlayerInteractor()
    private var playerState = States.STATE_DEFAULT.state

    private lateinit var url: String
    private var mainThreadHandler: Handler? = null

    private lateinit var tvTrackName: TextView
    private lateinit var tvNameArtist: TextView
    private lateinit var tvAlbum: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvGenre: TextView
    private lateinit var tvCountry: TextView
    private lateinit var tvLength: TextView

    private lateinit var ivTrackImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        mainThreadHandler = Handler(Looper.getMainLooper())

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
        tvPreLength.text =
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.getPlayerCurrentPosition())
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(App.TRACK, TrackSearchItem.Track::class.java)
        } else {
            intent.getParcelableExtra(App.TRACK)
        }
        parseTrackInfo(track)
    }

    private fun parseTrackInfo(track: TrackSearchItem.Track?) {
        if (track != null) {
            url = track.previewUrl
            mediaPlayer.preparePlayer(track)
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
            mediaPlayer.playbackControl()
        }
        lifecycleScope.launch(Dispatchers.Main) {
            mediaPlayer.getPlayerStateFlow().collect { playerState ->
                this@AudioPlayerActivity.playerState = playerState
                mainThreadHandler?.post { checkState(playerState) }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.onPause()
        removeTimerPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.onDestroy()
        removeTimerPlayer()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == States.STATE_PLAYING.state) {
                    tvPreLength.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.getPlayerCurrentPosition())
                    Log.d("MyLog", mediaPlayer.getPlayerCurrentPosition().toString())
                    mainThreadHandler?.postDelayed(this, DELAY_UPDATE)
                }
            }
        }
    }

    private fun removeTimerPlayer() {
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    private fun checkState(playerState: Int) {
        when (playerState) {
            States.STATE_PLAYING.state -> {
                mainThreadHandler?.post(createUpdateTimerTask())
                btPlay.setImageResource(R.drawable.bt_paused)
            }

            States.STATE_PREPARED.state -> {
                btPlay.setImageResource(R.drawable.bt_play)
                removeTimerPlayer()
                tvPreLength.text = resources.getString(R.string.fixed_time_track)
            }

            States.STATE_PAUSED.state -> {
                btPlay.setImageResource(R.drawable.bt_play)
            }
        }
    }

    companion object {
        private const val DELAY_UPDATE = 300L
    }
}