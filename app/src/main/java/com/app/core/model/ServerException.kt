package com.app.core.model

import com.google.gson.annotations.SerializedName

data class ServerException(

    @field:SerializedName("message")
    override val message: String? = "",

    @field:SerializedName("errorCode")
    val errorCode: Int? = 0,

    @field:SerializedName("httpErrorCode")
    var httpErrorCode: Int? = UNABLE_MAKE_NETWORK_CALLS,

    val throwable: Throwable? = null

) : Exception() {
    companion object {
        const val UNABLE_MAKE_NETWORK_CALLS = -1
    }
}