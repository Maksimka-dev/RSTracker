package com.rshack.rstracker.model.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng

interface ITrackRepository {
    fun getCoordinates() : LiveData<List<LatLng>>
    fun clearCoordinates()
    fun getPolylineLength(): Float
    fun subscribeToUpdates(trackDate: Long)
}