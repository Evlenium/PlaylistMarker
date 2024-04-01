package com.practicum.playlistmarker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.search.presentation.model.TrackSearchItem

class TrackAdapter(
    private var data: List<TrackSearchItem>,
    private var clickListener: TrackClickListener?,
    private val buttonClickListener: ButtonClickListener?,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    constructor(data: List<TrackSearchItem>) : this(data, null, null) {
        this.data = data
    }

    constructor(data: List<TrackSearchItem>, clickListener: TrackClickListener) : this(
        data,
        clickListener,
        null
    ) {
        this.data = data
        this.clickListener = clickListener
    }

    fun setUpTracks(tracks: List<TrackSearchItem>) {
        data = tracks
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is TrackSearchItem.Track -> TYPE_TRACK
            is TrackSearchItem.Button -> TYPE_BUTTON
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TRACK -> {
                TrackViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
                )
            }

            TYPE_BUTTON -> {
                ButtonViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.button_item, parent, false)
                )
            }

            else -> throw java.lang.IllegalArgumentException("unknown")
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = data[position]) {
            is TrackSearchItem.Button -> {
                (holder as ButtonViewHolder).bind()
                holder.bind().setOnClickListener { buttonClickListener?.onButtonClick(item) }
            }

            is TrackSearchItem.Track -> {
                (holder as TrackViewHolder).bind(item)
                holder.itemView.setOnClickListener {
                    clickListener?.onTrackClick(item)
                }
            }
        }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: TrackSearchItem.Track)
    }

    fun interface ButtonClickListener {
        fun onButtonClick(track: TrackSearchItem.Button)
    }

    companion object {
        const val TYPE_TRACK = 0
        const val TYPE_BUTTON = 1
    }
}
