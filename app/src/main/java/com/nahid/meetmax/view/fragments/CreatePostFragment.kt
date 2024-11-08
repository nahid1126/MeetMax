package com.nahid.meetmax.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.FragmentCreatePostBinding
import com.nahid.meetmax.utils.AppPreferences
import com.nahid.meetmax.utils.CustomToast
import com.nahid.meetmax.utils.Status
import com.nahid.meetmax.view_models.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostFragment : Fragment() {
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var appPreference: AppPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePostBinding.inflate(layoutInflater)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = this

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.message.collect {
                    CustomToast.showToast(requireContext(), it, Status.FAILED)
                }
            }
        }
        appPreference.getGetUserEmail()?.let {
            userViewModel.getUser(it)
        }
        binding.textCreatePost.setOnClickListener {
            findNavController().navigate(R.id.action_createPostFragment_to_dashboardFragment)
        }

        binding.buttonPost.setOnClickListener {
            userViewModel.addPost { result, message ->
                if (result) {
                    CustomToast.showToast(requireContext(), message, Status.SUCCESS)
                } else {
                    CustomToast.showToast(requireContext(), message, Status.FAILED)
                }
            }
        }

        return binding.root
    }

}