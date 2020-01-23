package com.app.core.di

import com.app.core.ui.HomeActivity
import com.app.core.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun contributesMainActivity(): MainActivity

    @ContributesAndroidInjector()
    abstract fun contributesHomeActivity(): HomeActivity

}