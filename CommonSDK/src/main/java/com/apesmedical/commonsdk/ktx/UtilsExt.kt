package com.library.sdk.ext

import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.SpanUtils

/**
 * Created by Beetle_Sxy on 2019-07-25.
 */

fun <T : TextView> T.span(block: SpanUtils.() -> Unit) = SpanUtils.with(this).apply(block).create()

fun Float.sp() = ConvertUtils.sp2px(this)
fun Int.sp() = ConvertUtils.sp2px(this.toFloat())
fun Float.dp() = ConvertUtils.dp2px(this)
fun Int.dp() = ConvertUtils.dp2px(this.toFloat())

val Float.dp get() = ConvertUtils.sp2px(this)
val Int.dp get() = ConvertUtils.dp2px(this.toFloat())
val Float.sp get() = ConvertUtils.sp2px(this)
val Int.sp get() = ConvertUtils.sp2px(this.toFloat())

