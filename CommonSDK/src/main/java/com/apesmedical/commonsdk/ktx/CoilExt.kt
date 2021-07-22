package com.apesmedical.commonsdk.ktx

import android.content.Context
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

/**
 * @author Beetle_sxy
 * @ClassName: CoilExt
 * @Description:
 * @date: 2021/5/12
 */

/**
 * Get the singleton [ImageLoader]. This is an alias for [Coil.imageLoader].
 */
inline val Context.gifImageLoader: ImageLoader
    @JvmName("imageLoader") get() = CoilCustomized.gifImageLoader(this)

object CoilCustomized {
    private var mGifImageLoader: ImageLoader? = null
    fun gifImageLoader(context: Context) = mGifImageLoader ?: newGifImageLoader(context)
    private fun newGifImageLoader(context: Context): ImageLoader {
        mGifImageLoader?.let { return it }
        val newImageLoader = ImageLoader.Builder(context)
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) add(ImageDecoderDecoder(context))
                else add(GifDecoder())
            }
            .build()
        mGifImageLoader = newImageLoader
        return newImageLoader
    }
}