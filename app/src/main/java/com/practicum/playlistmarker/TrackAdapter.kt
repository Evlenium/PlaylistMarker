package com.practicum.playlistmarker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private var data: List<Track>, private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    fun setUpTracks(tracks: List<Track>) {
        data = tracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(data[position]) }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}