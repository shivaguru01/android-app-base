package com.app.core.model

import com.google.gson.annotations.SerializedName

data class RecyclerResponse<T>(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<T> = emptyList(),
    val nextPage: Int? = null
)