package com.rshack.rstracker.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rshack.rstracker.R
import com.rshack.rstracker.model.data.Track
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class TrackAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val items = mutableListOf<Track>()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItems(newItems: List<Track>) {
        val diffCallback = TrackDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(track: Track) {
        itemView.apply {
            tv_date.text = track.date.toString()
            tv_distance.text = track.distance.toString()
            tv_time.text = track.time.toString()
        }
    }
}