package com.app.core

import android.content.Context
import com.app.core.di.AppComponent
import com.app.core.di.AppModule
import com.app.core.di.DaggerAppComponent
import com.app.core.helper.ComponentHelper
import com.google.gson.Gson
import dagger.android.DaggerApplication
import javax.inject.Inject

class App : DaggerApplication(), ComponentHelper {

    @Inject
    lateinit var appGson: Gson

    @Inject
    lateinit var context : Context

    override fun getGson(): Gson = appGson

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun applicationInjector() = appComponent;

}