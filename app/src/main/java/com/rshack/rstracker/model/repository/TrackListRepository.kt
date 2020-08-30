package com.rshack.rstracker.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rshack.rstracker.model.data.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackListRepository : ITrackListRepository {

    override suspend fun load(): LiveData<List<Track>> = withContext(Dispatchers.Main) {
        val tracks = MutableLiveData<List<Track>>()
        val path = "locations_" + FirebaseAuth.getInstance().currentUser?.uid
        val ref =
            FirebaseDatabase.getInstance().getReference(path)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                tracks.value = snapshot.children
                    .reversed()
                    .map { snap ->
                        val value = snap.value as HashMap<*, *>?
                        val id = value!!["id"].toString()
                        val time = value["time"].toString().toLong()
                        val distance = value["distance"]?.toString()?.toFloat() ?: 0f
                        val date = value["date"].toString().toLong()
                        val imgUrl = value["imgUrl"]?.toString() ?: ""
                        Track(id, date, distance, time, imgUrl)
                    }
            }
        })
        return@withContext tracks
    }
}
