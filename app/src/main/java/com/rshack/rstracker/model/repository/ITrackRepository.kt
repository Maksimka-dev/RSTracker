package com.rshack.rstracker.model.repository

interface ITrackRepository {
    suspend fun getCoordinates()
}