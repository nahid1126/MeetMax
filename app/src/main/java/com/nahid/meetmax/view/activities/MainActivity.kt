package com.nahid.meetmax.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.ActivityMainBinding
import com.nahid.meetmax.utils.ApplicationCallBack
import com.nahid.meetmax.utils.Constants.storagePermissionBelow13
import com.nahid.meetmax.utils.Constants.storagePermissionUpper12
import com.nahid.meetmax.utils.checkStoragePermission
import com.nahid.meetmax.utils.checkStoragePermissionResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , ApplicationCallBack.MainListener{
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


    private val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            this.checkStoragePermissionResult(it) { isPermissionGranted ->
                if (isPermissionGranted) {//All Storage Permission Granted

                } else {//Ask Permission Again
                    requestStorage()
                }
            }


        }

    private fun requestStorage() {
        if (!this.checkStoragePermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestStoragePermission.launch(storagePermissionUpper12)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } else {
                    requestStoragePermission.launch(storagePermissionBelow13)
                }
            }
        }
    }

    override fun requestStoragePermission() {
        requestStorage()
    }
}