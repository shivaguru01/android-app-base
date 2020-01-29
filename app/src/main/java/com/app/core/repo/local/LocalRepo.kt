package com.app.core.repo.local

import android.content.SharedPreferences
import android.util.Log
import androidx.paging.DataSource
import com.app.core.common.APP_TAG
import com.app.core.model.RecyclerItem
import com.app.core.model.RecyclerItemAlbum
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocalRepo @Inject constructor(
    val sharedPreferences: SharedPreferences,
    private val database: AppDatabase
) {
    companion object {
        const val ACCESS_TOKEN = "TOKEN"
        const val FORCE_CREATE_SERVICE = "FORCE_CREATE_SERVICE"
    }

    var accessToken: String? by sharedPreferences.string(ACCESS_TOKEN)
    var forceCreateService: Boolean by sharedPreferences.boolean(FORCE_CREATE_SERVICE)

    fun insertRepo(repoList: List<RecyclerItem>, onComplete: () -> Unit) {
        Completable.fromAction {
            database.repoDao().insert(repoList)
            onComplete.invoke()
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun insertAlbums(albumList: List<RecyclerItemAlbum>?, onComplete: () -> Unit) {
        Log.d(APP_TAG, "insertion method called")
        albumList?.let {
            Completable.fromAction {
                Log.d(APP_TAG, "before insertion")
                database.albumDao().insert(albums = albumList)
                Log.d(APP_TAG, "insertion success")
                onComplete.invoke()
            }.subscribeOn(Schedulers.io()).subscribe()
        }
    }

    fun searchRepo(name: String): DataSource.Factory<Int, RecyclerItem> {
        val query = "%${name.replace(' ', '%')}%"
        return database.repoDao().searchRepoByName(query)
    }

    fun getAllAlbum(): DataSource.Factory<Int, RecyclerItemAlbum> {
        return database.albumDao().loadAllAlbums()
    }

}