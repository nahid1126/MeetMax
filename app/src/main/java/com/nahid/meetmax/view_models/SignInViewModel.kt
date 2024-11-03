package com.nahid.meetmax.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahid.meetmax.di.qualifier.SignInQualifier
import com.nahid.meetmax.model.repository.SignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SignInViewModel @Inject constructor(@SignInQualifier private val signInRepository: SignInRepository) :
    ViewModel() {
    var message = MutableSharedFlow<String>()
    var userMailFlow = MutableStateFlow<String>("")
    var userPassFlow = MutableStateFlow<String>("")

    val signInResponse = signInRepository.signInResponse

    fun signIn() {
        val userMail = userMailFlow.value
        val userPass = userPassFlow.value

        viewModelScope.launch {
            if (userMail.isEmpty()) {
                message.emit("Please Enter Mail")
            } else if (userPass.isEmpty()) {
                message.emit("Please Enter Password")
            } else {
                signInRepository.requestForSignIn(userMail, userPass)
            }
        }
    }
}