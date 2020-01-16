package com.app.core.di

import com.app.core.ui.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector()
    abstract fun contributesSplashFragment(): SplashFragment

}