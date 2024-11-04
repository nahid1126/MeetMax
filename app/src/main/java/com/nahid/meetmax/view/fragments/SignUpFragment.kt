package com.nahid.meetmax.view.fragments

import android.os.Bundle
import android.text.Editable
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
import com.google.android.material.datepicker.MaterialDatePicker
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.FragmentSignUpBinding
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        binding.viewModel = signUpViewModel
        binding.lifecycleOwner = this

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
        binding.buttonSignUp.setOnClickListener {
            signUpViewModel.signUp()
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
        val dateRangePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.ThemeMaterialCalender)
                .setTitleText("Select Date")
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

}