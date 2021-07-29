package com.apesmedical.commonsdk.base.newbase

import okhttp3.RequestBody
import java.io.File

sealed class Method(lambda: IParamBuilder.() -> Unit) {

    private val method = Method

    init {
        lambda(method)
    }

    fun convert() = method.convert()

    private companion object : IParamBuilder{
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
        override fun setUrlFormat(formatUrl: String, vararg formats: Any) { this.urlFormat = formatUrl to formats }
        override fun addHeader(key: String, value: String) { this.headers[key] = value }
        override fun addHeaders(vararg headers: Pair<String, String>) { this.headers += headers }
        override fun addParam(key: String, value: Any) { this.params[key] = value }
        override fun addParams(vararg params: Pair<String, Any>) { this.params += params }
        override fun addQuery(key: String, value: Any) { this.querys[key] = value }
        override fun addQuerys(vararg querys: Pair<String, Any>) { this.querys += querys }
        override fun addFile(name: String, file: File) { this.files[name] = file }
        override fun addFiles(vararg files: Pair<String, File>) { this.files += files }
        override fun addPart(requestBody: RequestBody) { this.parts += requestBody }
        fun convert() = Request(config, urlStr, urlFormat, headers, params, querys, files, parts)
    }
}

operator fun Method.plus(method: Method): Method{
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