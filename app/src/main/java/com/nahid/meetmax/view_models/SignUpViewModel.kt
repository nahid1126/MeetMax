package com.nahid.meetmax.view_models

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahid.meetmax.di.qualifier.SignUpQualifier
import com.nahid.meetmax.model.data.User
import com.nahid.meetmax.model.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(@SignUpQualifier private val signUpRepository: SignUpRepository) :
    ViewModel() {
    var message = MutableSharedFlow<String>()
    var userMailFlow = MutableStateFlow<String>("")
    var userNameFlow = MutableStateFlow<String>("")
    var userPassFlow = MutableStateFlow<String>("")
    var userDateOfBirthFlow = MutableStateFlow<String>("")
    var userGenderFlow = MutableStateFlow<String>("")

    val signUpResponse = signUpRepository.signUpResponse

    fun signIn() {
        val userMail = userMailFlow.value
        val userName = userNameFlow.value
        val userPass = userPassFlow.value
        val userDateOfBirth = userDateOfBirthFlow.value
        val userGender = userGenderFlow.value

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
            } else if (userGender.isEmpty()) {
                message.emit("Please Select Gender")
            } else {
                signUpRepository.requestForSignUp(
                    User(
                        userMail,
                        userName,
                        userPass,
                        userDateOfBirth,
                        userGender
                    )
                )
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(userPass: String): String {
        val passwordInput: String = userPass.trim()

        val finalMessage = if (!passwordInput.matches(".*[0-9].*".toRegex())) {
            "Password should contain at least 1 digit"
        } else if (!passwordInput.matches(".*[a-z].*".toRegex())) {
            "Password should contain at least 1 lower case letter"
        } else if (!passwordInput.matches(".*[A-Z].*".toRegex())) {
            "Password should contain at least 1 upper case letter"
        } else if (!passwordInput.matches(".*[a-zA-Z].*".toRegex())) {
            "Password should contain a letter"
        } else if (!passwordInput.matches(".{8,}".toRegex())) {
            "Password should contain 8 characters"
        } else {
            "OK"
        }
        return finalMessage
    }
}