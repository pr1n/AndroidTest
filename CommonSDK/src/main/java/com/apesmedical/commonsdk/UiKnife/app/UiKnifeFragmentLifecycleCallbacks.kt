package com.apesmedical.commonsdk.UiKnife.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.apesmedical.commonsdk.UiKnife.UIConfig
import com.apesmedical.commonsdk.UiKnife.UiHplesInte
import com.blankj.utilcode.util.ReflectUtils
import com.library.sdk.ext.tryArea

/**
 * Created by Beetle_Sxy on 2019-06-21.
 * Fragment 的声明周期回调
 */
class UiKnifeFragmentLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {
	override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
		super.onFragmentCreated(fm, f, savedInstanceState)
		val mConfig = f.javaClass.getAnnotation(UIConfig::class.java)
		if (mConfig !== null) {
			val activityReflect = ReflectUtils.reflect(f)
			val mUiHplesInte = ReflectUtils.reflect(mConfig.help.java).newInstance().get<UiHplesInte>()
			tryArea { activityReflect.field("mUiHples", mUiHplesInte) }
			mUiHplesInte.bind(f, mConfig)
		}
	}
}