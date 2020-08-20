package com.rshack.rstracker.model.repository

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rshack.rstracker.service.GpsService
import java.util.*

class TrackRepository : ITrackRepository {

    private val points = MutableLiveData<List<LatLng>>()
        .apply { value = listOf() }

    override fun getCoordinates(): LiveData<List<LatLng>> {
        return points
    }

    override fun clearCoordinates() {
        points.value = listOf()
    }

    override fun getPolylineLength(): Float {
        if (points.value!!.size <= 1) {
            return 0f
        }
        return points.value!!.zipWithNext { a, b ->
            val results = FloatArray(1)
            Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, results)
            results[0]
        }.sum()
    }

    override fun subscribeToUpdates(trackDate: Long) {
        val path = "locations/track$trackDate"
        val ref =
            FirebaseDatabase.getInstance().getReference(path)
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {
                addPoint(dataSnapshot)
            }

            override fun onChildChanged(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {
            }

            override fun onChildMoved(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onCancelled(error: DatabaseError) {
                Log.d(
                    GpsService.TAG,
                    "Failed to read value.",
                    error.toException()
                )
            }
        })
    }

    private fun addPoint(dataSnapshot: DataSnapshot) {
        try {
            val value = dataSnapshot.value as HashMap<*, *>?
            val lat = value!!["latitude"].toString().toDouble()
            val lng = value["longitude"].toString().toDouble()
            val location = LatLng(lat, lng)
            val list = points.value?.toMutableList()
            list?.add(location)
            points.value = list
        } catch (e: Exception) {
        }
    }

    override fun saveTimeAndDistanceToFirebase(time: Long, distance: Float, trackDate: Long) {
        val path = "locations/track$trackDate"
        var ref = FirebaseDatabase.getInstance().getReference("$path/time")
        ref.setValue(time)
        ref = FirebaseDatabase.getInstance().getReference("$path/distance")
        ref.setValue(distance)
    }

}