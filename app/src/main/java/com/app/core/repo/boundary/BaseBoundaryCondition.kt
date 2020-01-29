package com.app.core.repo.boundary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.app.core.model.ServerException
import com.app.core.repo.local.LocalRepo
import com.app.core.repo.remote.RemoteRepo

abstract class BaseBoundaryCondition<T> : PagedList.BoundaryCallback<T>() {

    abstract var localRepo: LocalRepo
    abstract var remoteRepo: RemoteRepo
    abstract var networkPageSize: Int

    protected val serverException = MutableLiveData<ServerException>()
    private var isRequestInProgress = false

    val exception: LiveData<ServerException>
        get() = serverException

    override fun onZeroItemsLoaded() = requestAndSaveData()

    override fun onItemAtEndLoaded(itemAtEnd: T) = requestAndSaveData()

    private fun requestAndSaveData() {
        if (isLoading()) {
            return
        }
        else{
            loadData()
        }
    }

    abstract fun loadData()

    protected open fun loadingStarted() {
        isRequestInProgress = true
    }

    protected open fun loadingComplete() {
        isRequestInProgress = false
    }

    private fun isLoading(): Boolean = isRequestInProgress

    protected open fun postServerException(ex: ServerException) {
        serverException.value = ex
    }

}