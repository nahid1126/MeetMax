package com.nahid.meetmax.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.FragmentSplashBinding
import com.nahid.meetmax.utils.AppPreferences
import com.nahid.meetmax.utils.Tools
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        Tools.setSystemBarColor(requireActivity(), R.color.white)
        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)
            if (appPreferences.getLoginResponse().isNullOrEmpty()) {
                findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_dashboardFragment)
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}