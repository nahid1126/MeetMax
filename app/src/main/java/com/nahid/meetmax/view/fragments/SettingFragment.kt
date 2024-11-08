package com.nahid.meetmax.view.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.FragmentSettingBinding
import com.nahid.meetmax.utils.AppPreferences
import com.nahid.meetmax.utils.ApplicationCallBack
import com.nahid.meetmax.utils.ImageUtils
import com.nahid.meetmax.utils.checkStoragePermission
import com.nahid.meetmax.view_models.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import javax.inject.Inject

private const val TAG = "SettingFragment"
@AndroidEntryPoint
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var appPreferences: AppPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = this

        appPreferences.getGetUserEmail()?.let {
            userViewModel.setUser(it)
        }

        binding.setOnLogout {
            appPreferences.putLoginResponse("")
            findNavController().navigate(R.id.action_settingFragment_to_signInFragment)
        }

        return binding.root
    }



}