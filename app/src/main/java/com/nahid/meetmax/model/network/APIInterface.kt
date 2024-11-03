package com.nahid.meetmax.model.network

import com.nahid.meetmax.model.data.SignIn
import com.nahid.meetmax.model.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIInterface {
    @GET("userSign")
    suspend fun requestSignIn(
        @Query("email") userMail: String,
        @Query("password") userPass: String
    ): Response<SignIn>

    @POST("userSignUP")
    suspend fun requestSignUp(
        @Body user: User
    ): Response<Boolean>

}