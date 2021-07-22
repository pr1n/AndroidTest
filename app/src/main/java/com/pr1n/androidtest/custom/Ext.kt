package com.pr1n.androidtest.custom

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import com.pr1n.androidtest.R

fun Float.toPx(unit: Int = TypedValue.COMPLEX_UNIT_DIP): Float {
    return TypedValue.applyDimension(
        unit,
        this,
        Resources.getSystem().displayMetrics
    )
}

fun Int.toPx(unit: Int = TypedValue.COMPLEX_UNIT_DIP): Float {
    return this.toFloat().toPx(unit)
}

val Float.dp get() = this.toPx()
val Int.dp get() = this.toPx()

fun getAvatar(res: Resources, width: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, R.mipmap.avart, options)
    options.inJustDecodeBounds = false
    options.inDensity = options.outWidth
    options.inTargetDensity = width
    return BitmapFactory.decodeResource(res, R.mipmap.avart, options)
}