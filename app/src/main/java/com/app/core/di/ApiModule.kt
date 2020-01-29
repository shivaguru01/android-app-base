package com.app.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.app.core.BuildConfig
import com.app.core.helper.ComponentHelper
import com.app.core.network.HttpRequestInterceptor
import com.app.core.repo.AppRepo
import com.app.core.repo.local.AppDatabase
import com.app.core.repo.local.LocalRepo
import com.app.core.repo.remote.RemoteApiService
import com.app.core.repo.remote.RemoteRepo
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
class ApiModule {

    private var appRepo: AppRepo? = null

    @Provides
    internal fun provideOkHttpClient(
        context: Context, httpRequestInterceptor: HttpRequestInterceptor
    ): OkHttpClient {
        OkHttpClient.Builder().run {
            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(httpLoggingInterceptor)
                addInterceptor(ChuckInterceptor(context))
            }
            addInterceptor(httpRequestInterceptor)
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }.also {
            return@provideOkHttpClient it.build()
        }
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
    internal fun providesLocalRepo(sharedPreferences: SharedPreferences, appDatabase: AppDatabase): LocalRepo {
        return LocalRepo(sharedPreferences = sharedPreferences, database = appDatabase)
    }

    @Provides
    internal fun providesRemoteRepo(apiService: RemoteApiService, componentHelper: ComponentHelper): RemoteRepo {
        return RemoteRepo(apiService = apiService, componentHelper = componentHelper)
    }


}