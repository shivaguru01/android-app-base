package com.app.core.model

import com.google.gson.annotations.SerializedName

data class UserInfo(

    @SerializedName("name")
    val name: String?

)