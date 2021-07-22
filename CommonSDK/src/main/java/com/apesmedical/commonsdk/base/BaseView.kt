package com.apesmedical.commonsdk.base

import android.os.Bundle
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import com.apesmedical.commonsdk.UiKnife.UiHplesInte
import com.apesmedical.commonsdk.http.Complete
import com.apesmedical.commonsdk.http.Failure
import com.apesmedical.commonsdk.http.Loading
import com.apesmedical.commonsdk.http.ResultData
import com.apesmedical.commonsdk.http.Success
import com.apesmedical.commonsdk.ktx.KeyboardExt
import com.library.sdk.ext.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.ext.android.stateViewModel

/**
 * Created by Beetle_Sxy on 2020/10/9.
 */
abstract class BaseView<DB : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :AppCompatActivity(), CoroutineScope by MainScope() {

    protected var mUiHples: UiHplesInte? = null//通过反射赋值
    protected val mRootLayout by lazy { mUiHples?.getRootLayout() as? LinearLayout }
    protected lateinit var mDataBinding: DB

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = if (mRootLayout != null) DataBindingUtil.inflate(
            layoutInflater,
            layoutRes,
            mRootLayout,
            true
        )
        else initBinding(layoutRes)
        mDataBinding.lifecycleOwner = this
        initView(savedInstanceState)
    }

    /**
     * 初始化[binding]
     * 当 mRootLayout == null 时调用
     */
    open fun initBinding(@LayoutRes layoutRes: Int): DB = DataBindingUtil.setContentView(
        this,
        layoutRes
    )

    //初始化
    abstract fun initView(savedInstanceState: Bundle?)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        KeyboardExt.handleAutoCloseKeyboard(true, currentFocus, ev, this)
        return super.dispatchTouchEvent(ev)
    }

    protected fun <T> LiveData<ResultData<T>>.observer(
        failure: (message: String, throwable: Throwable) -> Unit = { _, _ -> },
        success: ((T?) -> Unit) = {},
    ) {
        observe(this@BaseView) {
            when (val result = it) {
                is Loading -> logi("*-*-*-*-*-Loading")
                is Failure -> {
                    logi("*-*-*-*-*-Failure -> message : ${result.message}, throwable : ${result.throwable}")
                    failure(result.message ?: "", result.throwable ?: Exception())
                }
                is Success -> {
                    logi("*-*-*-*-*-Success -> data : ${result.data}")
                    success(result.data)
                }
                is Complete -> logi("*-*-*-*-*-Complete")
            }

        }
    }

}

