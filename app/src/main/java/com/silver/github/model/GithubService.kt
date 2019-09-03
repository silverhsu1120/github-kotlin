package com.silver.github.model

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GithubService private constructor() {

    private object Holder {
        val instance = GithubService()
    }

    private val retrofit by lazy {
        val baseUrl = "https://api.github.com"
        val client = OkHttpClient.Builder().build()
        val gson = GsonBuilder().serializeNulls().create()
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    companion object {

        val apiUser: ApiUser = Holder.instance.retrofit.create(
            ApiUser::class.java
        )
    }
}
