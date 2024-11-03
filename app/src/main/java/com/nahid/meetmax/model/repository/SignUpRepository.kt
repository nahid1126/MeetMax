package com.nahid.meetmax.model.repository

import com.nahid.meetmax.model.data.User
import com.nahid.meetmax.model.network.NetworkResponse
import kotlinx.coroutines.flow.SharedFlow

interface SignUpRepository {
    val signUpResponse: SharedFlow<NetworkResponse<Boolean>>
    suspend fun requestForSignUp(user: User)
}