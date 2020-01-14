package com.app.base.model

import com.google.gson.annotations.SerializedName

data class ServerException(

    @field:SerializedName("message")
    override val message: String?,

    @field:SerializedName("status")
    val status: String? = "",

    @field:SerializedName("errorCode")
    val errorCode: Int? = 0,

    @field:SerializedName("httpErrorcode")
    val httpErrorCode: Int? = DEFAULT_ERROR_CODE,

    val throwable: Throwable? = null

) : Exception() {
    companion object {
        const val DEFAULT_ERROR_CODE = -1
    }
}