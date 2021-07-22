package com.pr1n.androidtest

import com.blankj.utilcode.util.GsonUtils
import kotlin.reflect.KProperty

operator fun UserDelegate.getValue(thisRef: Any?, property: KProperty<*>) =
    GsonUtils.fromJson(userJson, User::class.java) ?: User.Empty

operator fun UserDelegate.setValue(thisRef: Any?, property: KProperty<*>, value: User) {
    userJson = GsonUtils.toJson(value)
}