package com.app.core.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class RecyclerResult(
    val data: LiveData<PagedList<RecyclerItem>>,
    val serverException: LiveData<ServerException>
)