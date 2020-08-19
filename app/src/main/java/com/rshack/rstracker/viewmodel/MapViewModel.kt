package com.rshack.rstracker.viewmodel

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rshack.rstracker.R
import com.rshack.rstracker.model.repository.ITrackRepository
import com.rshack.rstracker.model.repository.TrackRepository
import com.rshack.rstracker.service.GpsService
import java.util.*

class MapViewModel : ViewModel() {

    private val repository: ITrackRepository = TrackRepository()

    private val _isRunning = MutableLiveData<Boolean>().apply { value = null }
    val isRunning: LiveData<Boolean>
        get() = _isRunning

    private val _points = MutableLiveData<MutableList<LatLng>>()
    val points: LiveData<MutableList<LatLng>>
        get() = _points

    fun changeStatus() {
        if (_isRunning.value == null) _isRunning.value = false
        else _isRunning.value = !_isRunning.value!!
    }

    fun clearPoints() {
        _points.value?.clear()
    }

    fun saveTimeAndDistanceToFirebase(
        context: Context,
        time: Long,
        distance: Float,
        trackDate: Long
    ) {
        val path = context.getString(R.string.firebase_path) + "/" +
                context.getString(R.string.track_id) + trackDate
        var ref = FirebaseDatabase.getInstance().getReference("$path/time")
        ref.setValue(time)
        ref = FirebaseDatabase.getInstance().getReference("$path/distance")
        ref.setValue(distance)
    }

    fun subscribeToUpdates(context: Context, trackDate: Long) {
        val path = context.getString(R.string.firebase_path) + "/" +
                context.getString(R.string.track_id) + trackDate
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
            _points.value?.add(location)
//            points.add(location)
//            binding.tvDistance.text = (round(polylineLength() * 10) / 10.0).toString() + " м"
//            drawPolyline()
        } catch (e: Exception) {
        }
    }

    fun getPolylineLength(): Float {
        if (_points.value!!.size <= 1) {
            return 0f
        }
        return _points.value!!.zipWithNext { a, b ->
            val results = FloatArray(1)
            Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, results)
            results[0]
        }.sum()
    }

}