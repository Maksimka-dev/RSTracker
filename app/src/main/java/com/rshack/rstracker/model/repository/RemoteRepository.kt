package com.rshack.rstracker.model.repository

import com.rshack.rstracker.model.data.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class RemoteRepository : IRepository {

    override suspend fun load(): List<Track> = withContext(Dispatchers.Main) {

        return@withContext listOf(
            Track(
                1,
                Calendar.getInstance().time,
                32192
            ),
            Track(
                2,
                Calendar.getInstance().time,
                9224
            ),
            Track(
                3,
                Calendar.getInstance().time,
                912
            ),
            Track(
                4,
                Calendar.getInstance().time,
                9212
            ),
            Track(
                5,
                Calendar.getInstance().time,
                9872
            ),
            Track(
                6,
                Calendar.getInstance().time,
                9542
            ),
            Track(
                7,
                Calendar.getInstance().time,
                9712
            )
        )
    }

}