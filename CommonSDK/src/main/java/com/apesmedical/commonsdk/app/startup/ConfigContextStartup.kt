package com.apesmedical.commonsdk.app.startup

import android.content.Context
import android.content.res.Configuration
import com.rousetime.android_startup.AndroidStartup

class ConfigContextStartup : AndroidStartup<Unit>() {
    override fun callCreateOnMainThread() = false
    override fun waitOnMainThread() = true

    override fun create(context: Context) {
        // 禁止app字体大小跟随系统字体大小调节
        val resources = context.resources
        if (resources != null && resources.configuration.fontScale != 1.0f) {
            val configuration: Configuration = resources.configuration
            configuration.fontScale = 1.0f
            context.createConfigurationContext(configuration)
        }
    }
}