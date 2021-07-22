package com.apesmedical.commonsdk.delegate

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager

/**
 * Created by Beetle_Sxy on 2020/10/12.
 * [ConfigModule] 可以给框架配置一些参数,需要实现 [ConfigModule] 后,在 AndroidManifest 中声明该实现类
 */
interface ConfigModule  {

    /**
     * 使用{@link GlobalConfigModule.Builder}给框架配置一些配置参数
     *
     * @param context
     * @param builder
     */
    fun applyOptions(context: Context, builder: GlobalConfigModule){}

    /**
     * 使用[AppLifecycles]在Application的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>)

    /**
     * 使用[Application.ActivityLifecycleCallbacks]在Activity的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    fun injectActivityLifecycle(context: Context, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>){}


    /**
     * 使用[FragmentManager.FragmentLifecycleCallbacks]在Fragment的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    @Deprecated("未完成功能")
    fun injectFragmentLifecycle(context: Context, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>){}
}