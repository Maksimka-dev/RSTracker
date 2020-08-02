package com.rshack.rstracker.model.repository

import com.rshack.rstracker.model.data.Track

interface IRepository {
    suspend fun load(): List<Track>
}