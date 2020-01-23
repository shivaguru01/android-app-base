package com.app.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.app.core.App
import com.app.core.BuildConfig
import com.app.core.helper.ComponentHelper
import com.app.core.repo.local.AppDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app : App) {

    @Provides
    @Singleton
    fun providesAppContext() : Context = app;

    @Provides
    @Singleton
    fun providesSharedPref() : SharedPreferences = app.getSharedPreferences(app.packageName, Context.MODE_PRIVATE);

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun providesComponentHelper(): ComponentHelper = app

    @Provides
    @Singleton
    internal fun providesDb(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "${BuildConfig.APPLICATION_ID}.db"
        ).build()
    }

}