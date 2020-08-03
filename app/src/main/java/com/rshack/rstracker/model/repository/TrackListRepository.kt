package com.rshack.rstracker.model.repository

import com.rshack.rstracker.model.data.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.util.*

class TrackListRepository : ITrackListRepository {

    override suspend fun load(): List<Track> = withContext(Dispatchers.Main) {
        // Load list of tracks from firebase(switch context to Dispatchers.IO)
        return@withContext listOf(
            Track(
                "fsjkc",
                456,
                32192f,
                55
            ),
            Track(
                "vkdsjvhs",
                78965,
                9224f,
                785
            )
        )
    }

}