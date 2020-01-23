package com.app.core.network

import android.util.Log
import com.app.core.common.APP_TAG
import com.app.core.common.Utils
import com.app.core.helper.ComponentHelper
import com.app.core.model.ServerException
import com.app.core.listener.ResponseListener
import com.google.gson.Gson
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.Response
import java.lang.ref.WeakReference
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject

abstract class RestObserver<R>(private val componentHelper: ComponentHelper) : DisposableSingleObserver<Response<R>>() {

    protected abstract fun error(serverException: ServerException)
    protected abstract fun success(data: R?)

    override fun onError(throwable: Throwable) {
        error(ServerException(message = throwable.message, throwable = throwable));
    }

    override fun onSuccess(response: Response<R>) {
        handleSuccess(response)
    }

    private fun getErrorMessage(response: Response<R>): ServerException {
        var serverException: ServerException?
        try {
            serverException =
                componentHelper.getGson().fromJson(
                    response.errorBody()?.charStream(),
                    ServerException::class.java
                )
            return serverException
        } catch (ex: Exception) {
            Log.e(APP_TAG, ex.message)
            return ServerException("unable to read server response", errorCode = 500, status = "500")
        }
    }

    private fun handleSuccess(response: Response<R>) {
        if (response.code() in HTTP_OK..210) {
            success(response.body())
        } else {
            val serverException = getErrorMessage(response)
            error(serverException)
        }
    }

}