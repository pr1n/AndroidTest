package com.apesmedical.commonsdk.app.startup

import android.app.Application

interface RegisterLifecycleCallbacks {
    fun registerActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks)
}