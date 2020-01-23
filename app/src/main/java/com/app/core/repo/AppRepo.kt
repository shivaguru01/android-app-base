package com.app.core.repo

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.core.common.APP_TAG
import com.app.core.helper.ComponentHelper
import com.app.core.model.*
import com.app.core.network.RestObserver
import com.app.core.repo.boundary.RecyclerBoundaryCondition
import com.app.core.repo.local.LocalRepo
import com.app.core.repo.remote.RemoteRepo
import com.app.core.viewmodel.LoginViewModel
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class AppRepo @Inject constructor(
    private val remoteRepo: RemoteRepo,
    private val localRepo: LocalRepo
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

    fun searchRepos(
        query: String
    ): RecyclerResult {
        Log.d(APP_TAG, "New query: $query")
        val dataSourceFactory = localRepo.searchRepo(query)
        val boundaryCallback = RecyclerBoundaryCondition(query, localRepo, remoteRepo)
        val data = LivePagedListBuilder(dataSourceFactory,
            PagedList.Config.Builder().setPageSize(20).setEnablePlaceholders(true).setPrefetchDistance(20).build()).build()
        return RecyclerResult(data, boundaryCallback.exception)
    }


}