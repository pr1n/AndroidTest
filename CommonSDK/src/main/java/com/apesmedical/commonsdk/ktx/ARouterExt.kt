package com.android.sdklibrary.ext

//import com.example.component.ValData
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import java.io.Serializable

/**
 * Created by Beetle_Sxy on 2019/3/21.
 * ARouter 扩展
 */

fun String.navigation() = ARouter.getInstance().build(this).navigation()
fun <S> String.navigationService() = ARouter.getInstance().build(this).navigation() as? S
inline fun <reified S> navigationService() = ARouter.getInstance().navigation(S::class.java)

/*fun String.navigation(bundle: Bundle) = ARouter.getInstance().build(this)
        .withBundle(com.example.component.ValData.BUNDLE_BEAN, bundle)
        .navigation()*/

fun String.navigation(key: String, value: Any?) {
	/*val build = ARouter.getInstance().build(this)
	when (value) {
        is Byte -> build.withByte(key, value)
        is Char -> build.withChar(key, value)
        is Int -> build.withInt(key, value)
        is Long -> build.withLong(key, value)
        is Double -> build.withDouble(key, value)
        is Float -> build.withFloat(key, value)
        is Boolean -> build.withBoolean(key, value)
        is String -> build.withString(key, value)
        is Bundle -> build.withBundle(key, value)
        is ByteArray -> build.withByteArray(key, value)
        is CharArray -> build.withCharArray(key, value)
        is CharSequence -> build.withCharSequence(key, value)
        is Parcelable -> build.withParcelable(key, value)
        is Serializable -> build.withSerializable(key, value)
	}*/
	ARouter.getInstance().build(this).withHelp(key, value).navigation()
}

fun String.navigation(vararg pair: Pair<String, Any?>) {
	val build = ARouter.getInstance().build(this)
	pair.forEach {
		/*val second = it.second
		when (second) {
            is Byte -> build.withByte(it.first, second)
            is Char -> build.withChar(it.first, second)
            is Int -> build.withInt(it.first, second)
            is Long -> build.withLong(it.first, second)
            is Double -> build.withDouble(it.first, second)
            is Float -> build.withFloat(it.first, second)
            is Boolean -> build.withBoolean(it.first, second)
            is String -> build.withString(it.first, second)
            is Bundle -> build.withBundle(it.first, second)
            is ByteArray -> build.withByteArray(it.first, second)
            is CharArray -> build.withCharArray(it.first, second)
            is CharSequence -> build.withCharSequence(it.first, second)
            is Parcelable -> build.withParcelable(it.first, second)
            is Serializable -> build.withSerializable(it.first, second)
			else -> build.withObject(it.first, second)
		}*/
		build.withHelp(it.first, it.second)
	}
	build.navigation()
}

fun <T : Fragment> String.navigationFm() = ARouter.getInstance().build(this).navigation() as? T

/*fun String.navigationFm(bundle: Bundle) = ARouter.getInstance().build(this)
        .withBundle(com.example.component.ValData.BUNDLE_BEAN, bundle)
        .navigation() as Fragment*/

fun <T : Fragment> String.navigationFm(key: String, value: Any?): T? {
	/*val build = ARouter.getInstance().build(this)
	when (value) {
		is Byte -> build.withByte(key, value)
		is Char -> build.withChar(key, value)
		is Int -> build.withInt(key, value)
		is Long -> build.withLong(key, value)
		is Double -> build.withDouble(key, value)
		is Float -> build.withFloat(key, value)
		is Boolean -> build.withBoolean(key, value)
		is String -> build.withString(key, value)
		is Bundle -> build.withBundle(key, value)
		is ByteArray -> build.withByteArray(key, value)
		is CharArray -> build.withCharArray(key, value)
		is CharSequence -> build.withCharSequence(key, value)
		is Parcelable -> build.withParcelable(key, value)
		is Serializable -> build.withSerializable(key, value)
	}
	build.withHelp(key, value)*/
	return ARouter.getInstance().build(this).withHelp(key, value).navigation() as? T
}

fun <T : Fragment> String.navigationFm(vararg pair: Pair<String, Any?>): T? {
	val build = ARouter.getInstance().build(this)
	pair.forEach {
		/*val second = it.second
		when (second) {
            is Byte -> build.withByte(it.first, second)
            is Char -> build.withChar(it.first, second)
            is Int -> build.withInt(it.first, second)
            is Long -> build.withLong(it.first, second)
            is Double -> build.withDouble(it.first, second)
            is Float -> build.withFloat(it.first, second)
            is Boolean -> build.withBoolean(it.first, second)
            is String -> build.withString(it.first, second)
            is Bundle -> build.withBundle(it.first, second)
            is ByteArray -> build.withByteArray(it.first, second)
            is CharArray -> build.withCharArray(it.first, second)
            is CharSequence -> build.withCharSequence(it.first, second)
            is Parcelable -> build.withParcelable(it.first, second)
            is Serializable -> build.withSerializable(it.first, second)
		}*/
		build.withHelp(it.first, it.second)
	}
	return build.navigation() as? T
}

private fun Postcard.withHelp(key: String, second: Any?) = when (second) {
	is Byte -> this.withByte(key, second)
	is Char -> this.withChar(key, second)
	is Int -> this.withInt(key, second)
	is Long -> this.withLong(key, second)
	is Double -> this.withDouble(key, second)
	is Float -> this.withFloat(key, second)
	is Boolean -> this.withBoolean(key, second)
	is String -> this.withString(key, second)
	is Bundle -> this.withBundle(key, second)
	is ByteArray -> this.withByteArray(key, second)
	is CharArray -> this.withCharArray(key, second)
	is CharSequence -> this.withCharSequence(key, second)
	is Parcelable -> this.withParcelable(key, second)
	is Serializable -> this.withSerializable(key, second)
	else -> this.withObject(key, second)
}



