package com.apesmedical.commonsdk.base

import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.ImmersionOwner
import com.gyf.immersionbar.components.ImmersionProxy


/**
 * Created by Beetle_Sxy on 2020/10/26.
 * 沉浸式布局 BaseFragment 配合 [ImmersionBar] 使用提供了生命周期方法
 */
abstract class ImmersionFragment<B : ViewDataBinding>(@LayoutRes layoutRes: Int) : BaseFragment<B>(layoutRes), ImmersionOwner {
    protected val mImmersionProxy by lazy { ImmersionProxy(this) }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mImmersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImmersionProxy.onCreate(savedInstanceState)
    }

    override fun onActivityCreated( savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mImmersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mImmersionProxy.onResume()
    }

    override fun onPause() {
        super.onPause()
        mImmersionProxy.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mImmersionProxy.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mImmersionProxy.onHiddenChanged(hidden)
    }

   override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mImmersionProxy.onConfigurationChanged(newConfig)
    }


    /**
     * 懒加载，在view初始化完成之前执行
     * On lazy after view.
     */
    override fun onLazyBeforeView() {}

    /**
     * 懒加载，在view初始化完成之后执行
     * On lazy before view.
     */
    override fun onLazyAfterView() {}

    /**
     * Fragment用户可见时候调用
     * On visible.
     */
    override fun onVisible() {}

    /**
     * Fragment用户不可见时候调用
     * On invisible.
     */
    override fun onInvisible() {}

    /**
     * 初始化沉浸式代码
     * Init immersion bar.
     */
    override fun initImmersionBar() {
    }
	
	/**
	 * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
	 * Immersion bar enabled boolean.
	 *
	 * @return the boolean
	 */
	override fun immersionBarEnabled() = false
}