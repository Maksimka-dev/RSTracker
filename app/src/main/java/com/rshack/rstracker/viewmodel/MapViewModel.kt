package com.rshack.rstracker.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.rshack.rstracker.R
import com.rshack.rstracker.model.repository.ITrackRepository
import com.rshack.rstracker.model.repository.TrackRepository

class MapViewModel : ViewModel() {

    private val repository: ITrackRepository = TrackRepository()

    private val _isRunning = MutableLiveData<Boolean>().apply { value = null }
    val isRunning: LiveData<Boolean>
        get() = _isRunning

    val points: LiveData<List<LatLng>>
        get() = repository.getCoordinates()

    fun changeStatus() {
        if (_isRunning.value == null) _isRunning.value = false
        else _isRunning.value = !_isRunning.value!!
    }

    fun clearPoints() {
        repository.clearCoordinates()
    }

    fun startNewTrack(trackDate: Long) {
        repository.subscribeToUpdates(trackDate)
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

    fun getPolylineLength() = repository.getPolylineLength()
}