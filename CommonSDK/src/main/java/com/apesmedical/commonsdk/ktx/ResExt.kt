package com.library.sdk.ext

import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.Utils

/**
 * Created by Beetle_Sxy on 2019/2/15.
 */

/**
 * 根据 id 获取颜色
 */
fun @receiver:ColorRes Int.toColorInt(): Int {
    return try {
        //Utils.getApp().resources.getColor(this)
        ContextCompat.getColor(Utils.getApp(), this)
    } catch (e: Exception) {
        e.printStackTrace()
        Color.WHITE
    }
}

/**
 * 根据 id 获取图片
 */
fun @receiver:DrawableRes Int.toDrawable() = try {
    //Utils.getApp().resources.getDrawable(this)
    val drawable = ContextCompat.getDrawable(Utils.getApp(), this)
    drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    drawable
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * 根据 id 获取字符串
 */
fun @receiver:StringRes Int.toResString() = try {
    Utils.getApp().resources.getString(this)
} catch (e: Exception) {
    e.printStackTrace()
    ""
}

/**
 * 根据 id 获取Dimension
 */
fun @receiver:DimenRes Int.toResDimension() = try {
    Utils.getApp().resources.getDimension(this)
} catch (e: Exception) {
    0f
}