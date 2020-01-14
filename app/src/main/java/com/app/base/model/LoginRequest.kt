package com.grofers.firstmile.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @field:SerializedName("mobileNumber")
    val mobileNumber: String?

)