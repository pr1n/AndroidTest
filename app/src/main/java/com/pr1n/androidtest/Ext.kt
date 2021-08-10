package com.pr1n.androidtest

import android.view.View
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.blankj.utilcode.util.GsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KProperty

fun View.onClick(
    context: CoroutineContext = Dispatchers.Main,
    handler: suspend CoroutineScope.(v: View) -> Unit
) {

    this.setOnClickListener {
        GlobalScope.launch(context, CoroutineStart.DEFAULT) {
            handler(it)
        }
    }
}

operator fun UserDelegate.getValue(thisRef: Any?, property: KProperty<*>) =
    GsonUtils.fromJson(userJson, User::class.java) ?: User.Empty

operator fun UserDelegate.setValue(thisRef: Any?, property: KProperty<*>, value: User) {
    userJson = GsonUtils.toJson(value)
}