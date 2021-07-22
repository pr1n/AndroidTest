package com.apesmedical.commonsdk.base

import androidx.annotation.UiThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeremyliao.liveeventbus.core.Observable
import com.library.sdk.ext.logi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.set
import kotlin.coroutines.CoroutineContext

/**
 * Created by Beetle_Sxy on 2020/10/9.
 * 协程处理网络请求回调
 */
abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    private val mObserverManagement = hashMapOf<LiveData<*>, Observer<in Any>>()
    private val mLiveBusManagement = hashMapOf<Observable<in Any>, Observer<*>>()

    /**
     * 需要 lifecycle.addObserver(mViewModel)
     * 对应 Activity/Fragment -> onStart
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
    }

    /**
     * 需要 lifecycle.addObserver(mViewModel)
     * 对应 Activity/Fragment -> onCreate
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
    }

    /**
     * 运行在UI线程的协程 viewModelScope 已经实现了在onCleared取消协程
     */
    @UiThread
    fun launchUI(
        context: CoroutineContext = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) =
        viewModelScope.launch(
            context = context + CoroutineExceptionHandler { _, throwable -> logi("CoroutineExceptionHandler -> $throwable") },
            start = start,
            block = block
        )

    /**
     * LiveData 在 ViewModel 中使用生命周期管理
     */
    protected fun <T> addObserver(key: LiveData<T>, observer: Observer<T>) {
        key.observeForever(observer)
        mObserverManagement[key] = observer as Observer<in Any>
    }

    /**
     * LiveEventBus 在 ViewModel 中使用生命周期管理语法糖
     */
    protected fun <T : Any> Observable<T>.add(observer: Observer<T>) {
        this.observeForever(observer)
        mLiveBusManagement[this as Observable<in Any>] = observer as Observer<in Any>
    }

    /**
     * LiveData 在 ViewModel 中使用生命周期管理语法糖
     */
    protected fun <T> LiveData<T>.add(observer: Observer<T>) {
        this.observeForever(observer)
        mObserverManagement[this] = observer as Observer<in Any>
    }

    override fun onCleared() {
        //livedata 解绑
        mObserverManagement.forEach {
            it.key.removeObserver(it.value)
        }
        mLiveBusManagement.forEach {
            it.key.removeObserver(it.value)
        }
        mObserverManagement.clear()
        mLiveBusManagement.clear()

    }


}
