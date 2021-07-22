package com.pr1n.user

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.apesmedical.commonsdk.app.startup.AbstractStartup
import com.apesmedical.commonsdk.base.BaseActivityLifecycleCallbacks
import com.library.sdk.ext.logi

class UserStartup : AbstractStartup() {
    override val waitOnMainThread = false
    override val callCreateOnMainThread = true

    private val activityLifecycleCallbacks = object : BaseActivityLifecycleCallbacks() {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            logi("activityLifecycleCallbacks*-*-*-*-*-userModel")
        }
    }

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            logi("fragmentLifecycleCallbacks*-*-*-*-*-userModel")
        }
    }

    override fun create(context: Context) {
        init {
            addActivityLifecycleCallbacks(activityLifecycleCallbacks)
            addFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
        }
    }
}