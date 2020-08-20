package com.rshack.rstracker.model.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot

interface ITrackRepository {
    fun saveIntoFirebase(time: Long, distance: Float, trackDate: Long)
    fun subscribeToUpdates(trackDate: Long): LatLng
//    fun addPoint(dataSnapshot: DataSnapshot)
}