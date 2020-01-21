package com.app.core.repo.local

import android.content.SharedPreferences
import javax.inject.Inject

class LocalRepo @Inject constructor(
    sharedPreferences: SharedPreferences
) {

    companion object {
        const val ACCESS_TOKEN = "TOKEN"
        const val FORCE_CREATE_SERVICE = "FORCE_CREATE_SERVICE"
    }

    var accessToken: String? by sharedPreferences.string(ACCESS_TOKEN)

    var forceCreateService: Boolean by sharedPreferences.boolean(FORCE_CREATE_SERVICE)

}