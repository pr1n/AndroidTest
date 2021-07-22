package com.apesmedical.commonsdk.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.apesmedical.commonsdk.delegate.AppDelegate
import com.apesmedical.commonsdk.delegate.AppLifecycles

/**
 * Created by Beetle_Sxy on 2020-10-02.
 */
open class ApesApplication : Application() {

    private var mAppDelegate: AppLifecycles? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate == null) mAppDelegate = AppDelegate(base)
        mAppDelegate?.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        mAppDelegate?.onCreate(this)
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate?.onTerminate(this)
    }

    override fun getResources(): Resources? {
        //禁止app字体大小跟随系统字体大小调节
        val resources: Resources? = super.getResources()
        if (resources != null && resources.configuration.fontScale !== 1.0f) {
            val configuration: Configuration = resources.configuration
            configuration.fontScale = 1.0f
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        return resources
    }
}