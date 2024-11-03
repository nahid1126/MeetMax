package com.nahid.meetmax.model.data

import com.google.gson.annotations.SerializedName

data class SignIn(
    @SerializedName("id") val id: Long, @SerializedName("token") val token: String
)