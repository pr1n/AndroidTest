package com.apesmedical.commonsdk.base.newbase

import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.RxHttpFormParam

class RxHttpBuilder(private val method: Method) {
    fun init() = run {
        val request = method.convert()
        val config = request.config
        when (method) {
            is Get ->
                if (request.urlFormat == null) RxHttp.get(request.url)
                else RxHttp.get(request.urlFormat.first, request.urlFormat.second)
            is Head ->
                if (request.urlFormat == null) RxHttp.head(request.url)
                else RxHttp.head(request.urlFormat.first, request.urlFormat.second)
            is Post ->
                if (request.urlFormat == null) RxHttp.postForm(request.url)
                else RxHttp.postForm(request.urlFormat.first, request.urlFormat.second)
            is Put ->
                if (request.urlFormat == null) RxHttp.putForm(request.url)
                else RxHttp.putForm(request.urlFormat.first, request.urlFormat.second)
            is Delete ->
                if (request.urlFormat == null) RxHttp.deleteForm(request.url)
                else RxHttp.deleteForm(request.urlFormat.first, request.urlFormat.second)
        }.also {
            it.connectTimeout(config.connectTimeout.toInt())
            it.readTimeout(config.readTimeout.toInt())
            it.writeTimeout(config.writeTimeout.toInt())

            when (method) {
                is Get, is Head ->
                    when {
                        request.headers.isNotEmpty() -> it.addAllHeader(request.headers)
                        request.querys.isNotEmpty() -> it.addAllQuery(request.querys)
                    }
                else ->
                    when {
                        request.headers.isNotEmpty() -> it.addAllHeader(request.headers)
                        request.params.isNotEmpty() -> (it as RxHttpFormParam).addAll(request.params)
                        request.querys.isNotEmpty() -> it.addAllQuery(request.querys)
                        request.files.isNotEmpty() -> (it as RxHttpFormParam).addFiles(request.files)
                        request.parts.isNotEmpty() -> request.parts.forEach((it as RxHttpFormParam)::addPart)
                    }
            }
        }
    }
}