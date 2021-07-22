package com.apesmedical.commonsdk.delegate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * Created by Beetle_Sxy on 2020/10/12.
 *
 * [Application.ActivityLifecycleCallbacks] 默认实现类
 * 通过 [ActivityDelegate] 管理 [Activity]
 */
class ActivityLifecycle(private val mApplication: Application, private val mModules: List<FragmentManager.FragmentLifecycleCallbacks>) :
    Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        mModules.forEach { (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(it, true) }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        mModules.forEach { (activity as? FragmentActivity)?.supportFragmentManager?.unregisterFragmentLifecycleCallbacks(it) }
    }
}