package com.nahid.meetmax.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String,
    val name: String,
    val password: String,
    val dateOfBirth: String,
    val gender: String
) : Parcelable
