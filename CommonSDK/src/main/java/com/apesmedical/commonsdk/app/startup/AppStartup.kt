package com.apesmedical.commonsdk.app.startup

import android.annotation.SuppressLint
import android.content.Context
import com.rousetime.android_startup.AndroidStartup

class AppStartup : AndroidStartup<MyApp>() {
    override fun callCreateOnMainThread() = false
    override fun waitOnMainThread() = true

    @SuppressLint("PrivateApi")
    override fun create(context: Context) =
        Class.forName("android.app.ActivityThread").run {
            val currentActivityThread = getMethod("currentActivityThread").invoke(null)
            getMethod("getApplication").invoke(currentActivityThread) as MyApp
        }
}