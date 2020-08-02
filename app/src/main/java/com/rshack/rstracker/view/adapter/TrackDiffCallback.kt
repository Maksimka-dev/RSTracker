package com.rshack.rstracker.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.rshack.rstracker.model.data.Track

class TrackDiffCallback(
    private val oldList: List<Track>,
    private val newList: List<Track>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].time == newList[newItemPosition].time
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (id1, url1) = oldList[oldItemPosition]
        val (id2, url2) = newList[newItemPosition]

        return id1 == id2 && url1 == url2
    }
}