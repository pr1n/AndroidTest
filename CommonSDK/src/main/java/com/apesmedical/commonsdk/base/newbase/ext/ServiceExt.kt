package com.apesmedical.commonsdk.base.newbase.ext

import com.apesmedical.commonsdk.base.newbase.Method
import com.apesmedical.commonsdk.base.newbase.Progress
import com.apesmedical.commonsdk.base.newbase.RemoteService
import kotlinx.coroutines.CoroutineScope

inline fun <reified T : Any> RemoteService.request(method: Method) =
    request(T::class.java, method)

suspend inline fun <reified T : Any> RemoteService.requestByData(method: Method) =
    requestByData(T::class.java, method)

inline fun <reified T : Any> RemoteService.upload(
    coroutine: CoroutineScope,
    method: Method,
    noinline progress: suspend (Progress) -> Unit
) = upload(T::class.java, coroutine, method, progress)

inline fun <reified T : Any> RemoteService.download(
    destPath: String, method: Method, noinline progress: suspend (Progress) -> Unit
) = download(T::class.java, destPath, method, progress)