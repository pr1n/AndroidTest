package com.pr1n.androidtest

import android.os.Parcelable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.parcelize.Parcelize

@FlowPreview
fun <T> parceableFlow(block: suspend FlowCollector<T>.() -> Unit) = ParcelableFlow(block)


@Parcelize
@FlowPreview
class ParcelableFlow<T>(private val block: suspend FlowCollector<T>.() -> Unit) :
    AbstractFlow<T>(), Parcelable {
    override suspend fun collectSafely(collector: FlowCollector<T>) = collector.block()
}