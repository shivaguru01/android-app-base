package com.app.core.model

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(

    @field:SerializedName("mobileNumber")
    val mobileNumber: String?,

    @field:SerializedName("verificationCode")
    val verificationCode: String?

)