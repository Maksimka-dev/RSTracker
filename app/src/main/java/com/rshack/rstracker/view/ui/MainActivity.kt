package com.rshack.rstracker.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.rshack.rstracker.R
import com.rshack.rstracker.databinding.ActivityMainBinding
import com.rshack.rstracker.view.adapter.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MapFragment.newInstance())
            }
        }

        binding.viewPager.adapter = ViewPagerAdapter(this)

        setContentView(binding.root)
    }

}