package com.nahid.meetmax.view.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.FragmentSignUpBinding
import com.nahid.meetmax.model.network.NetworkResponse
import com.nahid.meetmax.utils.CustomToast
import com.nahid.meetmax.utils.Status
import com.nahid.meetmax.utils.Tools
import com.nahid.meetmax.view_models.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone


@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        binding.viewModel = signUpViewModel
        binding.lifecycleOwner = this

        progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Loading...")
            setCancelable(false)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.message.collect {
                    CustomToast.showToast(requireContext(), it, Status.FAILED)
                }
            }
        }

        binding.textDob.setOnClickListener {
            showDatePickerDialog()
        }

//        those lines are for when api is ready for signIn response
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.signUpResponse.collect {
                    when (it) {
                        is NetworkResponse.Empty -> {

                        }

                        is NetworkResponse.Error -> {
                            CustomToast.showToast(requireContext(), "${it.message}", Status.FAILED)
                        }

                        is NetworkResponse.Loading -> {
                            progressDialog.show()
                        }

                        is NetworkResponse.Success -> {
                            CustomToast.showToast(
                                requireContext(),
                                "Sign Up Successful",
                                Status.SUCCESS
                            )
                            clearAllField()
                            progressDialog.dismiss()
                        }
                    }
                }
            }
        }

        binding.buttonSignUp.setOnClickListener {
            // signUpViewModel.signUp() when api is ready for login then uncomment this and remove below line
            signUpViewModel.signUp { response, message ->
                if (response) {
                    CustomToast.showToast(requireContext(), message, Status.SUCCESS)
                    findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                } else {
                    CustomToast.showToast(requireContext(), message, Status.FAILED)
                }
            }
        }

        binding.signIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        return binding.root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        calendar.timeInMillis = today
        val constraintsBuilder = CalendarConstraints.Builder()
            .setEnd(today)
        val dateRangePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.ThemeMaterialCalender)
                .setTitleText("Select Date Of Birth")
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
        dateRangePicker.show(requireActivity().supportFragmentManager, "datePicker")

        dateRangePicker.addOnPositiveButtonClickListener {
            binding.textDob.text = Editable.Factory.getInstance()
                .newEditable(
                    Tools.simpleDateTimeConvert(it)
                )
        }
        dateRangePicker.addOnNegativeButtonClickListener {
            dateRangePicker.dismiss()
        }
    }

    private fun clearAllField() {
        binding.apply {
            textEmail.text?.clear()
            textName.text?.clear()
            textPass.text?.clear()
            textDob.text?.clear()
            radioGroup.clearCheck()
        }
    }
}