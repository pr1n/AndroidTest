package com.apesmedical.commonsdk.app.startup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.launcher.ARouter
import com.apesmedical.commonsdk.UiKnife.app.UiKnifeActivityLifecycleCallbacks
import com.apesmedical.commonsdk.base.BaseActivityLifecycleCallbacks
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup

class LifecycleCallbacksStartup : AndroidStartup<Unit>() {
    override fun callCreateOnMainThread() = false
    override fun waitOnMainThread() = true

    private lateinit var registerLifecycleCallbacks: RegisterLifecycleCallbacks

    private val uiKnifeActivityLifecycle = UiKnifeActivityLifecycleCallbacks()
    private val activityLifecycle = object :
        BaseActivityLifecycleCallbacks() {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            ARouter.getInstance().inject(activity)
        }
    }

    private val fragmentLifecycle = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            ARouter.getInstance().inject(f)
        }
    }

    @SuppressLint("PrivateApi")
    override fun create(context: Context) {
        registerLifecycleCallbacks.registerActivityLifecycleCallbacks(uiKnifeActivityLifecycle)
        registerLifecycleCallbacks.registerActivityLifecycleCallbacks(activityLifecycle)
        registerLifecycleCallbacks.registerActivityLifecycleCallbacks(fragmentLifecycle.convertActivityLifecycleCallbacks())
    }

    override fun dependencies() = listOf(AppStartup::class.java)

    override fun onDependenciesCompleted(startup: Startup<*>, result: Any?) =
        if (startup is AppStartup) registerLifecycleCallbacks =
            result as RegisterLifecycleCallbacks else Unit

}