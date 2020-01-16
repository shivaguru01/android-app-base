package com.app.core.repo.remote

import com.app.core.model.VerifyOtpResponse
import com.app.core.model.LoginRequest
import com.app.core.model.VerifyOtpRequest
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class RemoteRepo @Inject constructor(
    private val apiService: RemoteApiService
) {

    fun generateOtp(loginRequest: LoginRequest): Single<Response<Void>> {
        return apiService.generateOtp(loginRequest);
    }

    fun verifyOtp(verifyOtpRequest: VerifyOtpRequest): Single<Response<VerifyOtpResponse>> {
        return apiService.verifyOtp(verifyOtpRequest);
    }

    fun logout(): Single<Response<Void>> {
        return apiService.logout();
    }

}