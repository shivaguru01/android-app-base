package com.grofers.firstmile.di

import android.content.Context
import android.content.SharedPreferences
import com.app.base.BuildConfig
import com.app.base.network.HttpRequestInterceptor
import com.app.base.repo.AppRepo
import com.app.base.repo.local.LocalRepo
import com.app.base.repo.remote.RemoteApiService
import com.app.base.repo.remote.RemoteRepo
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class ApiModule() {

    private var appRepo: AppRepo? = null

    @Provides
    internal fun provideOkHttpClient(
        context: Context, httpRequestInterceptor: HttpRequestInterceptor
    ): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(httpLoggingInterceptor)
            httpClient.addInterceptor(ChuckInterceptor(context))
        }
        httpClient.addInterceptor(httpRequestInterceptor)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        httpClient.writeTimeout(30, TimeUnit.SECONDS)
        return httpClient.build()
    }

    @Provides
    internal fun provideHeaderInterceptor(localRepo: LocalRepo): Interceptor =
        HttpRequestInterceptor(localRepo)

    @Provides
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.SERVER_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    internal fun provideApiService(retrofit: Retrofit): RemoteApiService {
        return retrofit.create(RemoteApiService::class.java)
    }

    @Provides
    internal fun providesRepo(remoteRepo: RemoteRepo, localRepo: LocalRepo): AppRepo {
        when (appRepo?.isForceCreateService()) {
            null, true -> {
                appRepo?.saveForceCreateService(false)
                appRepo = AppRepo(remoteRepo, localRepo)
            }
        }
        return appRepo!!
    }

    @Provides
    internal fun providesLocalRepo(sharedPreferences: SharedPreferences): LocalRepo {
        return LocalRepo(sharedPreferences = sharedPreferences)
    }

    @Provides
    internal fun providesRemoteRepo(apiService: RemoteApiService): RemoteRepo {
        return RemoteRepo(apiService = apiService)
    }

}