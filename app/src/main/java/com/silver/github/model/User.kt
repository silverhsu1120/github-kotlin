package com.silver.github.model

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("id")
    var id: Long?,

    @SerializedName("login")
    var name: String?,

    @SerializedName("avatar_url")
    var avatar: String?
)