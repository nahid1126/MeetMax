package com.nahid.meetmax.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignIn(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("userName") val userName: String
):Parcelable