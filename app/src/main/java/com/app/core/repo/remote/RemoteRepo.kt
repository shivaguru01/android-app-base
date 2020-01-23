package com.app.core.repo.remote

import com.app.core.helper.ComponentHelper
import com.app.core.model.*
import com.app.core.network.RestObserver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class RemoteRepo @Inject constructor(
    private val apiService: RemoteApiService,
    private val componentHelper: ComponentHelper

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

    fun searchRepos(
        query: String,
        page: Int,
        itemsPerPage: Int,
        onSuccess: (repos: List<RecyclerItem>?) -> Unit,
        onError: (serverException: ServerException) -> Unit
    ) {
        // why is disposable not used
        // side effects of not adding here
        apiService.searchRepos(query, page, itemsPerPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : RestObserver<RecyclerResponse>(componentHelper) {
                override fun error(serverException: ServerException) {
                    onError(serverException)
                }

                override fun success(data: RecyclerResponse?) {
                    onSuccess(data?.items)
                }
            })
    }


}