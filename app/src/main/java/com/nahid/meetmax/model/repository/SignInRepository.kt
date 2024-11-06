package com.nahid.meetmax.model.repository

import com.nahid.meetmax.model.data.SignIn
import com.nahid.meetmax.model.data.User
import com.nahid.meetmax.model.network.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface SignInRepository {
    val signInResponse: SharedFlow<NetworkResponse<SignIn>>
    suspend fun requestForSignIn(userMail: String, userPass: String) //those are for when api is ready

   suspend fun checkValidUser(userMail: String, userPass: String): User? //this is for checking valid user from room
   suspend fun checkMailFound(userMail: String): User?
}