package com.rshack.rstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rshack.rstracker.model.data.Track
import java.util.*

class ResultsViewModel : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>>
        get() = _tracks

    init {
        getTracks()
    }

    fun getTracks() {
        // Load from firebase though repository
        _tracks.value = listOf(
            Track(
                Calendar.getInstance().time,
                32192f,
                42f
            ),
            Track(
                Calendar.getInstance().time,
                9224f,
                4221f
            ),
            Track(
                Calendar.getInstance().time,
                912f,
                44242f
            ),
            Track(
                Calendar.getInstance().time,
                9212f,
                4287f
            ),
            Track(
                Calendar.getInstance().time,
                9872f,
                4287f
            ),
            Track(
                Calendar.getInstance().time,
                9542f,
                4412f
            ),
            Track(
                Calendar.getInstance().time,
                9712f,
                4212f
            )
        )

    }
}