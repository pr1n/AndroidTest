package com.apesmedical.commonsdk.app.startup

import android.app.Application
import androidx.fragment.app.FragmentManager

fun FragmentManager.FragmentLifecycleCallbacks.convertActivityLifecycleCallbacks(application: Application? = null) =
    ActivityLifecycle(application, this)