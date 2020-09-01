package com.rshack.rstracker.view.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.rshack.rstracker.R
import com.rshack.rstracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var appBarConfiguration: AppBarConfiguration


    //    private lateinit var auth: FirebaseAuth
//    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
//    private val navController by lazy {
//        this.findNavController(R.id.nav_host_fragment)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        drawerLayout = binding.drawerLayout

//        setupNavigationMenu()

//        auth = FirebaseAuth.getInstance()

//        setSupportActionBar(binding.toolbar)

//        binding.navView.setNavigationItemSelectedListener {
//            drawerLayout.closeDrawers()
//            when (it.itemId) {
//                R.id.nav_map_fragment -> {
////                    if(!navController.popBackStack(R.id.mapFragment, false))
//                    navController.navigate(R.id.mapFragment)
//                }
//                R.id.nav_results_fragment -> {
////                    if(!navController.popBackStack(R.id.resultsFragment, false))
//                    navController.navigate(R.id.resultsFragment)
//                }
//                R.id.nav_night_mode -> {
//                    setTheme()
//                }
//            }
//            true
//        }

//        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
//        drawerLayout.addDrawerListener(drawerToggle)
//        drawerToggle.syncState()
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    // button back and up works the same
//    override fun onSupportNavigateUp(): Boolean {
////        return findNavController(R.id.nav_host_fragment).navigateUp()
//        return NavigationUI.navigateUp(navController, drawerLayout)
//    }

//    private fun setupNavigationMenu() {
//        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
//            if (nd.id == nc.graph.startDestination || nd.id == R.id.registerFragment) {
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//            } else {
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
//            }
//        }
        // turn on left menu
//        binding.navView.setupWithNavController(navController)
//        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                drawerLayout.openDrawer(GravityCompat.START)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
    }
