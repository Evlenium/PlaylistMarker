package com.practicum.playlistmarker

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val buttonClearHistory: Button = itemView.findViewById(R.id.buttonClearHistory)
    fun bind(): Button {
        return buttonClearHistory
    }
}