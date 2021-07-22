package com.apesmedical.commonsdk.UiKnife

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.apesmedical.commonsdk.R
import com.blankj.utilcode.util.ReflectUtils
import com.library.sdk.ext.tryArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.scrollView


/**
 * Created by Beetle_Sxy on 2019-06-21.
 * 基础布局帮助类
 * 提供功能有:
 *  1. 布局状态切换(未实现)
 *      1.1 成功布局
 *      1.2 等待布局
 *      1.3 错误布局
 *      1.4 自定义布局
 *  2. 标题栏实现
 *      2.1 默认类型布局
 *      2.2 自定义布局
 *  3. 导航实现
 *      3.1 默认布局类型
 *      3.2 自定义布局类型
 *  4. 类型布局（未实现）
 *      4.1 ScrollView
 *      4.2 RecyclerView
 */
class DefaultUiHplesImpl : UiHplesInte {
	
	private val mRootLayout by lazy {
		LinearLayout(mContext).apply {
			orientation = LinearLayout.VERTICAL
		}
	}
	private var mContext: Context? = null
	private var mActionHples: ActionHplesInte? = null
	private var mNavigationHples: NavigationHplesInte? = null
	
	override fun bind(container: LifecycleOwner?, uiConfig: UIConfig?): ViewGroup? {
		
		when (container) {
			is Activity -> {
				mContext = container
				val rootLayout = container.findViewById<ViewGroup>(android.R.id.content)
				rootLayout.removeAllViews()
				rootLayout.addView(mRootLayout)
			}
			is Fragment -> mContext = container.context
			else -> throw NullPointerException("container 只能为 Activity 和 Fragment")
		}
		container.lifecycleScope.launch {
			initLayout(container, uiConfig)
			initUi(container, uiConfig)
		}
		return mRootLayout
	}
	
	private fun addViewGroup(res: Int?, rootLayout: ViewGroup = mRootLayout) {
		rootLayout.addView(
			LayoutInflater
				.from(rootLayout.context)
				.inflate(res ?: R.layout.activity_default, rootLayout, false)
		)
	}
	
	private suspend fun initLayout(container: Any, uiConfig: UIConfig?) {
		val activityReflect = ReflectUtils.reflect(container)
		
		val mScrollViewConfig = container.javaClass.getAnnotation(ScrollViewConfig::class.java)
		
		//读取layoutRes
		val layoutRes = try {
			activityReflect.method("getLayoutRes").get()
		} catch (e: Exception) {
			-1
		}
		
		//读取layoutView
		val layoutView = try {
			activityReflect.method("getLayoutView").get<View?>()
		} catch (e: Exception) {
			null
		}
		
		//布局嵌套
		val rootLayouy = when {
			mScrollViewConfig != null -> mRootLayout.scrollView()
			/*mRecyclerViewConfig != null -> {
				val recyclerView = mRootLayout.recyclerView()
				recyclerView
			}*/
			else -> mRootLayout
		}
		
		//填入布局
		when {
			uiConfig?.layout != -1 -> addViewGroup(uiConfig?.layout, rootLayouy)
			layoutRes != -1 -> addViewGroup(layoutRes, rootLayouy)
			layoutView != null -> rootLayouy.addView(layoutView)
			else -> tryArea(catchArea = {}) { activityReflect.method("getLayoutAnko", rootLayouy).get<View>() }
		}
		
		if (mRootLayout.childCount > 0) setWeight(mRootLayout[0])
		
	}
	
	private fun setWeight(view: View) {
		tryArea(catchArea = {}) {
			view.layoutParams =
				LinearLayout.LayoutParams(matchParent, 0).apply { weight = 1f }
		}
	}
	
	private suspend fun initUi(container: LifecycleOwner, uiConfig: UIConfig?) {
		uiConfig?.isAction?.also {
			if (it.not()) return
			setAction(DefaultActionHplesImpl()
				.setTitleText(uiConfig.title)//设置标题
				.setBackClick { back() }//设置返回
			) {}
		}
	}
	
	private suspend fun back() {
		withContext(Dispatchers.Default) { tryArea { Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK) } }
	}
	
	/* ######################################################## 标题栏方法 ######################################################## */
	override fun setAction(actionHples: ActionHplesInte, onCallback: (ViewGroup.() -> Unit)?): ActionHplesInte {
		if (mActionHples != null) mRootLayout.removeViewAt(0)
		if (mRootLayout.childCount > 0) setWeight(mRootLayout[0])
		mActionHples = actionHples
		val bind = actionHples.bind(mRootLayout)
		if (onCallback != null) onCallback(bind)
		return actionHples
	}
	
	override fun <T : ActionHplesInte> getActionHples() = mActionHples as? T
	
	/* ######################################################## 导航栏方法 ######################################################## */
	override fun setNavigation(navigationHples: NavigationHplesInte, onCallback: (ViewGroup.() -> Unit)?): NavigationHplesInte {
		if (mNavigationHples != null) mRootLayout.removeViewAt(mRootLayout.childCount - 1)
		if (mRootLayout.childCount > 0) setWeight(mRootLayout[if (mActionHples != null) 1 else 0])
		mNavigationHples = navigationHples
		val bind = navigationHples.bind(mRootLayout)
		if (onCallback != null) onCallback(bind)
		return navigationHples
	}
	
	override fun <T : NavigationHplesInte> getNavigationHples() = mNavigationHples as? T
	
	override fun getRootLayout() = mRootLayout
	
	override fun getLayout() = mRootLayout.getChildAt(if (mActionHples == null) 0 else 1)
	
}