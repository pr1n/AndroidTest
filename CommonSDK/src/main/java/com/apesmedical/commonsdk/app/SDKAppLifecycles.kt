package com.apesmedical.commonsdk.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.apesmedical.commonsdk.BuildConfig
import com.apesmedical.commonsdk.delegate.AppLifecycles
import com.blankj.utilcode.util.Utils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lzx.starrysky.StarrySky
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV

/**
 * Created by Beetle_Sxy on 2020/10/12.
 */
class SDKAppLifecycles : AppLifecycles {
    override fun attachBaseContext(base: Context) {
        MultiDex.install(base)
    }

    override fun onCreate(application: Application) {
        Utils.init(application)
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application);
        LiveEventBus.config()
            .autoClear(true)//在没有Observer关联的时候是否自动清除LiveEvent以释放内存
            .enableLogger(BuildConfig.DEBUG.not())//是否打印日志

        //Bugly日志上报
        CrashReport.initCrashReport(
            application,
            if (BuildConfig.BUILD_TYPE == "release") "9247ef8e2b" else "9425dcfbb4",
            BuildConfig.BUILD_TYPE != "release"
        )

        MMKV.initialize(application)
        //音频初始化
        StarrySky.init(application).apply()
        //initRxHttp(application)
    }

    private fun initRxHttp(application: Application) {
        /**
         * 设置debug模式，默认为false，设置为true后，发请求，过滤"RxHttp"能看到请求日志，
         * okHttpClient 的初始化已经在 [com.apesmedical.commonsdk.delegate.GlobalConfigModule] 中完成
         */
        //RxHttp.init(application.get(), BuildConfig.DEBUG)

        //设置数据解密/解码器，非必须
//        RxHttp.setResultDecoder(s -> s);

        //设置全局的转换器，非必须
//        RxHttp.setConverter(FastJsonConverter.create());

        //设置公共参数，非必须
        //RxHttp.setOnParamAssembly(application.get())
    }
}