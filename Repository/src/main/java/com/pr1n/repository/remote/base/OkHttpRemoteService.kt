package com.pr1n.repository.remote.base

import com.apesmedical.commonsdk.http.ResultData
import com.pr1n.repository.remote.RemoteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OkHttpRemoteService : RemoteService {
    override fun <T> request(
        clazz: Class<T>,
        method: Method
    ) = OkHttpBuilder(method).newCall().asFlow(clazz)

    override fun <T> requestToList(
        clazz: Class<T>,
        method: Method
    ): Flow<ResultData<List<T>>> = OkHttpBuilder(method).newCall().asFlow(clazz) as Flow<ResultData<List<T>>>

    override suspend fun <T> requestByData(clazz: Class<T>, method: Method): ResultData<T> =
        OkHttpBuilder(method).newCall().asData(clazz)

    override suspend fun <T> requestByDatas(clazz: Class<T>, method: Method): ResultData<List<T>> =
        OkHttpBuilder(method).newCall().asData(clazz) as ResultData<List<T>>

    override fun <T> upload(
        clazz: Class<T>,
        coroutine: CoroutineScope,
        method: Method,
        progress: suspend (Progress) -> Unit
    ) = OkHttpBuilder(method).newCall().asFlow(clazz)

    override fun <T> download(
        clazz: Class<T>,
        destPath: String,
        method: Method,
        progress: suspend (Progress) -> Unit
    ) = flow { emit("") }
}