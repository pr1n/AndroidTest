package com.apesmedical.commonsdk.http

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import rxhttp.IRxHttp
import rxhttp.asFlow
import rxhttp.toParser

fun <T> IRxHttp.parserToFlow(clazz: Class<T>) =
    toParser(ResponseParser<T>(clazz))
        .asFlow()
        .onStart { emit(Loading()) }
        .catch { emit(Failure(it.message ?: "", it)) }
        .onCompletion { emit(Complete()) }