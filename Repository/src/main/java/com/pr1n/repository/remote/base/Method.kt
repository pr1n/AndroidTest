package com.pr1n.repository.remote.base

import okhttp3.RequestBody
import java.io.File

sealed class Method(lambda: IParamBuilder.() -> Unit) {

    private val method = Method

    init {
        lambda(method)
    }

    fun convert() = Companion.convert()

    private companion object : IParamBuilder {
        private val configBuilder = RequestConfig.Builder()
        private var config: RequestConfig = RequestConfig.Builder().build()
        private var urlStr: String? = ""
        private var urlFormat: Pair<String, Array<out Any>>? = null
        private val headers = mutableMapOf<String, String>()
        private val params = mutableMapOf<String, Any>()
        private val querys = mutableMapOf<String, Any>()
        private val files = mutableMapOf<String, File>()
        private val parts = mutableListOf<RequestBody>()
        override fun config(builder: IConfigBuilder.() -> Unit) {
            config = configBuilder.let {
                builder(it)
                it.build()
            }
        }
        override fun setUrl(url: String) { urlStr = url }
        override fun setUrlFormat(formatUrl: String, vararg formats: Any) { urlFormat = formatUrl to formats }
        override fun addHeader(key: String, value: String) { headers[key] = value }
        override fun addHeaders(vararg headers: Pair<String, String>) { Companion.headers += headers }
        override fun addParam(key: String, value: Any) { params[key] = value }
        override fun addParams(vararg params: Pair<String, Any>) { Companion.params += params }
        override fun addQuery(key: String, value: Any) { querys[key] = value }
        override fun addQuerys(vararg querys: Pair<String, Any>) { Companion.querys += querys }
        override fun addFile(name: String, file: File) { files[name] = file }
        override fun addFiles(vararg files: Pair<String, File>) { Companion.files += files }
        override fun addPart(requestBody: RequestBody) { parts += requestBody }
        fun convert() = Request(config, urlStr, urlFormat, headers, params, querys, files, parts)
    }
}

operator fun Method.plus(method: Method): Method {
    return Post{
        val request = method.convert()
        setUrl(request.url ?: "")
        if (request.urlFormat != null)
            setUrlFormat(request.urlFormat.first, request.urlFormat.second)
        config {
            connectTimeout(request.config.connectTimeout)
            readTimeout(request.config.readTimeout)
            writeTimeout(request.config.writeTimeout)
        }
        addHeaders(*request.headers.toList().toTypedArray())
        addQuerys(*request.querys.toList().toTypedArray())
        addParams(*request.params.toList().toTypedArray())
        addFiles(*request.files.toList().toTypedArray())
        request.parts.toList().forEach(::addPart)
    }
}