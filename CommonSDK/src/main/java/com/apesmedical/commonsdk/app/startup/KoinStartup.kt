package com.apesmedical.commonsdk.app.startup

import android.app.Application
import android.content.Context
import com.apesmedical.commonsdk.http.GlobalHttpHandler
import com.library.sdk.ext.logi
import com.library.sdk.ext.middleToast
import com.rousetime.android_startup.AndroidStartup
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import rxhttp.wrapper.callback.Function
import rxhttp.wrapper.param.Param
import rxhttp.wrapper.ssl.HttpsUtils
import java.net.UnknownHostException
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class KoinStartup : AndroidStartup<KoinApplication>() {
    override fun callCreateOnMainThread() = false
    override fun waitOnMainThread() = true

    companion object {
        private const val TIME_OUT = 10L
    }

    private val interceptors = mutableListOf<Interceptor>()
    private var handler: GlobalHttpHandler = GlobalHttpHandler
    private var rxHandler: MutableList<(Param<*>) -> Unit> = mutableListOf()
    private var okhttpConfiguration: (context: Context, builder: OkHttpClient.Builder) -> Unit =
        { _, _ -> }

    private var executorService: ExecutorService = ThreadPoolExecutor(
        0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
        SynchronousQueue()
    ) { run -> Thread(run, "apes Executor") }

    override fun create(context: Context): KoinApplication {
        logi("Koin Init....")
        return startKoin {
            //androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)//用来打印日志
            androidLogger(Level.DEBUG)//用来打印日志
            androidContext(context)//向Koin中注入context
            modules(module {
                single { OkHttpClient.Builder() }
                single {
                    val builder: OkHttpClient.Builder = get()
                    val application: Application = get()
                    val sslParams = HttpsUtils.getSslSocketFactory()
                    builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .sslSocketFactory(
                            sslParams.sSLSocketFactory,
                            sslParams.trustManager
                        ) //添加信任证书
                        .addInterceptor(HttpLoggingInterceptor().also {
                            it.level = HttpLoggingInterceptor.Level.BODY
                        })
                        .hostnameVerifier { _, _ -> true } //忽略host验证
                        .dispatcher(Dispatcher(executorService))
                        .apply {
                            try {
                                addInterceptor(Interceptor { chain ->
                                    chain.proceed(
                                        handler.onHttpRequestBefore(chain, chain.request())
                                    )
                                })
                                //如果外部提供了interceptor的集合则遍历添加
                                for (interceptor in interceptors) addInterceptor(interceptor)

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
    }
}