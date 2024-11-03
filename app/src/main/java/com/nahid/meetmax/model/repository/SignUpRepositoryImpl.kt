package com.nahid.meetmax.model.repository

import com.nahid.meetmax.model.data.User
import com.nahid.meetmax.model.network.APIInterface
import com.nahid.meetmax.model.network.NetworkResponse
import com.nahid.meetmax.utils.ResponseUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(private val apiInterface: APIInterface) :
    SignUpRepository {
    private val _signUpResponse = MutableSharedFlow<NetworkResponse<Boolean>>()
    override val signUpResponse: SharedFlow<NetworkResponse<Boolean>>
        get() = _signUpResponse.asSharedFlow()

    override suspend fun requestForSignUp(user: User) {
        _signUpResponse.emit(NetworkResponse.Loading())
        try {
            val response = apiInterface.requestSignUp(user)

            when (response.code()) {
                in 200..299 -> {
                    val data = response.body()
                    _signUpResponse.emit(NetworkResponse.Success(data = data!!))
                }

                401 -> {
                    _signUpResponse.emit(NetworkResponse.Error("Unauthorized"))
                }

                400 -> {
                    _signUpResponse.emit(NetworkResponse.Error("Bad Request"))
                }

                404 -> {
                    _signUpResponse.emit(NetworkResponse.Error("No Data Found"))
                }

                408 -> {
                    _signUpResponse.emit(NetworkResponse.Error("Request Timeout"))
                }

                409 -> {
                    _signUpResponse.emit(NetworkResponse.Error("Conflict"))
                }

                413 -> {
                    _signUpResponse.emit(NetworkResponse.Error("Payload Too Large"))
                }

                in 500..599 -> {
                    _signUpResponse.emit(NetworkResponse.Error("Internal Server Error"))
                }

                else -> {
                    _signUpResponse.emit(
                        NetworkResponse.Error(
                            ResponseUtils.getErrorMessage(
                                response
                            )
                        )
                    )
                }
            }

        } catch (e: UnresolvedAddressException) {
            _signUpResponse.emit(NetworkResponse.Error("No Internet Connection"))
        } catch (e: Exception) {
            _signUpResponse.emit(NetworkResponse.Error("Exception: ${e.message}"))
        }
    }
}