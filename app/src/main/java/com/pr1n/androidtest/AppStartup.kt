package com.pr1n.androidtest

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.apesmedical.commonsdk.app.startup.AbstractStartup
import com.apesmedical.commonsdk.base.BaseActivityLifecycleCallbacks
import com.apesmedical.commonsdk.base.newbase.LocalService
import com.apesmedical.commonsdk.base.newbase.RxHttpRemoteService
import com.apesmedical.commonsdk.base.newbase.RemoteService
import com.apesmedical.commonsdk.db.MainDB
import com.library.sdk.ext.logi
import com.pr1n.androidtest.repo.MainRepository
import com.pr1n.androidtest.repo.impl.MainRepositoryImpl
import com.pr1n.androidtest.viewmodel.ViewModel1
import com.pr1n.androidtest.viewmodel.ViewModel2
import com.pr1n.androidtest.viewmodel.ViewModel3
import com.pr1n.user.UserStartup
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppStartup : AbstractStartup() {
    override val waitOnMainThread = false
    override val callCreateOnMainThread = true
    override val dependencies = listOf(UserStartup::class.java)


    private val module = module {
        single<RemoteService> { RxHttpRemoteService() }
        single<LocalService> { LocalService }
        single { MainDB.getDatabase(androidContext()) }
        factory<MainRepository> { MainRepositoryImpl() }
        viewModel { params -> ViewModel1(params.get(), get()) }
        viewModel { params -> ViewModel2(params.get(), get()) }
        viewModel { params -> ViewModel3(params.get(), get()) }
    }

    private val activityLifecycleCallbacks = object : BaseActivityLifecycleCallbacks() {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            logi("activityLifecycleCallbacks*-*-*-*-*-")
        }
    }

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            logi("fragmentLifecycleCallbacks*-*-*-*-*-")
        }
    }

    override fun create(context: Context) {
        init {
            initKoin {
                modules(module)
            }
            addActivityLifecycleCallbacks(activityLifecycleCallbacks)
            addFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
        }
    }
}