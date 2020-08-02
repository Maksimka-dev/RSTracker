package com.rshack.rstracker.service

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import com.rshack.rstracker.R
import com.rshack.rstracker.model.Track

class GpsService : Service() {

    private var trackDate: Long = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        trackDate = intent?.getLongExtra(TRACK_DATE, 0) ?: 0
        requestLocationUpdates()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        buildNotification()
    }

    private fun buildNotification() {
        registerReceiver(stopReceiver, IntentFilter(STOP))
        val broadcastIntent = PendingIntent.getBroadcast(
            this, 0, Intent(STOP), PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Create the persistent notification
        var channelId = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel()
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_text))
            .setOngoing(true)
            .setContentIntent(broadcastIntent)
            .setSmallIcon(R.drawable.ic_tracker)
        startForeground(1, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val chan = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
        )
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        service.createNotificationChannel(chan)
        return CHANNEL_ID
    }

    private var stopReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            Log.d(TAG, "received stop broadcast")
            // Stop the service when the notification is tapped
            unregisterReceiver(this)
            stopSelf()
        }
    }

    private fun requestLocationUpdates() {
        val request = LocationRequest()
        request.interval = 5000
        request.fastestInterval = 2500
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client =
            LocationServices.getFusedLocationProviderClient(this)
        var path = getString(R.string.firebase_path)

        val id = getString(R.string.track_id) + trackDate
        val track = Track(trackDate)

        val ref = FirebaseDatabase.getInstance()
            .getReference("$path/$id")
        ref.setValue(track)

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location? = locationResult.lastLocation
                    if (location != null) {
                        val locationRef = FirebaseDatabase.getInstance()
                            .getReference("$path/$id/${location.time}")
                        Log.d(TAG, "location update $location")
                        locationRef.setValue(location)
                    }
                }
            }, null)
        }
    }

    companion object {
        const val TAG = "Debug"
        const val STOP = "stop"
        const val CHANNEL_ID = "channel_id"
        const val CHANNEL_NAME = "channel_name"
        const val TRACK_DATE = "track_date"
    }
}
