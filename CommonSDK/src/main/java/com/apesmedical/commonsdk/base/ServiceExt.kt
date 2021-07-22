package com.apesmedical.commonsdk.base

import rxhttp.IRxHttp
import rxhttp.wrapper.param.RxHttpFormParam

suspend inline fun <reified T : Any> RemoteService.postToAwait(
    url: String,
    vararg requestArgs: Pair<String, Any> = arrayOf(),
    noinline before: (suspend (RxHttpFormParam) -> IRxHttp)? = null,
) = postToAwait(T::class.java, url, *requestArgs, before = before)

inline fun <reified T : Any> RemoteService.postToFlow(
    url: String,
    vararg requestArgs: Pair<String, Any> = arrayOf(),
) = postToFlow(T::class.java, url, *requestArgs)