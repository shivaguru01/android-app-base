package com.app.core.di

import com.app.core.ui.fragment.LoginOtpFragment
import com.app.core.ui.fragment.LoginPhoneFragment
import com.app.core.ui.fragment.PagedRecyclerFragment
import com.app.core.ui.fragment.SplashFragment
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

    @ContributesAndroidInjector()
    abstract fun contributesPagedRecyclerFragment(): PagedRecyclerFragment

}