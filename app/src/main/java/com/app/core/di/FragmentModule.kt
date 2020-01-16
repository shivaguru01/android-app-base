package com.app.core.di

import com.app.core.ui.LoginOtpFragment
import com.app.core.ui.LoginPhoneFragment
import com.app.core.ui.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector()
    abstract fun contributesSplashFragment(): SplashFragment

    @ContributesAndroidInjector()
    abstract fun contributesLoginPhoneFragment(): LoginPhoneFragment

    @ContributesAndroidInjector()
    abstract fun contributesLoginOtpFragment(): LoginOtpFragment

}