package com.apesmedical.commonsdk.base.newbase

import okhttp3.RequestBody
import java.io.File

class Request private constructor(
    val config: RequestConfig?,
    val url: String?,
    val urlFormat: Pair<String, Array<out Any>>?,
    val headers: Map<String, String>,
    val params: Map<String, Any>,
    val querys : Map<String, Any>,
    val files : Map<String, File>,
    val parts: List<RequestBody>
) {
    class Builder : IRequestBuilder {
        private val configBuilder = RequestConfig.Builder()
        private var config: RequestConfig? = null
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
        override fun setUrlFormat(formatUrl: String, vararg formats: Any){ this.urlFormat = formatUrl to formats  }
        override fun addHeader(key: String, value: String) { this.headers[key] = value }
        override fun addHeaders(vararg headers: Pair<String, String>) { this.headers += headers }
        override fun addParam(key: String, value: Any) { this.params[key] = value }
        override fun addParams(vararg params: Pair<String, Any>) { this.params += params }
        override fun addQuery(key: String, value: Any) { this.querys[key] = value }
        override fun addQuerys(vararg querys: Pair<String, Any>) { this.querys += querys }
        override fun addFile(name: String, file: File) { this.files[name] = file }
        override fun addFiles(vararg files: Pair<String, File>) { this.files += files }
        override fun addPart(requestBody: RequestBody) { this.parts += requestBody }
        fun build() = Request(config, urlStr, urlFormat, headers, params, querys, files, parts)
    }
}