package com.apesmedical.commonsdk.delegate

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.ContentProvider
import android.content.res.Configuration


/**
 * Created by Beetle_Sxy on 2020/10/12.
 *
 * [ComponentCallbacks2] 是一个细粒度的内存回收管理回调
 * [Application]、[Activity]、[Service]、[ContentProvider]、[Fragment] 实现了 [ComponentCallbacks2] 接口
 * 开发者应该实现 [ComponentCallbacks2.onTrimMemory] 方法, 细粒度 release 内存, 参数的值不同可以体现出不同程度的内存可用情况
 * 响应 [ComponentCallbacks2.onTrimMemory] 回调, 开发者的 App 会存活的更持久, 有利于用户体验
 * 不响应 [ComponentCallbacks2.onTrimMemory] 回调, 系统 kill 掉进程的几率更大
 */
class AppComponentCallbacks(private val application: Application) : ComponentCallbacks2 {


    /**
     * 在你的 App 生命周期的任何阶段, [ComponentCallbacks2.onTrimMemory] 发生的回调都预示着你设备的内存资源已经开始紧张
     * 你应该根据 [ComponentCallbacks2.onTrimMemory] 发生回调时的内存级别来进一步决定释放哪些资源
     * [ComponentCallbacks2.onTrimMemory] 的回调可以发生在 [Application]、[Activity]、[Service]、[ContentProvider]、[Fragment]
     *
     * @param level 内存级别
     * @see [level 官方文档](https://developer.android.com/reference/android/content/ComponentCallbacks2.html.TRIM_MEMORY_RUNNING_MODERATE)
     */
    override fun onTrimMemory(level: Int) {
        //状态1. 当开发者的 App 正在运行
        //设备开始运行缓慢, 不会被 kill, 也不会被列为可杀死的, 但是设备此时正运行于低内存状态下, 系统开始触发杀死 LRU 列表中的进程的机制
//                case TRIM_MEMORY_RUNNING_MODERATE:


        //设备运行更缓慢了, 不会被 kill, 但请你回收 unused 资源, 以便提升系统的性能, 你应该释放不用的资源用来提升系统性能 (但是这也会直接影响到你的 App 的性能)
//                case TRIM_MEMORY_RUNNING_LOW:


        //设备运行特别慢, 当前 App 还不会被杀死, 但是系统已经把 LRU 列表中的大多数进程都已经杀死, 因此你应该立即释放所有非必须的资源
        //如果系统不能回收到足够的 RAM 数量, 系统将会清除所有的 LRU 列表中的进程, 并且开始杀死那些之前被认为不应该杀死的进程, 例如那个包含了一个运行态 Service 的进程
//                case TRIM_MEMORY_RUNNING_CRITICAL:


        //状态2. 当前 App UI 不再可见, 这是一个回收大个资源的好时机
//                case TRIM_MEMORY_UI_HIDDEN:


        //状态3. 当前的 App 进程被置于 Background LRU 列表中
        //进程位于 LRU 列表的上端, 尽管你的 App 进程并不是处于被杀掉的高危险状态, 但系统可能已经开始杀掉 LRU 列表中的其他进程了
        //你应该释放那些容易恢复的资源, 以便于你的进程可以保留下来, 这样当用户回退到你的 App 的时候才能够迅速恢复
//                case TRIM_MEMORY_BACKGROUND:


        //系统正运行于低内存状态并且你的进程已经已经接近 LRU 列表的中部位置, 如果系统的内存开始变得更加紧张, 你的进程是有可能被杀死的
//                case TRIM_MEMORY_MODERATE:


        //系统正运行于低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的 App 恢复状态的资源
        //低于 API 14 的 App 可以使用 onLowMemory 回调
//                case TRIM_MEMORY_COMPLETE:
    }

    override fun onConfigurationChanged(newConfig: Configuration) {}


    /**
     * 当系统开始清除 LRU 列表中的进程时, 尽管它会首先按照 LRU 的顺序来清除, 但是它同样会考虑进程的内存使用量, 因此消耗越少的进程则越容易被留下来
     * [ComponentCallbacks2.onTrimMemory] 的回调是在 API 14 才被加进来的, 对于老的版本, 你可以使用 [ComponentCallbacks2.onLowMemory] 方法来进行兼容
     * [ComponentCallbacks2.onLowMemory] 相当于 `onTrimMemory(TRIM_MEMORY_COMPLETE)`
     *
     * @see .TRIM_MEMORY_COMPLETE
     */
    override fun onLowMemory() {
        //系统正运行于低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的 App 恢复状态的资源
    }

}