package com.app.base.repo.remote

import com.app.base.model.VerifyOtpResponse
import com.grofers.firstmile.model.*
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
