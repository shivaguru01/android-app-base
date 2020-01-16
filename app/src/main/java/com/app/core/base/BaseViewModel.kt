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
import com.app.core.event.SingleLiveEvent
import com.app.core.listener.ResponseListener
import com.app.core.model.Action
import com.app.core.model.NetworkState
import com.app.core.model.ServerException
import com.app.core.repo.AppRepo
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel(
    val appContext: Context,
    val sharedPreferences: SharedPreferences,
    val repo: AppRepo
) : ViewModel(), Observable, ResponseListener {

    protected val disposables = CompositeDisposable()

    @Bindable
    val action: SingleLiveEvent<Action> = SingleLiveEvent()

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

    override fun getContext(): Context = appContext

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
        return getContext().getString(resId)
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

    abstract fun setup(args: Bundle?)

    fun isLoggedIn(): Boolean = repo.isLoggedIn()

}