package com.nahid.meetmax.view.fragments

import android.app.ProgressDialog
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
import com.nahid.meetmax.databinding.FragmentForgotPasswordBinding
import com.nahid.meetmax.utils.CustomToast
import com.nahid.meetmax.utils.Status
import com.nahid.meetmax.view_models.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var progressDialog: ProgressDialog
    lateinit var signInViewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]

        binding.viewModel = signInViewModel
        binding.lifecycleOwner = this

        progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Loading...")
            setCancelable(false)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.message.collect {
                    CustomToast.showToast(requireContext(), it, Status.FAILED)
                }
            }
        }

        binding.buttonSend.setOnClickListener {
            signInViewModel.forgotPassword { response, message ->
                if (response) {
                    CustomToast.showToast(requireContext(), message, Status.SUCCESS)
                } else {
                    CustomToast.showToast(requireContext(), message, Status.FAILED)
                }
            }
        }

        binding.backSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
        }
        return binding.root
    }

}