package com.nahid.meetmax.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.FragmentSignInBinding
import com.nahid.meetmax.model.network.NetworkResponse
import com.nahid.meetmax.utils.Constants.RC_SIGN_IN
import com.nahid.meetmax.utils.CustomToast
import com.nahid.meetmax.utils.Status
import com.nahid.meetmax.view_models.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater)

        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.message.collect {
                    CustomToast.showToast(requireContext(), it, Status.FAILED)
                }
            }
        }

        binding.buttonSignIn.setOnClickListener {
            signInViewModel.signIn()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.signInResponse.collect {
                    when (it) {
                        is NetworkResponse.Empty -> TODO()
                        is NetworkResponse.Error -> TODO()
                        is NetworkResponse.Loading -> TODO()
                        is NetworkResponse.Success -> TODO()
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
            signInWithGoogle()
        }

        return binding.root
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Sign-in successful, get the Google account
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val email = account.email
                    val displayName = account.displayName

                    // Use email or displayName as needed
                    Log.d("GoogleSignIn", "Email: $email, Name: $displayName")
                }
            } catch (e: ApiException) {
                // Handle failure
                Log.w("GoogleSignIn", "Google sign-in failed", e)
            }
        }
    }

}