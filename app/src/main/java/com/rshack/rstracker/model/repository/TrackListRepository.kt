package com.rshack.rstracker.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rshack.rstracker.model.data.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class TrackListRepository : ITrackListRepository {

    override suspend fun load(): LiveData<List<Track>> = withContext(Dispatchers.Main) {
        val tracks = MutableLiveData<List<Track>>()
        val path = "locations"
        val ref =
            FirebaseDatabase.getInstance().getReference(path)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Count ", "" + snapshot.childrenCount)

                tracks.value = snapshot.children.map { snap ->
                    val value = snap.value as HashMap<*, *>?
                    val id = value!!["id"].toString()
                    val time = value["time"].toString().toLong()
                    val distance = value["distance"].toString().toFloat()
                    val date = value["date"].toString().toLong()
                    Track(id, date, distance, time)
                }
            }

        })
        return@withContext tracks
    }

}
