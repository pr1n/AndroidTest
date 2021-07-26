package com.apesmedical.commonsdk.base.newbase

import com.apesmedical.commonsdk.http.parserToFlow
import kotlinx.coroutines.CoroutineScope
import rxhttp.asFlow
import rxhttp.toDownload
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.upload
import kotlin.coroutines.EmptyCoroutineContext

class RxHttpRemoteService : RemoteService {
    override fun <T : Any> get(
        clazz: Class<T>,
        builder: IRequestBuilder.() -> Unit
    ) = buildHttpClient(builder)
        .parserToFlow(clazz)

    override fun <T : Any> post(
        clazz: Class<T>,
        builder: IRequestBuilder.() -> Unit
    ) = buildHttpClient(builder)
        .parserToFlow(clazz)

    override fun <T : Any> upload(
        clazz: Class<T>,
        coroutine: CoroutineScope,
        builder: IUploadBuilder.() -> Unit,
        progress: suspend (Progress) -> Unit
    ) = buildHttpClient(builder)
        .upload(coroutine)
        { progress(Progress(it.progress, it.currentSize, it.totalSize)) }
        .parserToFlow(clazz)

    override fun <T : Any> download(
        clazz: Class<T>,
        destPath: String,
        builder: IRequestBuilder.() -> Unit,
        progress: suspend (Progress) -> Unit
    ) = buildHttpClient(builder)
        .toDownload(destPath, EmptyCoroutineContext)
        { progress(Progress(it.progress, it.currentSize, it.totalSize)) }
        .asFlow()

    private fun buildHttpClient(builder: IRequestBuilder.() -> Unit) = with(Request.Builder()) {
        builder(this)
        val request = this.build()
        (if (request.urlFormat != null) RxHttp.postForm(
            request.urlFormat.first, request.urlFormat.second
        ) else RxHttp.postForm(request.url)).also {
            if (request.config != null) {
                it.connectTimeout(request.config.connectTimeout.toInt())
                it.readTimeout(request.config.readTimeout.toInt())
                it.writeTimeout(request.config.writeTimeout.toInt())
            }
            if (request.headers.isNotEmpty()) it.addAllHeader(request.headers)
            if (request.params.isNotEmpty()) it.addAll(request.params)
            if (request.querys.isNotEmpty()) it.addAllQuery(request.querys)
            if (request.files.isNotEmpty()) it.addFiles(request.files)
            if (request.parts.isNotEmpty()) request.parts.forEach(it::addPart)
        }
    }
}