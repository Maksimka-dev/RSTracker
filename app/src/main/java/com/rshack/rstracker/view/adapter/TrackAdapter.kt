package com.rshack.rstracker.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rshack.rstracker.R
import com.rshack.rstracker.model.data.Track
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import java.text.DateFormat
import kotlin.math.round

class TrackAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<ViewHolder>() {

    private val items = mutableListOf<Track>()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false),
            onClickListener
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

    class OnClickListener(val clickListener: (track: Track) -> Unit) {
        fun onClick(track: Track) = clickListener(track)
    }
}

class ViewHolder(itemView: View, private val onClickListener: TrackAdapter.OnClickListener) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(track: Track) {
        itemView.apply {
            setOnClickListener {
                onClickListener.onClick(track)
            }
            tv_date.text = DateFormat.getInstance().format(track.date)
            tv_distance.text = (round(track.distance * 100) / 100.0).toString()
            tv_time.text = (track.time / 1000).toString() + " с"
        }
    }
}