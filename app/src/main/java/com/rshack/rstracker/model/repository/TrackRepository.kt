package com.rshack.rstracker.model.repository

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rshack.rstracker.model.data.Track
import com.rshack.rstracker.service.GpsService
import com.rshack.rstracker.utils.TAG

class TrackRepository : ITrackRepository {

    private val points = MutableLiveData<List<LatLng>>()
        .apply { value = listOf() }

    override fun getCoordinates(): MutableLiveData<List<LatLng>> {
        return points
    }

    override fun getCoordinates(track: Track) {
        val path = "locations_" +
            FirebaseAuth.getInstance().currentUser?.uid + "/${track.id}"
        val ref = FirebaseDatabase.getInstance().getReference(path)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                points.value = snapshot.children
                    .filter { it.hasChildren() }
                    .map { point ->
                        val value = point.value as HashMap<*, *>?
                        val lat = value!!["latitude"].toString().toDouble()
                        val lng = value["longitude"].toString().toDouble()
                        LatLng(lat, lng)
                    }
            }
        })
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
        clearCoordinates()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val path = "locations_$uid/track$trackDate"
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
                return
            }

            override fun onChildMoved(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {
                return
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                return
            }

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
            Log.i(TAG, "add point error")
        }
    }

    override fun saveTimeAndDistanceToFirebase(time: Long, distance: Float, trackDate: Long) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val path = "locations_$uid/track$trackDate"
        var ref = FirebaseDatabase.getInstance().getReference("$path/time")
        ref.setValue(time)
        ref = FirebaseDatabase.getInstance().getReference("$path/distance")
        ref.setValue(distance)
    }

    override fun updateTrack(track: Track) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val path = "locations_$uid/${track.id}"
        val ref = FirebaseDatabase.getInstance().getReference(path)
        ref.setValue(track)
    }
}
