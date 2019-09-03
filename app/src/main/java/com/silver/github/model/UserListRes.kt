package com.silver.github.model

import com.google.gson.annotations.SerializedName

data class UserListRes(

    @SerializedName("total_count")
    var count: Int? = null,

    @SerializedName("incomplete_results")
    var incompleted: Boolean? = null,

    @SerializedName("items")
    var items: List<User> = listOf()
)