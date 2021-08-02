package com.apesmedical.commonsdk.base.newbase

import com.apesmedical.commonsdk.http.ResultData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RemoteService {
    fun <T> request(
        clazz: Class<T>,
        method: Method
    ): Flow<ResultData<T>>

    suspend fun <T> requestByData(
        clazz: Class<T>,
        method: Method
    ): ResultData<T>

    fun <T> upload(
        clazz: Class<T>,
        coroutine: CoroutineScope,
        method: Method,
        progress: suspend (Progress) -> Unit
    ): Flow<ResultData<T>>

    fun <T> download(
        clazz: Class<T>,
        destPath: String,
        method: Method,
        progress: suspend (Progress) -> Unit
    ): Flow<String>
}