package com.rshack.rstracker.model.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.rshack.rstracker.model.data.Track

interface ITrackRepository {
    fun getCoordinates(): MutableLiveData<List<LatLng>>
    fun getCoordinates(track: Track)
    fun clearCoordinates()
    fun getPolylineLength(): Float
    fun subscribeToUpdates(trackDate: Long)
    fun saveTimeAndDistanceToFirebase(time: Long, distance: Float, trackDate: Long)
    fun updateTrack(track: Track)
}
