package com.rshack.rstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.rshack.rstracker.network.Urls
import com.rshack.rstracker.view.adapter.PhotoDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class PhotosViewModel : ViewModel() {
    val photos: LiveData<PagedList<Urls>>

    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(false)
        .build()

    private val viewModelJob = Job()
    private val coroutineScope =
        CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        photos = initializedPagedListBuilder(config).build()
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Urls> {
        val dataSourceFactory = object : DataSource.Factory<Int, Urls>() {
            override fun create(): DataSource<Int, Urls> {
                return PhotoDataSource(coroutineScope)
            }
        }
        return LivePagedListBuilder<Int, Urls>(dataSourceFactory, config)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
