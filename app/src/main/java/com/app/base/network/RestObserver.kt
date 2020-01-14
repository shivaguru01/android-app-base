package com.grofers.retail.warehouse.android.network

import android.util.Log
import com.app.base.common.APP_TAG
import com.app.base.common.Utils
import com.app.base.helper.ComponentHelper
import com.app.base.model.ServerException
import com.grofers.retail.warehouse.android.listeners.ResponseListener
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.Response
import java.lang.ref.WeakReference
import java.net.HttpURLConnection.HTTP_OK

abstract class RestObserver<R>(responseListener: ResponseListener) : DisposableSingleObserver<Response<R>>() {

    private val networkListenerWeakReference: WeakReference<ResponseListener> = WeakReference(responseListener)

    private var componentHelper: ComponentHelper?

    protected abstract fun error(serverException: ServerException)

    protected abstract fun success(data: R?)

    init {
        this.componentHelper = Utils.getComponentHelper(networkListenerWeakReference.get()!!.getContext())
    }

    /**
     *  http exceptions
     */
    override fun onError(throwable: Throwable) {
        error(ServerException(message = throwable.message, throwable = throwable));
    }

    override fun onSuccess(response: Response<R>) {
        handleSuccess(response)
    }

    private fun getErrorMessage(response: Response<R>): ServerException {
        var serverException: ServerException? = null
        try {
            serverException =
                componentHelper?.getGson()!!.fromJson(
                    response.errorBody()!!.charStream(),
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