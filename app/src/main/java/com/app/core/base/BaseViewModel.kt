package com.app.core.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.ViewModel
import com.app.core.BR
import com.app.core.R
import com.app.core.common.Utils
import com.app.core.event.SingleLiveEvent
import com.app.core.helper.ComponentHelper
import com.app.core.model.Action
import com.app.core.model.NetworkState
import com.app.core.model.ServerException
import com.app.core.repo.AppRepo
import com.bumptech.glide.util.Util
import io.reactivex.disposables.CompositeDisposable
import okio.Utf8


abstract class BaseViewModel(
    val appContext: Context,
    val sharedPreferences: SharedPreferences,
    val repo: AppRepo
) : ViewModel(), Observable {

    protected val disposables = CompositeDisposable()

    @Bindable
    public val action: SingleLiveEvent<Action> = SingleLiveEvent()

    @Bindable
    val networkState: SingleLiveEvent<NetworkState?> = SingleLiveEvent()

    private val callbacks = PropertyChangeRegistry()

    protected var loaderMsg: String? = null

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    private fun notifyNetworkStatePropertyChanged() {
        notifyPropertyChanged(BR.networkState)
    }

    protected fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

    protected fun setLoading(loaderMsg: String? = null) {
        this.loaderMsg = loaderMsg
        networkState.value = NetworkState.LOADING
        notifyNetworkStatePropertyChanged()
    }

    protected fun setLoadingSuccess() {
        networkState.value = NetworkState.LOADED
        notifyNetworkStatePropertyChanged()
    }

    fun getLoadingMessage(): String {
        return loaderMsg ?: getString(R.string.loading)
    }

    protected fun getString(resId: Int): String {
        return appContext.getString(resId)
    }

    protected fun setLoadingFailed(serverException: ServerException) {
        action.value = Action(serverException = serverException)
        networkState.value = NetworkState.ERROR
        notifyNetworkStatePropertyChanged()
    }

    protected fun sendEvent(action: Action) {
        this.action.value = action
    }

    public override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    protected fun getComponentHelper() = Utils.getComponentHelper(appContext)!!

    abstract fun setup(args: Bundle?)

    fun isLoggedIn(): Boolean = repo.isLoggedIn()

}