package com.library.sdk.ext

import com.blankj.utilcode.util.SPUtils

/**
 * Created by Beetle_Sxy on 2019-06-24.
 */
fun spCustom() = SPUtils.getInstance("SP_DATA")

fun <T> getSpData(key:String) = spCustom().all[key] as? T

fun putSpData(key: String, any: Any?) {
    when (any) {
        is String -> spCustom().put(key, any)
        is Int -> spCustom().put(key, any)
        is Long -> spCustom().put(key, any)
        is Float -> spCustom().put(key, any)
        is Boolean -> spCustom().put(key, any)
        else -> throw TypeNotPresentException("类型呢不存在 $key", ClassNotFoundException())
    }
}