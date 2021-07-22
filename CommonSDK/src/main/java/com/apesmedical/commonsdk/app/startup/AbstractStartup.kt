package com.apesmedical.commonsdk.app.startup

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import org.koin.core.KoinApplication

abstract class AbstractStartup : AndroidStartup<Unit>() {
    protected abstract val waitOnMainThread: Boolean
    protected abstract val callCreateOnMainThread: Boolean
    protected open val dependencies: List<Class<out Startup<*>>> = emptyList()

    protected lateinit var registerLifecycleCallbacks: RegisterLifecycleCallbacks
    protected lateinit var koinApp: KoinApplication

    override fun waitOnMainThread() = waitOnMainThread
    override fun callCreateOnMainThread() = callCreateOnMainThread

    abstract override fun create(context: Context)

    final override fun dependencies() = mutableListOf(
        ConfigContextStartup::class.java,
        AppStartup::class.java,
        SdkStartup::class.java,
        KoinStartup::class.java,
        LifecycleCallbacksStartup::class.java
    ).also { it + dependencies }

    final override fun onDependenciesCompleted(startup: Startup<*>, result: Any?) = when (startup) {
        is AppStartup -> registerLifecycleCallbacks = result as RegisterLifecycleCallbacks
        is KoinStartup -> koinApp = result as KoinApplication
        else -> Unit
    }

    protected fun init(block: InitBuilder.() -> Unit) = block(InitBuilder())

    protected inner class InitBuilder {
        fun initKoin(initBlock: KoinApplication.() -> Unit) = initBlock(koinApp)
        fun addActivityLifecycleCallbacks(vararg callbacks: Application.ActivityLifecycleCallbacks) =
            if (!callbacks.isNullOrEmpty()) callbacks.forEach(registerLifecycleCallbacks::registerActivityLifecycleCallbacks)
            else Unit

        fun addFragmentLifecycleCallbacks(vararg callbacks: FragmentManager.FragmentLifecycleCallbacks) =
            if (!callbacks.isNullOrEmpty()) callbacks.forEach {
                registerLifecycleCallbacks.registerActivityLifecycleCallbacks(it.convertActivityLifecycleCallbacks())
            }
            else Unit
    }
}