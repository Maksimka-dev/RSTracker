package com.rshack.rstracker.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayoutMediator
import com.rshack.rstracker.R
import com.rshack.rstracker.databinding.ActivityMainBinding
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

        setContentView(binding.root)
    }

}