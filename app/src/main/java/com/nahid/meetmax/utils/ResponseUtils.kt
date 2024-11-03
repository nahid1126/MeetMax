package com.nahid.meetmax.utils

import com.google.gson.Gson
import com.nahid.meetmax.model.data.CommonError
import retrofit2.Response
import java.lang.Exception

object ResponseUtils {
    fun getErrorMessage(response: Response<*>): String {
        return try {
            val error: CommonError =
                Gson().fromJson(response.errorBody()!!.charStream(), CommonError::class.java)
            if (error.message != null) {
                error.message.toString()
            } else {
                error.errorMessage.toString()
            }
        } catch (e: Exception) {
            e.message.toString()
        }
    }
}