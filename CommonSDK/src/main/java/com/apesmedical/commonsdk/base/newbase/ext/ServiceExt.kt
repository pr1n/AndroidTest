package com.apesmedical.commonsdk.base.newbase.ext

import com.apesmedical.commonsdk.base.newbase.IRequestBuilder
import com.apesmedical.commonsdk.base.newbase.IUploadBuilder
import com.apesmedical.commonsdk.base.newbase.Progress
import com.apesmedical.commonsdk.base.newbase.RemoteService
import kotlinx.coroutines.CoroutineScope

inline fun <reified T : Any> RemoteService.get(
    noinline request: IRequestBuilder.() -> Unit
) = get(T::class.java, request)

inline fun <reified T : Any> RemoteService.post(
    noinline request: IRequestBuilder.() -> Unit
) = post(T::class.java, request)

inline fun <reified T : Any> RemoteService.upload(
    coroutine: CoroutineScope,
    noinline request: IUploadBuilder.() -> Unit,
    noinline progress: suspend (Progress) -> Unit
) = upload(T::class.java, coroutine, request, progress)

inline fun <reified T : Any> RemoteService.download(
    destPath: String,
    noinline request: IRequestBuilder.() -> Unit,
    noinline progress: suspend (Progress) -> Unit
) = download(T::class.java, destPath, request, progress)