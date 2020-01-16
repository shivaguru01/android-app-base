package com.app.base.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @field:SerializedName("mobileNumber")
    val mobileNumber: String?

)