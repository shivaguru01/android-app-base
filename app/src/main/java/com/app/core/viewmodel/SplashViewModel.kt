package com.app.core.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.app.core.base.BaseViewModel
import com.app.core.repo.AppRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashViewModel @Inject
constructor(appContext: Context, sharedPreferences: SharedPreferences, private val appRepo: AppRepo) :
    BaseViewModel(appContext, sharedPreferences, appRepo) {

    companion object {
    }

    override fun setup(args: Bundle?) {
        downloadPrerequisiteData();
    }

    private fun downloadPrerequisiteData() {

    }

}