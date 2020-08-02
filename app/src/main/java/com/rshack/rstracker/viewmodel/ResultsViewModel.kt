package com.rshack.rstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rshack.rstracker.model.data.Track
import com.rshack.rstracker.model.repository.IRepository
import com.rshack.rstracker.model.repository.RemoteRepository
import kotlinx.coroutines.launch

class ResultsViewModel : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>>
        get() = _tracks

    private val repository: IRepository = RemoteRepository()

    init {
        viewModelScope.launch {
            // Load from firebase though repository
            _tracks.value = repository.load()
        }
    }

}
