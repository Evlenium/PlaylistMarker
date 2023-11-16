package com.practicum.playlistmarker

import android.content.res.Resources
import android.os.Bundle
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


class AudioPlayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        val toolbarMediaLibraryActivity = findViewById<Toolbar>(R.id.toolbarMediaLibraryActivity)
        toolbarMediaLibraryActivity.setNavigationIcon(R.drawable.bt_arrow_back_mode)
        toolbarMediaLibraryActivity.setNavigationOnClickListener { finish() }
        toolbarMediaLibraryActivity.setTitleTextAppearance(
            this,
            R.style.SecondsActivityMediumTextAppearance
        )

        val ivTrackImage = findViewById<ImageView>(R.id.ivTrackImage)

        val btAddPlaylist = findViewById<ImageButton>(R.id.btAddPlaylist)
        val btPlay = findViewById<ImageButton>(R.id.btPlay)
        val btLike = findViewById<ImageButton>(R.id.btLike)

        val tvTrackName = findViewById<TextView>(R.id.tvTrackName)
        val tvNameArtist = findViewById<TextView>(R.id.tvNameArtist)
        val tvAlbum = findViewById<TextView>(R.id.tvAlbum)
        val tvYear = findViewById<TextView>(R.id.tvYear)
        val tvGenre = findViewById<TextView>(R.id.tvGenre)
        val tvCountry = findViewById<TextView>(R.id.tvCountry)
        val tvLength = findViewById<TextView>(R.id.tvLength)

        val track = intent.getSerializableExtra(App.TRACK) as TrackSearchItem.Track?

        if (track != null) {
            val imageUrl = URL(track.artworkUrl100.replaceAfterLast('/',resources.getString(R.string.new_dimension_image)))
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
            tvLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
        }
    }
    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}