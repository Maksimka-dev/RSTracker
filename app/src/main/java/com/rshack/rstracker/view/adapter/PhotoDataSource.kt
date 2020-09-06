package com.rshack.rstracker.view.adapter

import androidx.paging.PageKeyedDataSource
import com.rshack.rstracker.model.data.Urls
import com.rshack.rstracker.model.repository.network.PhotoApi
import java.net.UnknownHostException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PhotoDataSource(private val scope: CoroutineScope) : PageKeyedDataSource<Int, Urls>() {

    private val apiService = PhotoApi.retrofitService

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Urls>
    ) {
        scope.launch {
            try {
                val result = apiService.getPhotos(1)
                callback.onResult(result.results, null, 2)
            } catch (e: UnknownHostException) {
                loadInitial(params, callback)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Urls>) {
        scope.launch {
            try {
                val key = params.key
                val previousKey = if (key == 1) null else key.dec()
                val result = apiService.getPhotos(key)
                callback.onResult(result.results, previousKey)
            } catch (e: UnknownHostException) {
                loadBefore(params, callback)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Urls>) {
        scope.launch {
            try {

                val result = apiService.getPhotos(params.key)
                callback.onResult(result.results, params.key.inc())
            } catch (e: UnknownHostException) {
                loadAfter(params, callback)
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}
