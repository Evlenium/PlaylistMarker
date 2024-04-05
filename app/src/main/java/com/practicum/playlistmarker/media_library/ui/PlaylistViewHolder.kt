package com.practicum.playlistmarker.media_library.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist

class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imagePlaylist: ImageView = itemView.findViewById(R.id.image_playlist_item)
    private val namePlaylist: TextView = itemView.findViewById(R.id.name_playlist_item)
    private val counterPlaylist: TextView = itemView.findViewById(R.id.counter_playlist_item)
    fun bind(playlist: Playlist) {
        namePlaylist.text = playlist.playlistName
        counterPlaylist.text = 0.toString()
        if (playlist.uri != null) {
            imagePlaylist.setImageURI(playlist.uri.toUri())
        } else {
            imagePlaylist.setImageResource(R.drawable.placeholder_empty_image)
        }
    }
}