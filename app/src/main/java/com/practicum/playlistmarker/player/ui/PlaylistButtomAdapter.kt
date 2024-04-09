package com.practicum.playlistmarker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import com.practicum.playlistmarker.media_library.ui.PlaylistViewHolder

class PlaylistButtomAdapter(
    private var playlists: List<Playlist>,
    private val clickListener: PlaylistClickListener?,
) :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    fun setUpPlaylists(data: List<Playlist>) {
        playlists = data
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.little_playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = playlists[position]
        holder.bind(item,true)
        holder.itemView.setOnClickListener {
            clickListener?.onPlaylistClick(item)
        }
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}