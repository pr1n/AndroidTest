package com.apesmedical.commonsdk.UiKnife.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.apesmedical.commonsdk.UiKnife.UIConfig
import com.apesmedical.commonsdk.UiKnife.UiHplesInte
import com.blankj.utilcode.util.ReflectUtils
import com.library.sdk.ext.tryArea

/**
 * Created by Beetle_Sxy on 2019-06-21.
 * Activity 生命周期回调
 * 抽离 Base 类部分绑定功能
 */
class UiKnifeActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
	private val mFragmentLifecycleCallbacks =
		hashMapOf<Any, FragmentManager.FragmentLifecycleCallbacks>()
	
	override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
		if (activity !is LifecycleOwner) return
		if (activity is FragmentActivity) {
			mFragmentLifecycleCallbacks[activity] = UiKnifeFragmentLifecycleCallbacks()
			activity.supportFragmentManager.registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks[activity]!!, true)
		}
		val mConfig = activity.javaClass.getAnnotation(UIConfig::class.java)
		if (mConfig !== null) {
			val activityReflect = ReflectUtils.reflect(activity)
			val mUiHplesInte = ReflectUtils.reflect(mConfig.help.java).newInstance().get<UiHplesInte>()
			tryArea { activityReflect.field("mUiHples", mUiHplesInte) }
			mUiHplesInte.bind(activity, mConfig)
		}
	}
	
	override fun onActivityStarted(activity: Activity) {
	}
	
	override fun onActivityResumed(activity: Activity) {
	}
	
	override fun onActivityPaused(activity: Activity) {
	}
	
	override fun onActivityStopped(activity: Activity) {
	}
	
	override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
	}
	
	override fun onActivityDestroyed(activity: Activity) {
		if (activity is FragmentActivity) activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(
			mFragmentLifecycleCallbacks[activity]!!
		)
	}
}