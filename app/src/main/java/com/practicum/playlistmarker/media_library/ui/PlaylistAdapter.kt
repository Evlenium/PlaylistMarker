package com.practicum.playlistmarker.media_library.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist

class PlaylistAdapter(private var playlists: List<Playlist>, val context: Context) :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    fun setUpPlaylists(data: List<Playlist>) {
        playlists = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position], false, context)
    }

}