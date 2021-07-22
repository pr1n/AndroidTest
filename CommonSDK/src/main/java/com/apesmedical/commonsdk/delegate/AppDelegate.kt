package com.apesmedical.commonsdk.delegate

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ProcessLifecycleOwner
import com.apesmedical.commonsdk.app.ApplicationLifecycleObserver
import com.apesmedical.commonsdk.utlis.ManifestParser
import org.jetbrains.anko.collections.forEachReversedByIndex
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by Beetle_Sxy on 2020/10/12.
 */
class AppDelegate(private val mBase: Context) : AppLifecycles {
	
	//用反射, 将 AndroidManifest.xml 中带有 ConfigModule 标签的 class 转成对象集合（List<ConfigModule>）
	private val mModules by lazy { ManifestParser(mBase).parse() }
	private val mAppLifecycles: MutableList<AppLifecycles> = mutableListOf()
	private val mActivityLifecycles: MutableList<Application.ActivityLifecycleCallbacks> = mutableListOf()
	private val mFragmentLifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks> = mutableListOf()
	private val mGlobalConfigModule: GlobalConfigModule = GlobalConfigModule()
	private var mActivityLifecycle: Application.ActivityLifecycleCallbacks? = null
	
	init {
		//遍历之前获得的集合, 执行每一个 ConfigModule 实现类的某些方法
		mModules.forEachReversedByIndex { module ->
			//给全局配置 GlobalConfigModule 添加参数
			module.applyOptions(mBase, mGlobalConfigModule)

			//将框架外部, 开发者实现的 Application 的生命周期回调 (AppLifecycles) 存入 mAppLifecycles 集合 (此时还未注册回调)
			module.injectAppLifecycle(mBase, mAppLifecycles)

			//将框架外部, 开发者实现的 Activity 的生命周期回调 (ActivityLifecycleCallbacks) 存入 mActivityLifecycles 集合 (此时还未注册回调)
			module.injectActivityLifecycle(mBase, mActivityLifecycles)

			module.injectFragmentLifecycle(mBase, mFragmentLifecycles)
		}
		
	}
	
	
	override fun onCreate(application: Application) {
		mActivityLifecycle = ActivityLifecycle(application, mFragmentLifecycles)

		startKoin {
			//androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)//用来打印日志
			androidLogger(Level.DEBUG)//用来打印日志
			androidContext(application)//向Koin中注入context
			modules(mGlobalConfigModule.getModule())
		}
		
		//listOf( GlobalConfigModule.apply {}.build())
		//注册框架内部已实现的 Activity 生命周期逻辑
		application.registerActivityLifecycleCallbacks(mActivityLifecycle)
		
		//注册框架外部, 开发者扩展的 Activity 生命周期逻辑
		//每个 ConfigModule 的实现类可以声明多个 Activity 的生命周期回调
		//也可以有 N 个 ConfigModule 的实现类 (完美支持组件化项目 各个 Module 的各种独特需求)
		mActivityLifecycles.forEach { application.registerActivityLifecycleCallbacks(it) }
		
		//注册回掉: 内存紧张时释放部分内存
		application.registerComponentCallbacks(AppComponentCallbacks(application))
		
		//执行框架外部, 开发者扩展的 App onCreate 逻辑
		mAppLifecycles.forEach { it.onCreate(application) }
		
		//监听app前后台生命周期
		ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
		
	}
	
	override fun attachBaseContext(base: Context) {
		//遍历 mAppLifecycles, 执行所有已注册的 AppLifecycles 的 attachBaseContext() 方法 (框架外部, 开发者扩展的逻辑)
		mAppLifecycles.forEach { it.attachBaseContext(base) }
	}
	
	override fun onTerminate(application: Application) {
		if (mActivityLifecycle != null) application.unregisterActivityLifecycleCallbacks(mActivityLifecycle)
		mActivityLifecycles.forEach { application.unregisterActivityLifecycleCallbacks(it) }
	}
}