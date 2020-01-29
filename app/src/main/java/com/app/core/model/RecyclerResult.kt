package com.app.core.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class RecyclerResult<T>(
    val data: LiveData<PagedList<T?>>,
    val serverException: LiveData<ServerException>
)