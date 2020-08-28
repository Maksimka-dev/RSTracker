package com.rshack.rstracker.view.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.widget.Chronometer
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.FirebaseDatabase
import com.rshack.rstracker.R
import com.rshack.rstracker.databinding.FragmentMapBinding
import com.rshack.rstracker.service.GpsService
import com.rshack.rstracker.viewmodel.MapViewModel
import kotlin.math.round

private const val PERMISSION_LOCATION = 1

class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var application: Application
    private val viewModel: MapViewModel by activityViewModels()
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private lateinit var stopwatch: Chronometer
    private var trackDate: Long = 0

//    private lateinit var firebaseAuth: FirebaseAuth

    private val polyline = PolylineOptions()
        .width(5f)
        .color(Color.RED)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        application = requireNotNull(activity).application
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        stopwatch = binding.stopwatch

        setHasOptionsMenu(true)
//        firebaseAuth = FirebaseAuth.getInstance()

        viewModel.points.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                binding.tvDistance.text =
                    (round(viewModel.getPolylineLength() * 10) / 10.0).toString() + " м"
                drawPolyline(it)
            }
        })

        binding.floatingButton.setOnClickListener {
            viewModel.changeStatus()
        }

        viewModel.isRunning.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            when (it) {
                true -> {
                    // save in firebase
                    Log.i(
                        "how_to_get_millisec",
                        "${SystemClock.elapsedRealtime() - stopwatch.base}"
                    )
                    Toast.makeText(context, "Stop tracking", Toast.LENGTH_SHORT).show()
                    stopwatch.stop()
                    //save time and distance to database
                    val time = SystemClock.elapsedRealtime() - stopwatch.base
                    val distance = viewModel.getPolylineLength()
                    viewModel.saveIntoFirebase(time, distance, trackDate)
                    viewModel.clearPoints()
                    stopwatch.base = SystemClock.elapsedRealtime()
                    binding.floatingButton.setImageResource(R.drawable.ic_start)
                    stopService()
                }
                false -> {
                    //start service if permission granted
                    if (isLocationPermissionGranted()) {
                        Toast.makeText(context, "Start tracking", Toast.LENGTH_SHORT).show()
                        Log.d(GpsService.TAG, "service started")
                        stopwatch.base = SystemClock.elapsedRealtime()
                        stopwatch.start()
                        binding.floatingButton.setImageResource(R.drawable.ic_stop)
                        trackDate = System.currentTimeMillis()
                        startTrackerService()
                        viewModel.startNewTrack(trackDate)
                    }
                }
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopService()
        _binding = null
    }

    private fun stopService() {
        application.stopService(Intent(application, GpsService()::class.java))
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setPadding(0, 0, 0, 800)
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
            map.setOnMyLocationClickListener { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
            }
        }
    }

    private fun startTrackerService() {
        val intent = Intent(application, GpsService()::class.java)
        intent.putExtra(GpsService.TRACK_DATE, trackDate)
        application.startService(intent)
    }

    private fun isLocationPermissionGranted(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return if (permission == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_LOCATION
            )
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_LOCATION && grantResults.size == 1 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Start the service when the permission is granted
            startTrackerService()
        }
    }

    private fun drawPolyline(points: List<LatLng>) {
        //clear map and polyline
        polyline.points.clear()
        map.clear()
        //add start and end markers
        map.addMarker(MarkerOptions().title("Start").position(points.first()))
        if (points.size > 1)
            map.addMarker(MarkerOptions().title("End").position(points.last()))
        //add polyline
        map.addPolyline(
            polyline.addAll(points)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_btn_logout -> {
                // todo logout
//                firebaseAuth.signOut()
                viewModel.logout()
                findNavController().navigate(R.id.action_mapFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}