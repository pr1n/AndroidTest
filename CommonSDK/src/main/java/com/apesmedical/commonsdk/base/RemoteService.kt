package com.apesmedical.commonsdk.base

import com.apesmedical.commonsdk.http.ResultData
import com.apesmedical.commonsdk.http.parserToFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import rxhttp.IRxHttp
import rxhttp.toParser
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.RxHttpFormParam
import rxhttp.wrapper.parse.SimpleParser

interface RemoteService {
    suspend fun <T : Any> postToAwait(
        clazz: Class<T>,
        url: String,
        vararg requestArgs: Pair<String, Any> = arrayOf(),
        before: (suspend (RxHttpFormParam) -> IRxHttp)? = null,
    ): T

    fun <T : Any> postToFlow(
        clazz: Class<T>,
        url: String,
        vararg requestArgs: Pair<String, Any> = arrayOf(),
    ): Flow<ResultData<T>>

    companion object : RemoteService {
        override suspend fun <T : Any> postToAwait(
            clazz: Class<T>,
            url: String,
            vararg requestArgs: Pair<String, Any>,
            before: (suspend (RxHttpFormParam) -> IRxHttp)?
        ) = RxHttp.postForm(url).run {
            if (requestArgs.isNotEmpty())
                requestArgs.forEach { (key, value) -> this.add(key, value) }
            (before?.invoke(this) ?: this).also { delay(2000) }
        }.toParser(SimpleParser[clazz]).await()

        override fun <T : Any> postToFlow(
            clazz: Class<T>,
            url: String,
            vararg requestArgs: Pair<String, Any>
        ) = RxHttp.postForm(url).also {
            if (requestArgs.isNotEmpty())
                requestArgs.forEach { (key, value) -> it.add(key, value) }
        }.parserToFlow(clazz)
    }
}