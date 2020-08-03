package com.rshack.rstracker.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackRepository : ITrackRepository {

    override suspend fun getCoordinates() = withContext(Dispatchers.Main) {
        // Load current position and path on map
        TODO("Not yet implemented")
    }

}