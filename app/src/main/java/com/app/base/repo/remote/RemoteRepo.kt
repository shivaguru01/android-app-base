package com.app.base.repo.remote

import com.app.base.model.VerifyOtpResponse
import com.grofers.firstmile.model.LoginRequest
import com.grofers.firstmile.model.VerifyOtpRequest
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