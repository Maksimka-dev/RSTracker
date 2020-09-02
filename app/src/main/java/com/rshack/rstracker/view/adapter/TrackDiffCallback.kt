package com.rshack.rstracker.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.rshack.rstracker.model.data.Track

class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {

    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.id == newItem.id
    }
}
