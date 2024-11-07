package com.nahid.meetmax.view_models

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahid.meetmax.R
import com.nahid.meetmax.di.qualifier.SignUpQualifier
import com.nahid.meetmax.model.data.User
import com.nahid.meetmax.model.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SignUpViewModel"
@HiltViewModel
class SignUpViewModel @Inject constructor(@SignUpQualifier private val signUpRepository: SignUpRepository) :
    ViewModel() {
    init {
        insertUsers()
    }
    var message = MutableSharedFlow<String>()
    var userMailFlow = MutableStateFlow<String>("")
    var userNameFlow = MutableStateFlow<String>("")
    var userPassFlow = MutableStateFlow<String>("")
    var userDateOfBirthFlow = MutableStateFlow<String>("")
    val selectedOption = MutableStateFlow<Int>(0)

    val signUpResponse = signUpRepository.signUpResponse

    fun signUp(onResult: (Boolean, String) -> Unit) {
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
            } else if (userPass.isNotEmpty() && validatePassword(userPass) != null
            ) {
                validatePassword(userPass)?.let { message.emit(it) }
            } else if (userDateOfBirth.isEmpty()) {
                message.emit("Please Enter Date Of Birth")
            } else if (genderSelect.isEmpty()) {
                message.emit("Please Select Gender")
            } else {
                val user = User(
                    email = userMail,
                    userName = userName,
                    password = userPass,
                    dateOfBirth = userDateOfBirth,
                    gender = genderSelect
                )
                /*signUpRepository.requestForSignUp(user)
                when api is ready then uncomment this line and and remove callback
                */
                val isUserExist = signUpRepository.checkUserExists(userMail)
                if (isUserExist != null) {
                    onResult(false, "Email Already Exist")
                } else {
                    onResult(true, "Success Now Sign In")
                    signUpRepository.insertUser(user)
                }
            }
        }
    }

    private fun insertUsers() {
        viewModelScope.launch(IO) {
            val users = listOf(
                User(
                    userId = 1,
                    email = "example1@gmail.com",
                    userName = "Example1",
                    password = "password1",
                    dateOfBirth = "1990-01-01",
                    gender = "Male"
                ),
                User(
                    userId = 2,
                    email = "example2@gmail.com",
                    userName = "Example2",
                    password = "password2",
                    dateOfBirth = "1998-01-01",
                    gender = "Male"
                ),
                User(
                    userId = 3,
                    email = "example3@gmail.com",
                    userName = "Example3",
                    password = "password3",
                    dateOfBirth = "1996-01-01",
                    gender = "Female"
                )
            )
            signUpRepository.insertUsers(users)
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

    private fun validatePassword(userPass: String): String? {
        val passwordInput = userPass.trim()

        return when {
            !passwordInput.matches(".{8,}".toRegex()) -> "Password should contain at least 8 characters"
            !passwordInput.matches(".*[0-9].*".toRegex()) -> "Password should contain at least 1 digit"
            !passwordInput.matches(".*[a-z].*".toRegex()) -> "Password should contain at least 1 lower case letter"
            !passwordInput.matches(".*[A-Z].*".toRegex()) -> "Password should contain at least 1 upper case letter"
            !passwordInput.matches(".*[a-zA-Z].*".toRegex()) -> "Password should contain a letter"
            else -> null // Valid password
        }
    }
}