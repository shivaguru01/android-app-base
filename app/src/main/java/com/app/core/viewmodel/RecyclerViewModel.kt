package com.app.core.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.app.core.base.BaseViewModel
import com.app.core.model.RecyclerItem
import com.app.core.model.RecyclerResult
import com.app.core.model.ServerException
import com.app.core.repo.AppRepo
import javax.inject.Inject

class RecyclerViewModel @Inject
constructor(appContext: Context, sharedPreferences: SharedPreferences, private val appRepo: AppRepo) :
    BaseViewModel(appContext, sharedPreferences, appRepo) {

    private val queryLiveData = MutableLiveData<String>()
    private val repoResult: LiveData<RecyclerResult> = Transformations.map(queryLiveData, appRepo::searchRepos)

    val repos: LiveData<PagedList<RecyclerItem>> = Transformations.switchMap(repoResult) { repoResult ->
        repoResult.data
    }

    val networkErrors: LiveData<ServerException> = Transformations.switchMap(repoResult) { repoResult ->
        repoResult.serverException
    }

    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    override fun setup(args: Bundle?) {

    }

}