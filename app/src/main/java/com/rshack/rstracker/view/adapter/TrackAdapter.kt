package com.rshack.rstracker.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rshack.rstracker.R
import com.rshack.rstracker.model.data.Track
import com.rshack.rstracker.utils.bindImage
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import java.text.DateFormat
import kotlin.math.round

class TrackAdapter(
    private val onDetailClickListener: OnDetailClickListener,
    private val onImageClickListener: OnImageClickListener
) : ListAdapter<Track, ViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_item, parent, false),
            onDetailClickListener, onImageClickListener
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }

    class OnDetailClickListener(val clickListener: (track: Track) -> Unit) {
        fun onClick(track: Track) = clickListener(track)
    }

    class OnImageClickListener(val clickListener: (track: Track) -> Unit) {
        fun onClick(track: Track) = clickListener(track)
    }
}

class ViewHolder(
    itemView: View,
    private val onDetailClickListener: TrackAdapter.OnDetailClickListener,
    private val onImageClickListener: TrackAdapter.OnImageClickListener
) :
    RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bind(track: Track) {
        itemView.apply {
            detailLayout.setOnClickListener {
                onDetailClickListener.onClick(track)
            }
            imageView.setOnClickListener {
                onImageClickListener.onClick(track)
            }
            val url = track.imgUrl
            if (url.isNotEmpty()) bindImage(imageView, url) else imageView
                .setImageResource(R.drawable.ic_baseline_directions_run_24)
            tv_date.text = DateFormat.getInstance().format(track.date)
            tv_distance.text = (round(track.distance * 100) / 100.0).toString() + " m"
            tv_time.text = (track.time / 1000).toString() + " c"
        }
    }
}
