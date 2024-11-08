package com.nahid.meetmax.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

object Constants {

    const val BASE_URL = "https://example.com/api/"

    const val USER_EMAIL = "user_email"
    const val USER_PASSWORD = "user_password"
    const val LOGIN_RESPONSE = "login_response"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val storagePermissionUpper12 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO
    )

    val storagePermissionBelow13 = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}