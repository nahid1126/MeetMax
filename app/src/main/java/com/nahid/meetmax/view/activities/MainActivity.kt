package com.nahid.meetmax.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.fragment)
        //binding.bottomNavigation.setupWithNavController(navController)
        val badge = binding.bottomNavigation.getOrCreateBadge(R.id.notification)
        badge.isVisible = true
        badge.number = 2

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.signInFragment, R.id.signUpFragment, R.id.forgotPasswordFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
            }
        }
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dashboardFragment -> {
                    if (navController.currentDestination?.id == R.id.settingFragment) {
                        navController.navigate(R.id.action_settingFragment_to_dashboardFragment)
                    } else if (navController.currentDestination?.id == R.id.createPostFragment) {
                        navController.navigate(R.id.action_createPostFragment_to_dashboardFragment)
                    }
                    true
                }

                R.id.settingFragment -> {
                    if (navController.currentDestination?.id == R.id.dashboardFragment) {
                        navController.navigate(R.id.action_dashboardFragment_to_settingFragment)
                    } else if (navController.currentDestination?.id == R.id.createPostFragment) {
                        navController.navigate(R.id.action_createPostFragment_to_settingFragment)
                    } else {
                        navController.navigate(R.id.action_signInFragment_to_dashboardFragment)
                    }
                    true
                }

                else -> false
            }
        }
    }
}