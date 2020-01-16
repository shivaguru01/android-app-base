package com.app.core.di

import android.content.Context
import android.content.SharedPreferences
import com.app.core.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelModule::class,
        ApiModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<App> {

    fun providesContext(): Context

    fun providesSharedPref() : SharedPreferences

}