package com.practicum.playlistmarker.ui.search

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmarker.R

class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val buttonClearHistory: Button = itemView.findViewById(R.id.buttonClearHistory)
    fun bind(): Button {
        return buttonClearHistory
    }
}