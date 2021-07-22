package com.library.sdk.ext

import com.google.gson.reflect.TypeToken

/**
 * Created by Beetle_Sxy on 2019-07-10.
 */
inline fun <reified T> genericType() = object : TypeToken<T>() {}.type
