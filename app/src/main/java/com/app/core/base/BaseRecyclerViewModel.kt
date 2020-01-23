package com.app.core.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.app.core.event.SingleLiveEvent
import com.app.core.model.Action
import com.app.core.repo.AppRepo

abstract class BaseRecyclerViewModel<T>(
    appContext: Context,
    sharedPreferences: SharedPreferences,
    appRepo: AppRepo
) :
    BaseViewModel(appContext, sharedPreferences, appRepo) {

    companion object {
        const val NO_ITEMS = 5000
    }

    private val listItem = MutableLiveData<List<T>>()
    private val selectedItem = SingleLiveEvent<T>()

    fun getListItem(): MutableLiveData<List<T>> {
        return listItem
    }

    fun getSelectedItem(): MutableLiveData<T> {
        return selectedItem
    }

    fun onItemClick(index: Int?) {
        selectedItem.postValue(getItem(index))
    }

    private fun postNoItemsEvent() {
        action.value = Action(NO_ITEMS)
    }

    abstract fun loadContent(args: Bundle?)

    protected fun getItem(index: Int?): T? {
        return if (getListItem().value != null && index != null &&
            getListItem().value!!.size > index
        ) {
            getListItem().value!![index]
        } else {
            null
        }
    }

    protected fun postItemList(itemList: List<T>?) {
        getListItem().postValue(itemList)
    }

    protected fun isContentLoaded(): Boolean {
        if (getListItem().value != null && getListItem().value!!.isNotEmpty()) {
            return true
        }
        return false;
    }


}