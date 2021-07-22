package com.apesmedical.commonsdk.delegate

import android.app.Application
import android.content.Context


/**
 * Created by Beetle_Sxy on 2020/10/12.
 * 代理 {@link Application} 的生命周期
 * 按需实现
 */
interface AppLifecycles {
    fun attachBaseContext(base: Context){}

    fun onCreate(application: Application){}

    fun onTerminate(application: Application){}
}