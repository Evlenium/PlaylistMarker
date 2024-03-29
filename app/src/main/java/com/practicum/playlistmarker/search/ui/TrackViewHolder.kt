package com.practicum.playlistmarker.search.ui

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvNameComposition: TextView = itemView.findViewById(R.id.tv_name_composition)
    private val tvNameCompositor: TextView = itemView.findViewById(R.id.tv_name_compositor)
    private val tvLengthComposition: TextView = itemView.findViewById(R.id.tv_length_composition)
    private val icTrack: ImageView = itemView.findViewById(R.id.icon_track)


    fun bind(item: TrackSearchItem.Track) {
        tvNameComposition.text = item.trackName
        tvNameCompositor.text = item.artistName
        tvLengthComposition.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis.toLong())

        val imageUrl = URL(item.artworkUrl100)
        Glide.with(icTrack)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.empty_track_image)
            .transform(RoundedCorners(dpToPx(2)))
            .into(icTrack)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}