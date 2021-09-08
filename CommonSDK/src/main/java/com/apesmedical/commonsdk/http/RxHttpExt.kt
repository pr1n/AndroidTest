package com.apesmedical.commonsdk.http

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import rxhttp.IRxHttp
import rxhttp.asFlow
import rxhttp.toParser

fun <T> IRxHttp.parserToFlow(responseParser: ResponseParser<T>) =
    toParser(responseParser)
        .asFlow()
        .onStart { emit(Loading()) }
        .catch { emit(Failure(it.message ?: "", it)) }
        .onCompletion {
            delay(100)
            emit(Complete())
        }