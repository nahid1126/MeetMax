package com.nahid.meetmax.view_models

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nahid.meetmax.di.qualifier.SignInQualifier
import com.nahid.meetmax.model.data.SignIn
import com.nahid.meetmax.model.repository.SignInRepository
import com.nahid.meetmax.utils.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SignInViewModel"
@HiltViewModel
class SignInViewModel @Inject constructor(@SignInQualifier private val signInRepository: SignInRepository) :
    ViewModel() {
    var message = MutableSharedFlow<String>()
    var userMailFlow = MutableStateFlow<String>("")
    var userPassFlow = MutableStateFlow<String>("")
    var forgotMailFlow = MutableStateFlow<String>("")
    private var signIn: SignIn? = null

    val signInResponse = signInRepository.signInResponse

    fun signIn(onResult: (Boolean, String) -> Unit) {
        val userMail = userMailFlow.value
        val userPass = userPassFlow.value

        viewModelScope.launch {
            if (userMail.isEmpty()) {
                message.emit("Please Enter Mail")
            } else if (userMail.isNotEmpty() && !isValidEmail(userMail)) {
                message.emit("Please Enter Valid Email")
            } else if (userPass.isEmpty()) {
                message.emit("Please Enter Password")
            } else {
                val validUser = signInRepository.checkValidUser(userMail, userPass)
                if (validUser != null) {
                    onResult(true, "Success")
                    signIn = SignIn(validUser.userId!!, validUser.email, validUser.userName)
                } else {
                    onResult(false, "Email or Password is wrong")
                }
                // signInRepository.requestForSignIn(userMail, userPass) //when api is ready then uncomment this line and previous line comment or delete
            }
        }
    }

    fun setUserMail(appPreferences: AppPreferences) {
        viewModelScope.launch {
            appPreferences.putUserEmail(userMailFlow.value.trim())
        }
    }

    fun setUserData(appPreferences: AppPreferences, userInfo: SignIn? = signIn) {
        viewModelScope.launch {
            val userMail = userMailFlow.value.trim()
            appPreferences.putUserEmail(userMail)
            val loginResponseJson = Gson().toJson(userInfo)
            appPreferences.putLoginResponse(loginResponseJson)
        }
    }

    fun forgotPassword(onResult: (Boolean, String) -> Unit) {
        val userMail = forgotMailFlow.value
        viewModelScope.launch {
            if (userMail.isEmpty()) {
                message.emit("Please Enter Mail")
            } else if (userMail.isNotEmpty() && !isValidEmail(userMail)) {
                message.emit("Please Enter Valid Email")
            } else {
                val isMailExist = signInRepository.checkMailFound(userMail)
                if (isMailExist != null) {
                    onResult(true,"Forgot Password Link Send To Your Email Address Please Check Your Email")
                } else {
                    onResult(false,"Email Not Found")
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}