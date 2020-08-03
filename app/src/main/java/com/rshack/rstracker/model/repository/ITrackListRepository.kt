package com.rshack.rstracker.model.repository

import androidx.lifecycle.LiveData
import com.rshack.rstracker.model.data.Track

interface ITrackListRepository {
    suspend fun load(): LiveData<List<Track>>
}