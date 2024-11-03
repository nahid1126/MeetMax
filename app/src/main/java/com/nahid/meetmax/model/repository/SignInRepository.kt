package com.nahid.meetmax.model.repository

import com.nahid.meetmax.model.data.SignIn
import com.nahid.meetmax.model.network.NetworkResponse
import kotlinx.coroutines.flow.SharedFlow

interface SignInRepository {
    val signInResponse: SharedFlow<NetworkResponse<SignIn>>
    suspend fun requestForSignIn(userMail: String, userPass: String)
}