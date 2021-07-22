package com.apesmedical.commonsdk.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.apesmedical.commonsdk.UiKnife.UIConfig
import com.apesmedical.commonsdk.UiKnife.UiHplesInte
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * BaseFragment
 * 提供了 DataBinding 并兼容了 [UiHplesInte] 功能模块
 */
@UIConfig
abstract class BaseFragment<DB : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    Fragment(), CoroutineScope by MainScope() {
    //NavHostFragment
    private var _binding: DB? = null
    protected val mDataBinding get() = _binding!!
    protected var mUiHples: UiHplesInte? = null//通过反射赋值
    protected val mRootLayout by lazy { mUiHples?.getRootLayout() as? LinearLayout }
    private var isFragmentViewInit = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        if (mRootLayout?.isEmpty() != false) inflater.inflate(layoutRes, container, false)
        else inflater.inflate(layoutRes, mRootLayout, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Navigation返回避免页面重建
        if (isFragmentViewInit.not()) {
            _binding = DataBindingUtil.bind<DB>(mUiHples?.getLayout() ?: view)
                ?.also { it.lifecycleOwner = this }
            initData(savedInstanceState)
            isFragmentViewInit = true
        }
    }

    /**
     * 初始化 [_binding]
     */
    abstract fun initData(savedInstanceState: Bundle?)

}