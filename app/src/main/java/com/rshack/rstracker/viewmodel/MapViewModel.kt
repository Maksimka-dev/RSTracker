package com.rshack.rstracker.viewmodel

import android.app.Application
import android.content.Intent
import android.graphics.Color
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.rshack.rstracker.model.data.Track
import com.rshack.rstracker.model.repository.FirebaseAuthenticationRepository
import com.rshack.rstracker.model.repository.IAuthenticationRepository
import com.rshack.rstracker.model.repository.ITrackRepository
import com.rshack.rstracker.model.repository.TrackRepository
import com.rshack.rstracker.service.GpsService

private const val POLYLINE_WIDTH = 10f

class MapViewModel @ViewModelInject constructor (
    val repository: ITrackRepository,
    private val firebaseAuthenticationRepository: IAuthenticationRepository
) : ViewModel() {

    private val _isRunning = MutableLiveData<Boolean>().apply { value = null }
    val isRunning: LiveData<Boolean>
        get() = _isRunning

    private val _distance = MutableLiveData<Float>()
    val distance: LiveData<Float>
        get() = _distance

    val points: MutableLiveData<List<LatLng>> = repository.getCoordinates()

    private val polyline = PolylineOptions()
        .width(POLYLINE_WIDTH)
        .color(Color.RED)

    fun changeStatus() {
        if (_isRunning.value == null) _isRunning.value = false
        else _isRunning.value = !_isRunning.value!!
    }

    fun clearPoints() {
        repository.clearCoordinates()
        _isRunning.value = null
    }

    fun startNewTrack(trackDate: Long) {
        repository.subscribeToUpdates(trackDate)
    }

    fun saveIntoFirebase(time: Long, distance: Float, trackDate: Long) {
        repository.saveTimeAndDistanceToFirebase(time, distance, trackDate)
    }

    fun updateDistance() {
        _distance.value = repository.getPolylineLength()
    }

    fun logout() {
        firebaseAuthenticationRepository.logout()
    }

    fun updatePolyline(map: GoogleMap) {
        // clear map and polyline
        polyline.points.clear()
        map.clear()
        val pointList = points.value ?: listOf()
        if (pointList.isEmpty()) return
        map.addMarker(MarkerOptions().title("Start").position(pointList.first()))
        if (pointList.size > 1)
            map.addMarker(MarkerOptions().title("End").position(pointList.last()))
        // add polyline
        map.addPolyline(
            polyline.addAll(pointList)
        )
    }

    fun showTrack(track: Track) {
        repository.getCoordinates(track)
        _distance.value = track.distance
    }

    fun getEmail(): String? = firebaseAuthenticationRepository.getCurrentUserEmail()

}
