package com.apesmedical.commonsdk.UiKnife.default

import android.app.Application

/**
 * Created by Beetle_Sxy on 2020/11/13.
 * UiKnife 配置类 重构
 */
object UiKnife {
	private lateinit var _application: Application
	
	/**
	 * 初始化
	 */
	fun init(application: Application) {
		_application = application
	}
}