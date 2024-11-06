package com.nahid.meetmax.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val email: String,
    val name: String,
    val password: String,
    val dateOfBirth: String,
    val gender: String
) : Parcelable
