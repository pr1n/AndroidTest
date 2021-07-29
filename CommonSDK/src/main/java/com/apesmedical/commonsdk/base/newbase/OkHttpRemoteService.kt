package com.apesmedical.commonsdk.base.newbase

import com.apesmedical.commonsdk.http.ResultData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow

class OkHttpRemoteService : RemoteService {
    override fun <T : Any> request(
        clazz: Class<T>,
        method: Method
    ) = OkHttpBuilder(method).newCall().asFlow(clazz)

    override suspend fun <T : Any> requestByData(clazz: Class<T>, method: Method): ResultData<T> =
        OkHttpBuilder(method).newCall().asData(clazz)

    override fun <T : Any> upload(
        clazz: Class<T>,
        coroutine: CoroutineScope,
        method: Method,
        progress: suspend (Progress) -> Unit
    ) = OkHttpBuilder(method).newCall().asFlow(clazz)

    override fun <T : Any> download(
        clazz: Class<T>,
        destPath: String,
        method: Method,
        progress: suspend (Progress) -> Unit
    ) = flow { emit("") }
}