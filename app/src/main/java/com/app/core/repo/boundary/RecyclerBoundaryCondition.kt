package com.app.core.repo.boundary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.app.core.model.RecyclerItem
import com.app.core.model.ServerException
import com.app.core.repo.local.LocalRepo
import com.app.core.repo.remote.RemoteRepo
import javax.inject.Inject

class RecyclerBoundaryCondition
@Inject
constructor(
    private val query: String,
    private val localRepo: LocalRepo,
    private val remoteRepo: RemoteRepo
) : PagedList.BoundaryCallback<RecyclerItem>() {

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    private val serverException = MutableLiveData<ServerException>()
    private var isRequestInProgress = false
    private var pageNumber = 1
    val exception: LiveData<ServerException>
        get() = serverException

    override fun onZeroItemsLoaded() {
        requestAndSaveData(query)
    }

    override fun onItemAtFrontLoaded(itemAtFront: RecyclerItem) {
    }

    override fun onItemAtEndLoaded(itemAtEnd: RecyclerItem) {
        requestAndSaveData(query)
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        remoteRepo.searchRepos(query, pageNumber, NETWORK_PAGE_SIZE, onSuccess = { list ->
            list?.let {
                localRepo.insertRepo(repoList = list, onComplete = fun() {
                    pageNumber++
                })
            }
            isRequestInProgress = false
        }, onError = { ex ->
            serverException.value = ex
            isRequestInProgress = false
        })
    }

}