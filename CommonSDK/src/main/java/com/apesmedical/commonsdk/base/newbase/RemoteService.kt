package com.apesmedical.commonsdk.base.newbase

import com.apesmedical.commonsdk.http.ResultData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RemoteService {
    fun <T : Any> get(
        clazz: Class<T>,
        builder: IRequestBuilder.() -> Unit
    ): Flow<ResultData<T>>

    fun <T : Any> post(
        clazz: Class<T>,
        builder: IRequestBuilder.() -> Unit
    ): Flow<ResultData<T>>

    fun <T : Any> upload(
        clazz: Class<T>,
        coroutine: CoroutineScope,
        builder: IUploadBuilder.() -> Unit,
        progress: suspend (Progress) -> Unit
    ): Flow<ResultData<T>>

    fun <T : Any> download(
        clazz: Class<T>,
        destPath: String,
        builder: IRequestBuilder.() -> Unit,
        progress: suspend (Progress) -> Unit
    ): Flow<String>
}