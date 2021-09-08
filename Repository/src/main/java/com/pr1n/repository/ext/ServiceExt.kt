package com.pr1n.repository.ext

import com.pr1n.repository.remote.RemoteService
import com.pr1n.repository.remote.base.Method
import com.pr1n.repository.remote.base.Progress
import kotlinx.coroutines.CoroutineScope

inline fun <reified T> RemoteService.request(method: Method) =
    request(T::class.java, method)

inline fun <reified T> RemoteService.requestToList(method: Method) =
    requestToList(T::class.java, method)

suspend inline fun <reified T> RemoteService.requestByData(method: Method) =
    requestByData(T::class.java, method)

suspend inline fun <reified T> RemoteService.requestByDatas(method: Method) =
    requestByDatas(T::class.java, method)

inline fun <reified T> RemoteService.upload(
    coroutine: CoroutineScope,
    method: Method,
    noinline progress: suspend (Progress) -> Unit
) = upload(T::class.java, coroutine, method, progress)

inline fun <reified T> RemoteService.download(
    destPath: String, method: Method, noinline progress: suspend (Progress) -> Unit
) = download(T::class.java, destPath, method, progress)