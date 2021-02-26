package com.isaac.m17.base

import android.app.Application
import com.lzy.okgo.OkGo
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val builder = OkHttpClient.Builder()
        builder.apply {
            readTimeout(3000, TimeUnit.MILLISECONDS)
            writeTimeout(3000, TimeUnit.MILLISECONDS)
            connectTimeout(30000, TimeUnit.MILLISECONDS)
        }
        OkGo.getInstance().init(this).okHttpClient = builder.build()
    }
}