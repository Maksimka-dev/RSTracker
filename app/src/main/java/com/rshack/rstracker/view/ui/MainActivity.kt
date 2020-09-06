package com.rshack.rstracker.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rshack.rstracker.R
import com.rshack.rstracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.getOrNull(0)
        if (currentFragment is FragmentOnBackPressedListener ) {
            return currentFragment.onBackPressed()
        }
        super.onBackPressed()
    }
}
