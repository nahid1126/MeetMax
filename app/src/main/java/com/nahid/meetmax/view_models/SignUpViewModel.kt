package com.nahid.meetmax.view_models

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nahid.meetmax.R
import com.nahid.meetmax.di.qualifier.SignUpQualifier
import com.nahid.meetmax.model.data.User
import com.nahid.meetmax.model.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SignUpViewModel"
@HiltViewModel
class SignUpViewModel @Inject constructor(@SignUpQualifier private val signUpRepository: SignUpRepository) :
    ViewModel() {
    var message = MutableSharedFlow<String>()
    var userMailFlow = MutableStateFlow<String>("")
    var userNameFlow = MutableStateFlow<String>("")
    var userPassFlow = MutableStateFlow<String>("")
    var userDateOfBirthFlow = MutableStateFlow<String>("")
    val selectedOption = MutableStateFlow<Int>(0)

    val signUpResponse = signUpRepository.signUpResponse

    fun signUp() {
        val userMail = userMailFlow.value
        val userName = userNameFlow.value
        val userPass = userPassFlow.value
        val userDateOfBirth = userDateOfBirthFlow.value
         val genderSelect = when (selectedOption.value) {
             R.id.buttonMale -> "Male"

             R.id.buttonFemale -> "Female"
             else -> {
                 ""
             }
         }.toString()
        viewModelScope.launch {
            if (userMail.isEmpty()) {
                message.emit("Please Enter Mail")
            } else if (userMail.isNotEmpty() && !isValidEmail(userMail)) {
                message.emit("Please Enter Valid Email")
            } else if (userName.isEmpty()) {
                message.emit("Please Enter Name")
            } else if (userPass.isEmpty()) {
                message.emit("Please Enter Password")
            } else if (userPass.isNotEmpty() && validatePassword(userPass).equals(
                    "ok",
                    ignoreCase = true
                )
            ) {
                message.emit(validatePassword(userPass))
            } else if (userDateOfBirth.isEmpty()) {
                message.emit("Please Enter Date Of Birth")
            } else if (genderSelect.isEmpty()) {
                message.emit("Please Select Gender")
            } else {
                signUpRepository.requestForSignUp(
                    User(
                        userMail,
                        userName,
                        userPass,
                        userDateOfBirth,
                        genderSelect
                    )
                )
            }
        }
    }



    fun onGenderSelected(option: String) {
        selectedOption.value = when (option) {
            "Male" -> R.id.buttonMale
            "Female" -> R.id.buttonFemale
            else -> 0
        }

        Log.d(TAG, "onGenderSelected: ${selectedOption.value}")
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(userPass: String): String {
        val passwordInput: String = userPass.trim()

        val finalMessage = if (!passwordInput.matches(".{8,}".toRegex())) {
            "Password should contain 8 characters"
        } else if (!passwordInput.matches(".*[0-9].*".toRegex())) {
            "Password should contain at least 1 digit"
        } else if (!passwordInput.matches(".*[a-z].*".toRegex())) {
            "Password should contain at least 1 lower case letter"
        } else if (!passwordInput.matches(".*[A-Z].*".toRegex())) {
            "Password should contain at least 1 upper case letter"
        } else if (!passwordInput.matches(".*[a-zA-Z].*".toRegex())) {
            "Password should contain a letter"
        } else {
            "OK"
        }
        return finalMessage
    }
}