package com.rshack.rstracker.model.repository

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rshack.rstracker.service.GpsService
import java.util.*

class TrackRepository : ITrackRepository {

    companion object {
        const val FIREBASE_PATH = "locations/track"
    }

    override fun saveIntoFirebase(
        time: Long,
        distance: Float,
        trackDate: Long
    ) {
        val path = FIREBASE_PATH + trackDate
        var ref = FirebaseDatabase.getInstance().getReference("$path/time")
        ref.setValue(time)
        ref = FirebaseDatabase.getInstance().getReference("$path/distance")
        ref.setValue(distance)
    }

    override fun subscribeToUpdates(trackDate: Long): LatLng {
        var location = LatLng(0.0, 0.0)
        val path = FIREBASE_PATH + trackDate
        val ref =
            FirebaseDatabase.getInstance().getReference(path)
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {
                addPoint(dataSnapshot)
//                addPoint(dataSnapshot)
//                Log.i("my_tag", "$location")
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
        return location
    }

    fun addPoint(dataSnapshot: DataSnapshot): LatLng {

        var location = LatLng(50.0, 50.0)
        try {
            val value = dataSnapshot.value as HashMap<*, *>?
            val lat = value!!["latitude"].toString().toDouble()
            val lng = value["longitude"].toString().toDouble()
            location = LatLng(lat, lng)


//            val list = _points.value?.toMutableList()
//            list?.add(location)
//            _points.value = list

//            points.add(location)
//            binding.tvDistance.text = (round(polylineLength() * 10) / 10.0).toString() + " м"
//            drawPolyline()
        } catch (e: Exception) {
//            Log.i("my_tag", "point exception")
        }
        return location
    }

}