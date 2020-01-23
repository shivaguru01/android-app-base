package com.app.core.model

import com.google.gson.annotations.SerializedName

data class RecyclerResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<RecyclerItem> = emptyList(),
    val nextPage: Int? = null
)