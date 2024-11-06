package com.nahid.meetmax.view.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.FragmentSignInBinding
import com.nahid.meetmax.model.network.NetworkResponse
import com.nahid.meetmax.utils.AppPreferences
import com.nahid.meetmax.utils.CustomToast
import com.nahid.meetmax.utils.Status
import com.nahid.meetmax.view_models.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SignInFragment"
@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var appPreferences: AppPreferences

    private val singIn = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater)

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

        binding.buttonSignIn.setOnClickListener {
            //signInViewModel.signIn() when api is ready for login then uncomment this and remove below line and callbackFunction from viewModel
            signInViewModel.signIn { response, message ->
                if (response) {
                    CustomToast.showToast(requireContext(), message, Status.SUCCESS)
                    signInViewModel.setUserData(appPreferences)
                    findNavController().navigate(R.id.action_signInFragment_to_dashboardFragment)
                } else {
                    CustomToast.showToast(requireContext(), message, Status.FAILED)
                }
            }
        }

        //those lines are for when api is ready for login response
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.signInResponse.collect {
                    when (it) {
                        is NetworkResponse.Empty -> {

                        }

                        is NetworkResponse.Error -> {
                            CustomToast.showToast(requireContext(), "${it.message}", Status.FAILED)
                            progressDialog.dismiss()
                        }

                        is NetworkResponse.Loading -> {
                            progressDialog.show()

                        }

                        is NetworkResponse.Success -> {
                            it.data?.let {
                                Log.d(TAG, "loginResponse: $it")
                            }
                            it.data?.let {
                                signInViewModel.setUserData(appPreferences, it)
                            }
                            CustomToast.showToast(requireContext(), "Success", Status.SUCCESS)
                            progressDialog.dismiss()
                            findNavController().navigate(R.id.action_signInFragment_to_dashboardFragment)

                        }
                    }
                }
            }

        }


        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
         googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        binding.googleButton.setOnClickListener {
           singIn.launch(googleSignInClient.signInIntent)
        }

        return binding.root
    }

}