package com.pr1n.repository.remote.base

import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

interface IConfigBuilder {
    fun connectTimeout(timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS)
    fun readTimeout(timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS)
    fun writeTimeout(timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS)
}

interface IConfig {
    fun config(builder: IConfigBuilder.() -> Unit)
}

interface IUrlBuilder {
    fun setUrl(url: String)
    fun setUrlFormat(formatUrl: String, vararg formats: Any)
}

interface INoBodyParamBuilder : IConfig, IUrlBuilder {
    fun addHeader(key: String, value: String)
    fun addHeaders(vararg headers: Pair<String, String>)
    fun addQuery(key: String, value: Any)
    fun addQuerys(vararg querys: Pair<String, Any>)
}

interface IParamBuilder : INoBodyParamBuilder {
    fun addParam(key: String, value: Any)
    fun addParams(vararg params: Pair<String, Any>)
    fun addPart(requestBody: RequestBody)
    fun addFile(name: String, file: File)
    fun addFiles(vararg files: Pair<String, File>)
}