package com.apesmedical.commonsdk.app.startup

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.apesmedical.commonsdk.BuildConfig
import com.blankj.utilcode.util.Utils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lzx.starrysky.StarrySky
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import org.jetbrains.anko.runOnUiThread

class SdkStartup : AndroidStartup<Unit>() {
    override fun callCreateOnMainThread() = false
    override fun waitOnMainThread() = true

    private lateinit var application: Application

    override fun create(context: Context) {
       ARouter.init(application)

        if (BuildConfig.DEBUG) {    // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()       // 打印日志
            ARouter.openDebug()     // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }

        LiveEventBus.config()
            .autoClear(true)    //在没有Observer关联的时候是否自动清除LiveEvent以释放内存
            .enableLogger(BuildConfig.DEBUG.not())//是否打印日志

        //Bugly日志上报
        CrashReport.initCrashReport(
            application,
            if (BuildConfig.BUILD_TYPE == "release") "9247ef8e2b" else "9425dcfbb4",
            BuildConfig.BUILD_TYPE != "release"
        )

        MMKV.initialize(application)

        application.runOnUiThread {
            Utils.init(application)
            //音频初始化
            StarrySky.init(application).apply()
        }
    }

    override fun dependencies() = listOf(AppStartup::class.java)

    override fun onDependenciesCompleted(startup: Startup<*>, result: Any?) =
        if (startup is AppStartup) application = result as Application else Unit
}