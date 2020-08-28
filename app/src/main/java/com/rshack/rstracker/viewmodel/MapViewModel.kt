package com.rshack.rstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.rshack.rstracker.model.repository.FirebaseAuthenticationRepository
import com.rshack.rstracker.model.repository.ITrackRepository
import com.rshack.rstracker.model.repository.TrackRepository

class MapViewModel : ViewModel() {

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
}