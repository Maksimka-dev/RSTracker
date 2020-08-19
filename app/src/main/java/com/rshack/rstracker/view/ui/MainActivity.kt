package com.rshack.rstracker.view.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.rshack.rstracker.R
import com.rshack.rstracker.databinding.ActivityMainBinding
import com.rshack.rstracker.service.GpsService
import com.rshack.rstracker.view.adapter.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Map"
                1 -> tab.text = "Results"
            }
        }.attach()

        loginToFirebase()

        setContentView(binding.root)
    }

    private fun loginToFirebase() {
        // Authenticate with Firebase, and request location updates
        val email = getString(R.string.firebase_email)
        val password = getString(R.string.firebase_password)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(GpsService.TAG, "firebase auth success")
            } else {
                Log.d(GpsService.TAG, "firebase auth failed")
            }
        }
    }

}