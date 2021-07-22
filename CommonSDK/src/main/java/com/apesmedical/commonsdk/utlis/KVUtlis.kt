package com.apesmedical.commonsdk.utlis

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmName

/**
 * Created by Beetle_Sxy on 2020/10/9.
 * MMKV 封装
 * 因为 DataStore 还是 alpha 版本。
 * 请勿使用过多使用。
 */

infix fun <B> String.KVget(value: B): B? = KVUtlis.get(this, value)
infix fun <B> String.KVset(value: B?): Boolean = KVUtlis.set(this, value)
infix fun String.KVset(value: Set<String>): Boolean = KVUtlis.set(this, value)
fun String.KVremove() = KVUtlis.remove(this)

object KVUtlis {

    private val _kv by lazy { MMKV.defaultMMKV() }

    fun <T> set(key: String, value: T?): Boolean {
        when (value) {
            is String -> _kv?.encode(key, value)
            is Float -> _kv?.encode(key, value)
            is Boolean -> _kv?.encode(key, value)
            is Int -> _kv?.encode(key, value)
            is Long -> _kv?.encode(key, value)
            is Double -> _kv?.encode(key, value)
            is ByteArray -> _kv?.encode(key, value)
            is Parcelable -> _kv?.encode(key, value)
            is Nothing -> return false
            else -> return false
        }
        return true
    }

    fun set(key: String, value: Set<String>) = _kv?.encode(key, value) ?: false


    fun <T> get(key: String, defaultValue: T): T? {
        val value = when (defaultValue) {
            is String -> _kv?.decodeString(key, defaultValue)
            is Float -> _kv?.decodeFloat(key, defaultValue)
            is Boolean -> _kv?.decodeBool(key, defaultValue)
            is Int -> _kv?.decodeInt(key, defaultValue)
            is Long -> _kv?.decodeLong(key, defaultValue)
            is Double -> _kv?.decodeDouble(key, defaultValue)
            is ByteArray -> _kv?.decodeBytes(key, defaultValue)
            else -> return null
        }
        return value as? T
    }

    fun <T : Parcelable?> getMmkvParcelable(key: String, t: Class<T>?): T? =
        _kv?.decodeParcelable(key, t)


    fun clearAll() = _kv?.clearAll()
    fun remove(key: String) = _kv?.remove(key)

    fun getAll() = _kv?.all
}

class KVDelegate<T>(private val default: T, private val name: String) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        KVUtlis.get("${name}_${property.name}", default) ?: default

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        KVUtlis.set("${name}_${property.name}", value)
    }
}

inline fun <reified R, T> R.mmkv(value: T) = KVDelegate(value, R::class.jvmName)