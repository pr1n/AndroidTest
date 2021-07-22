package com.apesmedical.commonsdk.app.startup

import com.library.sdk.ext.logi
import com.rousetime.android_startup.StartupListener
import com.rousetime.android_startup.model.CostTimesModel
import com.rousetime.android_startup.model.LoggerLevel
import com.rousetime.android_startup.model.StartupConfig
import com.rousetime.android_startup.provider.StartupProviderConfig

class StartupProviderConfig : StartupProviderConfig {
    override fun getConfig(): StartupConfig {
        return StartupConfig.Builder()
            .setLoggerLevel(LoggerLevel.DEBUG)
            .setAwaitTimeout(12000L)
            .setListener(object : StartupListener {
                override fun onCompleted(
                    totalMainThreadCostTime: Long,
                    costTimesModels: List<CostTimesModel>
                ) {
                    logi("Startup Completed...")
                }
            })
            .build()
    }
}