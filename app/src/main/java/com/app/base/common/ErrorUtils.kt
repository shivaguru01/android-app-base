package com.app.base.common

import android.content.Context
import android.content.SharedPreferences
import com.app.base.R
import com.app.base.common.Utils.getStringFromResId
import com.app.base.model.ServerException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorUtils {

    fun handleError(context: Context, sharedPreferences: SharedPreferences, serverException: ServerException?) {
        val errorMessage = when (serverException?.httpErrorCode) {
            // authentication error
            403, 401 -> {
                Utils.logoutApplications(context, sharedPreferences)
                getStringFromResId(context, R.string.log_out_msg)
            }
            // other server errors
            500 -> {
                serverException.message ?: getStringFromResId(context, R.string.server_error)
            }
            400 -> {
                serverException.message ?: getStringFromResId(context, R.string.bad_request)
            }
            ServerException.DEFAULT_ERROR_CODE -> {
                // if its http error, lets check the throwable class to find
                // out if its internet exception
                // if its not internet error, we will throw a generic msg saying "unable to connect
                // to server"
                when (serverException.throwable) {
                    is UnknownHostException, is SocketTimeoutException, is IOException -> {
                        getStringFromResId(context, R.string.internet_error)
                    }
                    else -> {
                        getStringFromResId(context, R.string.connection_error)
                    }
                }
            }
            else -> {
                serverException?.message
            }
        }
        Utils.showToast(context, errorMessage)
    }

}