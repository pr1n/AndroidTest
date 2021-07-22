package com.pr1n.androidtest

import rxhttp.wrapper.annotation.DefaultDomain

@DefaultDomain
const val BASE_URL = "https://juejin.cn/"

const val POST_URL = "https://juejin.cn/post/6844904100090347528#heading-3"

//suspend inline fun <reified T : Any> postToAwait(
//    url: String,
//    vararg requestMaps: Pair<String, String> = arrayOf(),
//    noinline block: ((RxHttpFormParam) -> IRxHttp)? = null,
//) =
//    RxHttp.postForm(url).run {
//        if (requestMaps.isNotEmpty())
//            requestMaps.forEach { (key, value) -> this.add(key, value) }
//        block?.invoke(this) ?: this
//    }.toResponse<T>().await()
//
//inline fun <reified T : Any> postToFlow(
//    url: String,
//    vararg requestMaps: Pair<String, String> = arrayOf(),
//) =
//    RxHttp.postForm(url).also {
//        if (requestMaps.isNotEmpty())
//            requestMaps.forEach { (key, value) -> it.add(key, value) }
//    }.toResponse<T>().asFlow()