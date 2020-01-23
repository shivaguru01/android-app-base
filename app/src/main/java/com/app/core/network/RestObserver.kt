package com.app.core.network

import com.app.core.helper.ComponentHelper
import com.app.core.model.ServerException
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_OK

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
        return try {
            // later add flag to build_config
            if(true) {
                ServerException(httpErrorCode = response.code(), message = response.message() )
            }  else{
                return componentHelper.getGson().fromJson(
                    response.errorBody()?.charStream(),
                    ServerException::class.java
                )
            }
        } catch (ex: Exception) {
            ServerException(message = "server communication error", errorCode = 500, httpErrorCode = 500)
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