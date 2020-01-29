package com.app.core.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.app.core.base.BaseViewModel
import com.app.core.model.RecyclerItemAlbum
import com.app.core.model.RecyclerResult
import com.app.core.model.ServerException
import com.app.core.repo.AppRepo
import javax.inject.Inject

class AlbumViewModel @Inject
constructor(appContext: Context, sharedPreferences: SharedPreferences, private val appRepo: AppRepo) :
    BaseViewModel(appContext, sharedPreferences, appRepo) {

    private val repoResult: MutableLiveData<RecyclerResult<RecyclerItemAlbum>> = MutableLiveData()

    val repos: LiveData<PagedList<RecyclerItemAlbum?>> = Transformations.switchMap(repoResult) { repoResult ->
        repoResult.data
    }

    val networkErrors: LiveData<ServerException> = Transformations.switchMap(repoResult) { repoResult ->
        repoResult.serverException
    }

    override fun setup(args: Bundle?) {
        repoResult.value = appRepo.getAllAlbums()
    }

}