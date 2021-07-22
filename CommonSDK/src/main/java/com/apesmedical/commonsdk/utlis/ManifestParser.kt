package com.apesmedical.commonsdk.utlis

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.apesmedical.commonsdk.delegate.ConfigModule
import com.library.sdk.ext.tryArea


/**
 * Created by Beetle_Sxy on 2020/10/12.
 * 用于解析 AndroidManifest 中的 Meta 属性
 * 配合 [ConfigModule] 使用
 */
class ManifestParser(private val _context: Context) {
    val MODULE_VALUE = "ConfigModule"
    fun parse(): List<ConfigModule> {
        val modules: MutableList<ConfigModule> = mutableListOf()
        try {
            val appInfo = _context.packageManager.getApplicationInfo(_context.packageName, PackageManager.GET_META_DATA)
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (MODULE_VALUE == appInfo.metaData[key]) {
                        modules.add(parseModule(key))
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Unable to find metadata to parse ConfigModule", e)
        }
        return modules
    }

    private fun parseModule(className: String): ConfigModule {
        val clazz: Class<*> = try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Unable to find ConfigModule implementation", e)
        }
        val module: Any
        module = try {
            clazz.newInstance()
        } catch (e: InstantiationException) {
            throw RuntimeException("Unable to instantiate ConfigModule implementation for $clazz", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Unable to instantiate ConfigModule implementation for $clazz", e)
        }
        if (module !is ConfigModule) {
            throw RuntimeException("Expected instanceof ConfigModule, but found: $module")
        }
        return module
    }
}