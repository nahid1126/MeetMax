package com.nahid.meetmax.model.network

sealed class NetworkResponse<T>(val data:T? = null, val message:String? = null){
    class Empty<T>: NetworkResponse<T>()
    class Loading<T>: NetworkResponse<T>()
    class Error<T>(message: String): NetworkResponse<T>(message = message)
    class Success<T>(data: T): NetworkResponse<T>(data = data)
}
