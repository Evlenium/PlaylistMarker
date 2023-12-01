package com.practicum.playlistmarker

import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var btPlay: ImageButton
    private lateinit var tvPreLength: TextView
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var url: String
    private var mainThreadHandler: Handler? = null

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

        val ivTrackImage = findViewById<ImageView>(R.id.ivTrackImage)

        btPlay = findViewById(R.id.btPlay)

        val tvTrackName = findViewById<TextView>(R.id.tvTrackName)
        val tvNameArtist = findViewById<TextView>(R.id.tvNameArtist)
        val tvAlbum = findViewById<TextView>(R.id.tvAlbum)
        val tvYear = findViewById<TextView>(R.id.tvYear)
        val tvGenre = findViewById<TextView>(R.id.tvGenre)
        val tvCountry = findViewById<TextView>(R.id.tvCountry)
        val tvLength = findViewById<TextView>(R.id.tvLength)

        tvPreLength = findViewById(R.id.tvPrelength)
        tvPreLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(App.TRACK, TrackSearchItem.Track::class.java)
        } else {
            intent.getParcelableExtra(App.TRACK)
        }
        if (track != null) {
            url = track.previewUrl
            preparePlayer()
            val imageUrl = URL(
                track.artworkUrl100.replaceAfterLast(
                    '/',
                    resources.getString(R.string.new_dimension_image)
                )
            )
            Glide.with(ivTrackImage)
                .load(imageUrl)
                .placeholder(R.drawable.empty_track_image)
                .transform(RoundedCorners(dpToPx(8)))
                .into(ivTrackImage)
            tvTrackName.text = track.trackName
            tvNameArtist.text = track.artistName
            tvAlbum.text = track.collectionName
            val year = track.releaseDate.substring(0..3)
            tvYear.text = year
            tvGenre.text = track.primaryGenreName
            tvCountry.text = track.country
            tvLength.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis.toLong())
        }
        btPlay.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            btPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            btPlay.setImageResource(R.drawable.bt_play)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        val startTime = System.currentTimeMillis()
        mediaPlayer.start()
        btPlay.setImageResource(R.drawable.bt_paused)
        playerState = STATE_PLAYING
        mainThreadHandler?.post(
            createUpdateTimerTask(startTime)
        )
    }

    private fun createUpdateTimerTask(startTime: Long): Runnable {
        return object : Runnable {
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - startTime
                val remainingTime = LENGTH_TRACK - elapsedTime
                if (remainingTime > 0) {
                    tvPreLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    mainThreadHandler?.postDelayed(this, DELAY_UPDATE)
                }
                else{
                    tvPreLength.text = resources.getString(R.string.fixed_time_track)
                }
            }
        }
    }


    private fun pausePlayer() {
        mainThreadHandler?.removeCallbacksAndMessages(null)
        mediaPlayer.pause()
        btPlay.setImageResource(R.drawable.bt_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY_UPDATE = 300L
        private const val LENGTH_TRACK = 30000L
    }
}