package com.nahid.meetmax.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.FragmentDashboardBinding
import com.nahid.meetmax.utils.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    @Inject
    lateinit var appPreferences: AppPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        binding.setOnLogout {
            appPreferences.putLoginResponse("")
            findNavController().navigate(R.id.action_dashboardFragment_to_signInFragment)
        }

        return binding.root
    }


}