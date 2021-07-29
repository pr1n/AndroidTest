package com.apesmedical.commonsdk.base.newbase

import com.apesmedical.commonsdk.http.Failure
import com.apesmedical.commonsdk.http.ResponseApes
import com.apesmedical.commonsdk.http.ResultData
import com.apesmedical.commonsdk.http.Success
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class OkHttpBuilder(private val method: Method) {

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    fun newCall() = client.let {
        val request = method.convert()
        if (it.connectTimeoutMillis.toLong() == request.config.connectTimeout ||
            it.readTimeoutMillis.toLong() == request.config.readTimeout ||
            it.writeTimeoutMillis.toLong() == request.config.writeTimeout
        ) it
        else client.newBuilder()
            .connectTimeout(request.config.connectTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(request.config.readTimeout, TimeUnit.MILLISECONDS)
            .writeTimeout(request.config.writeTimeout, TimeUnit.MILLISECONDS)
            .build()
    }.newCall(newRequest())

    private fun newRequest(): Request {
        val request = method.convert()
        client.newBuilder()
            .connectTimeout(request.config.connectTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(request.config.readTimeout, TimeUnit.MILLISECONDS)
            .writeTimeout(request.config.writeTimeout, TimeUnit.MILLISECONDS)
            .build()
        return with(Request.Builder()) {
            val url =
                if (request.urlFormat == null) "${request.url}" else request.urlFormat.format()
            url(url)
            when (method) {
                is Get -> {
                    get()
                    headers(Headers.Builder().also { budiler ->
                        request.headers.forEach { budiler.add(it.key, it.value) }
                    }.build())
                    val queryFormatStr = request.querys.map { "?${it.key}=${it.value}" }
                        .fold(StringBuilder()) { acc, s -> acc.append(s) }
                    url("${url}$queryFormatStr")
                }
                is Head -> {
                    head()
                    headers(Headers.Builder().also { budiler ->
                        request.headers.forEach { budiler.add(it.key, it.value) }
                    }.build())
                    val queryFormatStr = request.querys.map { "?${it.key}=${it.value}" }
                        .fold(StringBuilder()) { acc, s -> acc.append(s) }
                    url("${url}$queryFormatStr")
                }
                is Post -> {
                    val queryFormatStr = request.querys.map { "?${it.key}=${it.value}" }
                        .fold(StringBuilder()) { acc, s -> acc.append(s) }
                    url("${url}$queryFormatStr")
                    val body = MultipartBody.Builder().also { multiBodyBuilder ->
                        val paramBody = FormBody.Builder().also { formBodyBuilder ->
                            request.params.forEach { entry ->
                                formBodyBuilder.add(entry.key, entry.value.toString())
                            }
                        }.build()
                        val headers = Headers.Builder().also { budiler ->
                            request.headers.forEach { budiler.add(it.key, it.value) }
                        }.build()
                        multiBodyBuilder.addPart(headers, paramBody)
                        request.parts.forEach(multiBodyBuilder::addPart)
                        request.files.forEach {
                            it.value.asRequestBody()
                            multiBodyBuilder.addFormDataPart(it.key, null, it.value.asRequestBody())
                        }
                    }.build()
                    post(body)
                }
                is Put -> {
                    val body = MultipartBody.Builder().also { multiBodyBuilder ->
                        val paramBody = FormBody.Builder().also { formBodyBuilder ->
                            request.params.forEach { entry ->
                                formBodyBuilder.add(entry.key, entry.value.toString())
                            }
                        }.build()
                        val headers = Headers.Builder().also { budiler ->
                            request.headers.forEach { budiler.add(it.key, it.value) }
                        }.build()
                        multiBodyBuilder.addPart(headers, paramBody)
                        request.parts.forEach(multiBodyBuilder::addPart)
                    }.build()
                    put(body)
                }
                is Delete -> {
                    val body = MultipartBody.Builder().also { multiBodyBuilder ->
                        val paramBody = FormBody.Builder().also { formBodyBuilder ->
                            request.params.forEach { entry ->
                                formBodyBuilder.add(entry.key, entry.value.toString())
                            }
                        }.build()
                        val headers = Headers.Builder().also { budiler ->
                            request.headers.forEach { budiler.add(it.key, it.value) }
                        }.build()
                        multiBodyBuilder.addPart(headers, paramBody)
                        request.parts.forEach(multiBodyBuilder::addPart)
                    }.build()
                    delete(body)
                }
            }
            build()
        }
    }
}

private fun Pair<String, Array<out Any>?>.format() =
    if (second.isNullOrEmpty()) first else java.lang.String.format(first, *second!!)

private suspend fun Call.enqueue() =
    suspendCancellableCoroutine<Response> { cancellableContinuation ->
        cancellableContinuation.invokeOnCancellation { this.cancel() }
        this.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) =
                cancellableContinuation.resumeWithException(e)

            override fun onResponse(call: Call, response: Response) =
                cancellableContinuation.resume(response)
        })
    }

fun <T> Call.asFlow(clazz: Class<T>? = null) =
    flow { emit(this@asFlow.enqueue()) }
        .transform<Response, ResultData<T>> {
            if (200 == it.code) {
                val resultString = it.body?.string() ?: ""
                val result =
                    if (clazz != null) Gson().fromJson<ResponseApes<T>>(resultString, clazz)
                    else Gson().fromJson(
                        resultString,
                        object : TypeToken<ResponseApes<T>>() {}.type
                    )
                if (200 == result.code) {
                    val data = result.data
                    if (result.data != null) emit(Success(data!!)) else throw Exception()
                } else throw Exception()
            } else throw Exception()
        }

suspend fun <T> Call.asData(clazz: Class<T>? = null): ResultData<T> = this.enqueue().let {
    val resultString = it.body?.string() ?: ""
    val result =
        if (clazz != null) Gson().fromJson<ResponseApes<T>>(resultString, clazz)
        else Gson().fromJson(resultString, object : TypeToken<ResponseApes<T>>() {}.type)
    if (200 == result.code) {
        if (result.data != null) Success(result.data!!) else Failure(
            "Failure",
            Exception("Failure")
        )
    } else Failure("Failure", Exception("Failure"))
}
