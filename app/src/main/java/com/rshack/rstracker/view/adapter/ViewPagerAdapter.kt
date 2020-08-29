package com.rshack.rstracker.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rshack.rstracker.view.ui.MapFragment
import com.rshack.rstracker.view.ui.ResultsFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MapFragment.newInstance()
        else -> ResultsFragment.newInstance()
    }
}
