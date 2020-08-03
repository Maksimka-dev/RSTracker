package com.rshack.rstracker.model.repository

import com.rshack.rstracker.model.data.Track

interface ITrackListRepository {
    suspend fun load(): List<Track>
}