package com.apesmedical.commonsdk.base.newbase

import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

interface IConfig {
    fun config(builder: IConfigBuilder.() -> Unit)
}

interface IConfigBuilder {
    fun connectTimeout(timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS)
    fun readTimeout(timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS)
    fun writeTimeout(timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS)
}

interface IUrlBuilder {
    fun setUrl(url: String)
    fun setUrlFormat(formatUrl: String, vararg formats: Any)
}

interface IParamBuilder : IConfig {
    fun addHeader(key: String, value: String)
    fun addHeaders(vararg headers: Pair<String, String>)
    fun addParam(key: String, value: Any)
    fun addParams(vararg params: Pair<String, Any>)
    fun addQuery(key: String, value: Any)
    fun addQuerys(vararg querys: Pair<String, Any>)
    fun addPart(requestBody: RequestBody)
}

interface IUploadBuilder : IConfig, IUrlBuilder {
    fun addFile(name: String, file: File)
    fun addFiles(vararg files: Pair<String, File>)
}

interface IRequestBuilder : IConfig, IUrlBuilder, IParamBuilder, IUploadBuilder

