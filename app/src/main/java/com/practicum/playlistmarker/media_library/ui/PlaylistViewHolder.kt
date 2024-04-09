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
    fun bind(playlist: Playlist, fromButtomRecycler: Boolean) {
        namePlaylist.text = playlist.playlistName
        var textCounter = when {
            playlist.counterTracks % 10 == 1 && playlist.counterTracks % 100 != 11 -> " трек"
            playlist.counterTracks % 10 in 2..5 && playlist.counterTracks % 100 < 20 -> " трека"
            playlist.counterTracks % 100 in 10..20 -> " треков"
            else -> " треков"
        }
        textCounter = "${playlist.counterTracks} $textCounter"
        counterPlaylist.text = textCounter
        if (playlist.uri != null) {
            imagePlaylist.setImageURI(playlist.uri.toUri())
        } else {
            if (fromButtomRecycler) {
                imagePlaylist.setImageResource(R.drawable.ph_empty)
            } else {
                imagePlaylist.setImageResource(R.drawable.placeholder_empty_image)
            }
        }
    }
}