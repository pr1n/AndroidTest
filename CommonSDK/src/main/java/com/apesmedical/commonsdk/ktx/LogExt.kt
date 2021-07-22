package com.library.sdk.ext

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import com.apesmedical.commonsdk.R
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ToastUtils
import kotlin.reflect.jvm.jvmName

/**
 * Created by Beetle_Sxy on 2019/3/21.
 */

inline fun <reified T> T.logi(message: String) = Log.i("TAG", "${T::class.jvmName} :: $message")

fun Any?.v() = LogUtils.v(this)

fun Any?.d() = LogUtils.d(this)
fun Any?.d(tag: String) = LogUtils.dTag(tag, this)
fun Any?.i() = LogUtils.i(this)
fun Any?.w() = LogUtils.w(this)
fun Any?.e() = LogUtils.e(this)
fun Any?.e(tag: String) = LogUtils.eTag(tag, this)
fun Any?.a() = LogUtils.a(this)
fun Any?.json() = LogUtils.json(this)
fun Any?.json(tag: String) = LogUtils.json(tag, this)

fun Array<Any>.v() = LogUtils.v(this)
fun Array<Any>.d() = LogUtils.d(this)
fun Array<Any>.i() = LogUtils.i(this)
fun Array<Any>.w() = LogUtils.w(this)
fun Array<Any>.e() = LogUtils.e(this)
fun Array<Any>.a() = LogUtils.a(this)

fun <T : CharSequence> T?.toast(): T? {
	//适配 Android R 无法弹出问题
	//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
	//Toast.makeText(Utils.getApp(), this, Toast.LENGTH_SHORT).show()
	//else
	//ToastUtils.showShort(this)
	this?.middleToast()
	return this
}


fun <T : CharSequence> T?.snack(v: View, apply: (SnackbarUtils) -> Unit = {}) {
    SnackbarUtils.with(v)
        .setMessage(this ?: "")
        .apply { apply(this) }
        .show()
}

fun <T : CharSequence> T?.middleToast() {
    ToastUtils.make()
        .setBgResource(R.drawable.toast_bg)
//        .setBgColor(Color.parseColor("#22232F"))
        .setGravity(Gravity.CENTER, 0, 0)
        .setTextColor(Color.WHITE)
        .setDurationIsLong(false)
        .show(this)

}


