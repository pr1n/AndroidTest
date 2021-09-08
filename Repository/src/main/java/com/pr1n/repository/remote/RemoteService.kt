package com.pr1n.repository.remote

import com.apesmedical.commonsdk.http.ResultData
import com.pr1n.repository.remote.base.Method
import com.pr1n.repository.remote.base.Progress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RemoteService {
    fun <T> request(
        clazz: Class<T>,
        method: Method
    ): Flow<ResultData<T>>

    fun <T> requestToList(
        clazz: Class<T>,
        method: Method
    ): Flow<ResultData<List<T>>>

    suspend fun <T> requestByData(
        clazz: Class<T>,
        method: Method
    ): ResultData<T>

    suspend fun <T> requestByDatas(
        clazz: Class<T>,
        method: Method
    ): ResultData<List<T>>

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