package com.nahid.meetmax.model.repository

import com.nahid.meetmax.model.data.SignIn
import com.nahid.meetmax.model.network.APIInterface
import com.nahid.meetmax.model.network.NetworkResponse
import com.nahid.meetmax.utils.ResponseUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(private val apiInterface: APIInterface) :
    SignInRepository {

    private val _signInResponse = MutableSharedFlow<NetworkResponse<SignIn>>()
    override val signInResponse: SharedFlow<NetworkResponse<SignIn>>
        get() = _signInResponse.asSharedFlow()

    override suspend fun requestForSignIn(userMail: String, userPass: String) {
        _signInResponse.emit(NetworkResponse.Loading())
        try {
            val response = apiInterface.requestSignIn(userMail, userPass)

            when (response.code()) {
                in 200..299 -> {
                    val data = response.body()
                    _signInResponse.emit(NetworkResponse.Success(data = data!!))
                }

                401 -> {
                    _signInResponse.emit(NetworkResponse.Error("Unauthorized"))
                }

                400 -> {
                    _signInResponse.emit(NetworkResponse.Error("Bad Request"))
                }

                404 -> {
                    _signInResponse.emit(NetworkResponse.Error("No Data Found"))
                }

                408 -> {
                    _signInResponse.emit(NetworkResponse.Error("Request Timeout"))
                }

                409 -> {
                    _signInResponse.emit(NetworkResponse.Error("Conflict"))
                }

                413 -> {
                    _signInResponse.emit(NetworkResponse.Error("Payload Too Large"))
                }

                in 500..599 -> {
                    _signInResponse.emit(NetworkResponse.Error("Internal Server Error"))
                }

                else -> {
                    _signInResponse.emit(
                        NetworkResponse.Error(
                            ResponseUtils.getErrorMessage(
                                response
                            )
                        )
                    )
                }
            }

        } catch (e: UnresolvedAddressException) {
            _signInResponse.emit(NetworkResponse.Error("No Internet Connection"))
        } catch (e: Exception) {
            _signInResponse.emit(NetworkResponse.Error("Exception: ${e.message}"))
        }
    }
}