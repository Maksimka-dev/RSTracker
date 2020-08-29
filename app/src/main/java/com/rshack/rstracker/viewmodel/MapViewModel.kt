package com.rshack.rstracker.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.rshack.rstracker.model.repository.FirebaseAuthenticationRepository
import com.rshack.rstracker.model.repository.ITrackRepository
import com.rshack.rstracker.model.repository.TrackRepository
import com.rshack.rstracker.service.GpsService

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private var _application = getApplication<Application>()

    private val repository: ITrackRepository = TrackRepository()
    private val firebaseAuthenticationRepository = FirebaseAuthenticationRepository()

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
        _isRunning.value = null
    }

    fun startNewTrack(trackDate: Long) {
        repository.subscribeToUpdates(trackDate)
    }

    fun saveIntoFirebase(time: Long, distance: Float, trackDate: Long) {
        repository.saveTimeAndDistanceToFirebase(time, distance, trackDate)
    }

    fun getPolylineLength() = repository.getPolylineLength()

    fun logout() {
        firebaseAuthenticationRepository.logout()
    }

    fun stopService() {
        _application.stopService(Intent(_application, GpsService()::class.java))
    }

    fun startService(trackDate: Long) {
        val intent = Intent(_application, GpsService()::class.java)
        intent.putExtra(GpsService.TRACK_DATE, trackDate)
        _application.startService(intent)
    }
}
