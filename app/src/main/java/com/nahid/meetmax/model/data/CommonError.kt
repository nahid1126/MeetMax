package com.nahid.meetmax.model.data

import com.google.gson.annotations.SerializedName

data class CommonError(
    @SerializedName("timestamp") var timestamp: String?,
    @SerializedName("status") var status: Int?,
    @SerializedName("error") var error: String?,
    @SerializedName("message") var message: String?,
    @SerializedName("errorMessage") var errorMessage: String?,
    @SerializedName("path") var path: String?
)
