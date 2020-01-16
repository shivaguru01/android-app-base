package com.app.core.repo

import com.app.core.model.LoginRequest
import com.app.core.model.VerifyOtpRequest
import com.app.core.model.VerifyOtpResponse
import com.app.core.repo.local.LocalRepo
import com.app.core.repo.remote.RemoteRepo
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class AppRepo @Inject constructor(
    private val remoteRepo: RemoteRepo, private val localRepo: LocalRepo
) {

    fun generateOtp(loginRequest: LoginRequest): Single<Response<Void>> {
        return remoteRepo.generateOtp(loginRequest);
    }

    fun verifyOtp(verifyOtpRequest: VerifyOtpRequest): Single<Response<VerifyOtpResponse>> {
        return remoteRepo.verifyOtp(verifyOtpRequest);
    }

    fun logout(): Single<Response<Void>> {
        return remoteRepo.logout();
    }

    fun saveAccessToken(accessToken: String?) {
        this.localRepo.accessToken = accessToken
    }

    fun getAccessToken(): String? {
        return localRepo.accessToken
    }

    fun saveForceCreateService(status: Boolean) {
        localRepo.forceCreateService = status
    }

    fun isForceCreateService(): Boolean = localRepo.forceCreateService

    fun isLoggedIn(): Boolean {
        getAccessToken()?.let {
            return@isLoggedIn true
        }
        return false
    }

}