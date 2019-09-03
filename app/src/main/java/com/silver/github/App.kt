package com.silver.github

import android.app.Application
import android.content.Context

class App : Application() {

    init {
        instance = this
    }

    companion object {

        private lateinit var instance: App

        fun getContext(): Context = instance.applicationContext
    }
}