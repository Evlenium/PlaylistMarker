package com.practicum.playlistmarker.media_library.ui

import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import java.io.File

class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imagePlaylist: ImageView = itemView.findViewById(R.id.image_playlist_item)
    private val namePlaylist: TextView = itemView.findViewById(R.id.name_playlist_item)
    private val counterPlaylist: TextView = itemView.findViewById(R.id.counter_playlist_item)
    fun bind(playlist: Playlist, fromButtomRecycler: Boolean, context: Context) {
        namePlaylist.text = playlist.playlistName

        val textCounter = context.resources.getQuantityString(
            R.plurals.plurals_track,
            playlist.counterTracks,
            playlist.counterTracks
        )
        counterPlaylist.text = textCounter
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val file = File(filePath, playlist.uri.toString())
        if (playlist.uri != null) {
            imagePlaylist.setImageURI(file.toUri())
        } else {
            if (fromButtomRecycler) {
                imagePlaylist.setImageResource(R.drawable.ph_empty)
            } else {
                imagePlaylist.setImageResource(R.drawable.placeholder_empty_image)
            }
        }
    }
}