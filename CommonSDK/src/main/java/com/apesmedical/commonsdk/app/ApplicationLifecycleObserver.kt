package com.apesmedical.commonsdk.app

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.apesmedical.commonsdk.data.SDKBusHub
import com.jeremyliao.liveeventbus.LiveEventBus
import com.library.sdk.ext.d

/**
 * Created by Beetle_Sxy on 4/12/21.
 * app 前后台生命周期监听
 */
class ApplicationLifecycleObserver : LifecycleObserver {
	
	/**
	 * 应用 START
	 */
	@OnLifecycleEvent(Lifecycle.Event.ON_START)
	fun onAppStart() {
		LiveEventBus.get(SDKBusHub.APP_BACKSTAGE_STATE, Boolean::class.java).post(true)
		"应用 START".d()
	}
	
	/**
	 * 应用 STOP
	 */
	@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
	fun onAppStop() {
		LiveEventBus.get(SDKBusHub.APP_BACKSTAGE_STATE, Boolean::class.java).post(false)
		"应用 STOP".d()
	}
}