package com.app.core.network

import com.app.core.repo.local.LocalRepo
import com.readystatesoftware.chuck.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


/**
 *   we cannot use appRepo to get access token as it will create circular dependency
 */
class HttpRequestInterceptor @Inject constructor(private val localRepo: LocalRepo) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var reqBuilder = request.newBuilder().apply {
            addHeader("appVersion", BuildConfig.VERSION_CODE.toString())
            addHeader("tokenType", "APP")
            if (localRepo.accessToken != null) {
                addHeader("token", localRepo.accessToken!!)
            }
        }
        return chain.proceed(reqBuilder.build())
    }

}