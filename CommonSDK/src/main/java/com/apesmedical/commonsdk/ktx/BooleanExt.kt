package com.library.sdk.ext

/**
 * Created by Beetle_Sxy on 2019-06-24.
 */

fun Int?.toBoolean() = this == 1

fun Long?.toBoolean() = this == 1L

fun String?.toBoolean() = this == "1"