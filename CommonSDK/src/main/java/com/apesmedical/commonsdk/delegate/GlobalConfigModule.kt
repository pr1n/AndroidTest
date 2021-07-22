package com.apesmedical.commonsdk.delegate

import android.app.Application
import android.content.Context
import com.apesmedical.commonsdk.http.GlobalHttpHandler
import com.library.sdk.ext.middleToast
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module
import rxhttp.wrapper.callback.Function
import rxhttp.wrapper.param.Param
import rxhttp.wrapper.ssl.HttpsUtils
import java.net.UnknownHostException
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * Created by Beetle_Sxy on 2020/10/12.
 */
class GlobalConfigModule {

    private val mKoinModule: MutableList<Module> = mutableListOf()
    private val TIME_OUT = 10L

    private val interceptors = mutableListOf<Interceptor>()
    private var handler: GlobalHttpHandler = GlobalHttpHandler
    private var rxHandler: MutableList<(Param<*>) -> Unit> = mutableListOf()
    private var okhttpConfiguration: (context: Context, builder: OkHttpClient.Builder) -> Unit =
        { _, _ -> }

    private var executorService: ExecutorService = ThreadPoolExecutor(
        0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
        SynchronousQueue()
    ) { run -> Thread(run, "apes Executor") }

    init {
        mKoinModule.add(module {
            single { OkHttpClient.Builder() }
            single {
                val builder: OkHttpClient.Builder = get()
                val application: Application = get()
                val sslParams = HttpsUtils.getSslSocketFactory()
                builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
                    .hostnameVerifier { _, _ -> true } //忽略host验证
                    .dispatcher(Dispatcher(executorService))
                    .apply {
                        try {
                            addInterceptor(Interceptor { chain ->
                                chain.proceed(
                                    handler.onHttpRequestBefore(
                                        chain,
                                        chain.request()
                                    )
                                )
                            })
                            //如果外部提供了interceptor的集合则遍历添加
                            for (interceptor in interceptors) {
                                addInterceptor(interceptor)
                            }
                            // 为 OkHttp 设置默认的线程池。
                            dispatcher(Dispatcher(executorService))
                            okhttpConfiguration(application, this)
                        } catch (exception: UnknownHostException) {
                            "检查网络是否正常".middleToast()
                        }
                    }
                    .build()
            }
            single { Function<Param<*>, Param<*>> { p -> p.apply { rxHandler.forEach { it(p) } } } }
        })
    }


    fun addModule(module: Module) {
        mKoinModule.add(module)
    }

    /**
     * 动态添加任意个interceptor
     */
    @Deprecated("未完成")
    fun addInterceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }

    /**
     * 用来处理http响应结果
     * 使用okhttp的方式添加请求头或者参数
     */
    fun setGlobalHttpHandler(handler: GlobalHttpHandler) {
        this@GlobalConfigModule.handler = handler
    }

    /**
     * 用来处理http响应结果
     * 使用 Rxhttp 的方式添加请求头或者参数
     */
    fun addGlobalRxHttpHandler(param: (Param<*>) -> Unit) {
        rxHandler.add(param)
    }

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     */
    fun setExecutorService(executorService: ExecutorService) {
        this@GlobalConfigModule.executorService = executorService
    }


    fun setOkhttpConfiguration(okhttpConfiguration: (context: Context, builder: OkHttpClient.Builder) -> Unit) {
        this@GlobalConfigModule.okhttpConfiguration = okhttpConfiguration
    }


    fun getModule() = mKoinModule
}

