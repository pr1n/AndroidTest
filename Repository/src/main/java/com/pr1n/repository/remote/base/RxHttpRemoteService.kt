package com.pr1n.repository.remote.base

import com.apesmedical.commonsdk.http.ResponseParser
import com.apesmedical.commonsdk.http.parserToFlow
import com.pr1n.repository.remote.RemoteService
import kotlinx.coroutines.CoroutineScope
import rxhttp.asFlow
import rxhttp.toDownload
import rxhttp.toParser
import rxhttp.wrapper.entity.ParameterizedTypeImpl
import rxhttp.wrapper.param.RxHttpFormParam
import rxhttp.wrapper.param.upload
import kotlin.coroutines.EmptyCoroutineContext

class RxHttpRemoteService : RemoteService {
    override fun <T> request(
        clazz: Class<T>,
        method: Method
    ) = RxHttpBuilder(method).init().parserToFlow(ResponseParser<T>(clazz))

    override fun <T> requestToList(
        clazz: Class<T>,
        method: Method
    ) = RxHttpBuilder(method).init().parserToFlow(ResponseParser<List<T>>(ParameterizedTypeImpl[List::class.java, clazz]))

    override suspend fun <T> requestByData(
        clazz: Class<T>,
        method: Method
    ) = RxHttpBuilder(method).init()
        .toParser(ResponseParser<T>(clazz))
        .await()

    override suspend fun <T> requestByDatas(
        clazz: Class<T>,
        method: Method
    ) = RxHttpBuilder(method).init()
            .toParser(ResponseParser<List<T>>(ParameterizedTypeImpl[List::class.java, clazz]))
            .await()

    override fun <T> upload(
        clazz: Class<T>,
        coroutine: CoroutineScope,
        method: Method,
        progress: suspend (Progress) -> Unit
    ) = (RxHttpBuilder(method).init() as RxHttpFormParam)
        .upload(coroutine)
        { progress(Progress(it.progress, it.currentSize, it.totalSize)) }
        .parserToFlow(ResponseParser<T>(clazz))

    override fun <T> download(
        clazz: Class<T>,
        destPath: String,
        method: Method,
        progress: suspend (Progress) -> Unit
    ) = RxHttpBuilder(method).init()
        .toDownload(destPath, EmptyCoroutineContext)
        { progress(Progress(it.progress, it.currentSize, it.totalSize)) }
        .asFlow()
}