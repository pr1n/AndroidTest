package com.android.sdklibrary.ext

/**
 * Created by Beetle_Sxy on 2019/1/7.
 * 约束扩展
 */

fun Int.constrain(min: Int = 0, max: Int = 100) = when {
    this > max -> max
    this < min -> min
    else -> this
}

fun Float.constrain(min: Float = 0f, max: Float = 100f) = when {
    this > max -> max
    this < min -> min
    else -> this
}

fun Long.constrain(min: Long = 0L, max: Long = 100L) = when {
    this > max -> max
    this < min -> min
    else -> this
}
