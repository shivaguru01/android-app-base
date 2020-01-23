package com.app.core.repo.remote

import com.app.core.model.*
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteApiService {

    @POST("api/login")
    fun generateOtp(@Body loginRequest: LoginRequest): Single<Response<Void>>

    @POST("api/logout")
    fun logout(): Single<Response<Void>>

    @POST("api/verifyOtp")
    fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequest): Single<Response<VerifyOtpResponse>>

    @GET("search/repositories")
    fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Single<Response<RecyclerResponse>>

}
