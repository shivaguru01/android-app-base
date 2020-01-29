package com.app.core.repo.boundary

import android.util.Log
import com.app.core.common.APP_TAG
import com.app.core.model.RecyclerItemAlbum
import com.app.core.repo.local.LocalRepo
import com.app.core.repo.remote.RemoteRepo
import javax.inject.Inject

class AlbumBoundaryCondition
@Inject
constructor(
    override var localRepo: LocalRepo,
    override var remoteRepo: RemoteRepo,
    override var networkPageSize: Int = 50
) : BaseBoundaryCondition<RecyclerItemAlbum>() {

    private var albumId = 1

    override fun loadData() {
        Log.d(APP_TAG, "load data called")
        loadingStarted()
        remoteRepo.getAlbums(albumId, onSuccess = { list ->
            Log.d(APP_TAG, "get albums ${list?.size}")
            localRepo.insertAlbums(albumList = list, onComplete = fun() {
                Log.d(APP_TAG, "insertion successful")
                albumId++
            })
            loadingComplete()
            Log.d(APP_TAG, "load data complete ")
        }, onError = { ex ->
            Log.d(APP_TAG, "load data error ")
            postServerException(ex)
            loadingComplete()
        })
    }
}