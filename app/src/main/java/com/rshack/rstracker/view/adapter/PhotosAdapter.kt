package com.rshack.rstracker.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rshack.rstracker.databinding.GridViewItemBinding
import com.rshack.rstracker.network.Urls

class PhotosAdapter(private val onClickListener: OnClickListener) :
    PagedListAdapter<Urls, PhotosAdapter.PhotoViewHolder>(DiffCallback) {

    class PhotoViewHolder(binding: GridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val img: ImageView = binding.photoImage
    }

    class OnClickListener(val clickListener: (urls: Urls) -> Unit) {
        fun onClick(urls: Urls) = clickListener(urls)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Urls>() {
        override fun areItemsTheSame(oldItem: Urls, newItem: Urls): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Urls, newItem: Urls): Boolean {
            return oldItem.photo.thumb == newItem.photo.thumb
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            GridViewItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val urls = getItem(position) ?: return
        holder.itemView.setOnClickListener {
            onClickListener.onClick(urls)
        }
        bindImage(holder.img, urls.photo.thumb)
    }
}