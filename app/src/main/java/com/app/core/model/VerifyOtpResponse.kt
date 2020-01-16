package com.app.core.model
import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(

    @field:SerializedName("userInfo")
    val userInfo: UserInfo?,

    @field:SerializedName("token")
    val token: String?,

    @field:SerializedName("tokenType")
    val tokenType: String?
)