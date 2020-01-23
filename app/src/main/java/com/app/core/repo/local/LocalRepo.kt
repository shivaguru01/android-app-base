package com.app.core.repo.local

import android.content.SharedPreferences
import androidx.paging.DataSource
import com.app.core.model.RecyclerItem
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

    fun searchRepo(name: String): DataSource.Factory<Int, RecyclerItem> {
        val query = "%${name.replace(' ', '%')}%"
        return database.repoDao().searchRepoByName(query)
    }

}