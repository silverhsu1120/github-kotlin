package com.silver.github.model

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiUser {

    @GET("/search/users")
    fun search(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") count: Int
    ): Observable<UserListRes>
}
