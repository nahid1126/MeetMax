package com.nahid.meetmax.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val PREFERENCE_TITLE = "AppPreferences"

class AppPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun putLoginResponse(json: String) {
        editor.putString(Constants.SIGN_IN_RESPONSE, json)
        editor.apply()
    }

    fun getLoginResponse(): String? {
        return preferences.getString(Constants.SIGN_IN_RESPONSE, null)
    }
}
