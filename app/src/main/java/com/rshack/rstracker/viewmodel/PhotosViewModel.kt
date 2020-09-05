package com.rshack.rstracker.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.rshack.rstracker.model.data.Track
import com.rshack.rstracker.model.data.Urls
import com.rshack.rstracker.model.repository.ITrackRepository
import com.rshack.rstracker.view.adapter.PhotoDataSource

class PhotosViewModel @ViewModelInject constructor(
    val repository: ITrackRepository
) : ViewModel() {
    val photos: LiveData<PagedList<Urls>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()
        photos = initializedPagedListBuilder(config).build()
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
        LivePagedListBuilder<Int, Urls> {
        val dataSourceFactory = object : DataSource.Factory<Int, Urls>() {
            override fun create(): DataSource<Int, Urls> {
                return PhotoDataSource(viewModelScope)
            }
        }
        return LivePagedListBuilder<Int, Urls>(dataSourceFactory, config)
    }

    fun onPhotoClicked(urls: Urls, track: Track) {
        track.imgUrl = urls.photo.thumb
        repository.updateTrack(track)
    }
}
