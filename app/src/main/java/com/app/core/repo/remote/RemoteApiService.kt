package com.app.core.repo.remote

import com.app.core.model.LoginRequest
import com.app.core.model.VerifyOtpRequest
import com.app.core.model.VerifyOtpResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RemoteApiService {

    @POST("api/login")
    fun generateOtp(@Body loginRequest: LoginRequest): Single<Response<Void>>

    @POST("api/logout")
    fun logout(): Single<Response<Void>>

    @POST("api/verifyOtp")
    fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequest): Single<Response<VerifyOtpResponse>>

}
