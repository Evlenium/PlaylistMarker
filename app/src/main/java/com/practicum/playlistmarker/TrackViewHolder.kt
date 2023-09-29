package com.practicum.playlistmarker

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.net.URL


class TrackViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    private val tvNameComposition:TextView = itemView.findViewById(R.id.tv_name_composition)
    private val tvNameCompositor:TextView = itemView.findViewById(R.id.tv_name_compositor)
    private val tvLengthComposition:TextView = itemView.findViewById(R.id.tv_length_composition)
    private val icTrack:ImageView = itemView.findViewById(R.id.icon_track)

    fun bind(item:Track){
        tvNameComposition.text = item.trackName
        tvNameCompositor.text = item.artistName
        tvLengthComposition.text = item.trackTime
        val imageUrl = URL(item.artworkUrl100)
        Glide.with(icTrack)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(dpToPx(2)))
            .into(icTrack)
    }
    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}