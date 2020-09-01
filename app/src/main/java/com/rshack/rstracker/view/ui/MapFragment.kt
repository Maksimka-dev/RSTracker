package com.rshack.rstracker.view.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.rshack.rstracker.R
import com.rshack.rstracker.TAG
import com.rshack.rstracker.databinding.FragmentMapBinding
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

    private lateinit var drawerLayout: DrawerLayout
    private val navController by lazy {
        requireActivity().findNavController(R.id.nav_host_fragment)
    }

    private lateinit var map: GoogleMap
    private lateinit var stopwatch: Chronometer
    private var trackDate: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        application = requireNotNull(activity).application
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        stopwatch = binding.stopwatch
        drawerLayout = binding.drawerLayout

//        viewModel.getEmail()
        viewModel.clearPoints()
        viewModel.points.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                viewModel.updateDistance()
                viewModel.updatePolyline(map)
            }
        }

        viewModel.distance.observe(viewLifecycleOwner) {
            val distance = (round(it * 10) / 10.0).toString()
            binding.tvDistance.text = getString(R.string.distance, distance)
        }

        binding.floatingButton.setOnClickListener {
            viewModel.changeStatus()
        }

        viewModel.isRunning.observe(viewLifecycleOwner) {
            it ?: return@observe
            if (it) {
                stopTracking()
            } else {
                if (isLocationPermissionGranted()) startTracking()
            }
        }

        binding.navView.setupWithNavController(navController)
//        NavigationUI.setupActionBarWithNavController(activity, navController, drawerLayout)
        binding.navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            when (it.itemId) {
                R.id.nav_map_fragment -> {
                    if (navController.popBackStack(R.id.mapFragment, false)) {
                        navController.navigate(R.id.mapFragment)
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
                R.id.nav_results_fragment -> {
                    navController.navigate(R.id.resultsFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_night_mode -> {
//                    setTheme()
                }
            }
            true
        }

//        viewModel.emailLogIn.observe(viewLifecycleOwner) {
//            it ?: return@observe
//            Log.d(TAG, it)
//            textView.text = it
//            Log.d(TAG, it)
//        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.getEmail()
        viewModel.emailLogIn.observe(viewLifecycleOwner) {
            it ?: return@observe
            Log.d(TAG, it)
            view.findViewById<TextView>(R.id.nav_tv_email).text = it
            Log.d(TAG, it)
        }
    }

    private fun startTracking() {
        Toast.makeText(context, "Start tracking", Toast.LENGTH_SHORT).show()
        stopwatch.base = SystemClock.elapsedRealtime()
        stopwatch.start()
        binding.floatingButton.setImageResource(R.drawable.ic_stop)
        trackDate = System.currentTimeMillis()
        viewModel.startService(trackDate)
        viewModel.startNewTrack(trackDate)
    }

    private fun stopTracking() {
        Toast.makeText(context, "Stop tracking", Toast.LENGTH_SHORT).show()
        stopwatch.stop()
        // save time and distance to database
        val time = SystemClock.elapsedRealtime() - stopwatch.base
        viewModel.saveIntoFirebase(time, viewModel.distance.value ?: 0f, trackDate)
        viewModel.clearPoints()
        binding.floatingButton.setImageResource(R.drawable.ic_start)
        viewModel.stopService()
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
        viewModel.stopService()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setPadding(0, 0, 0, 400)
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
            map.setOnMyLocationClickListener { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
            }
        }

        //selected track from ResultsFragment
        val track = MapFragmentArgs.fromBundle(requireArguments()).selectedTrack
        if (track != null) {
            viewModel.showTrack(track)
            binding.stopwatch.base = SystemClock.elapsedRealtime() - track.time
        }
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
            // todo !!!
            viewModel.startService(trackDate)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            R.id.menu_btn_logout -> {
                viewModel.logout()
                findNavController().navigate(R.id.action_mapFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
