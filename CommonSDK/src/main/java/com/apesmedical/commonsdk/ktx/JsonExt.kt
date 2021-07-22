package com.library.sdk.ext

/**
 * Created by Beetle_Sxy on 2019/3/21.
 * 有关 Json 扩展
 */

/**
 * 判断是为 json 字符串
 */
fun String?.isJson():Boolean{
    if (this == null || this.isEmpty()) return false
    val strChar = this.trim().toCharArray()
    if (strChar[0] == '{' || strChar[0] == '[') {
        if (strChar[strChar.size -1] == '}' || strChar[strChar.size -1] == ']') return true
        return false
    } else {
        return false
    }
}