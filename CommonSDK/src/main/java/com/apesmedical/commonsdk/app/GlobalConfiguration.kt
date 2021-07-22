package com.apesmedical.commonsdk.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.launcher.ARouter
import com.apesmedical.commonsdk.UiKnife.app.UiKnifeActivityLifecycleCallbacks
import com.apesmedical.commonsdk.base.BaseActivityLifecycleCallbacks
import com.apesmedical.commonsdk.delegate.AppLifecycles
import com.apesmedical.commonsdk.delegate.ConfigModule

//import me.jessyan.retrofiturlmanager.RetrofitUrlManager

/**
 * Created by Beetle_Sxy on 2020/10/12.
 */
class GlobalConfiguration : ConfigModule {
    
    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>) {
        lifecycles.add(SDKAppLifecycles())
    }
    
    override fun injectActivityLifecycle(context: Context, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>) {
        lifecycles.add(UiKnifeActivityLifecycleCallbacks())
        lifecycles.add(object : BaseActivityLifecycleCallbacks() {
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                //ScreenUtils.setPortrait(activity)
                ARouter.getInstance().inject(activity)
            }
        })
    }
    
    override fun injectFragmentLifecycle(context: Context, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>) {
        super.injectFragmentLifecycle(context, lifecycles)
        lifecycles.add(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                super.onFragmentCreated(fm, f, savedInstanceState)
                ARouter.getInstance().inject(f)
            }
        })
    }
    
}

