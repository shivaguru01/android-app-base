package com.app.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app : Application) {

    @Provides
    @Singleton
    fun providesAppContext() : Context = app;

    @Provides
    @Singleton
    fun providesSharedPref() : SharedPreferences = app.getSharedPreferences(app.packageName, Context.MODE_PRIVATE);

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

}