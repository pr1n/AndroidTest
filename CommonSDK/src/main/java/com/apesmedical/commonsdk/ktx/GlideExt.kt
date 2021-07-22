package com.library.sdk.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Beetle_Sxy on 2019/4/28.
 * glide 展示图片
 */
fun <T:ImageView> T.glide(load: Any?): T {
    Glide.with(this)
        .asBitmap()
        .load(load)
        .apply(
            RequestOptions()
                .centerCrop()
                .error(android.R.color.holo_red_dark)
                .placeholder(android.R.color.darker_gray)
                .fallback(android.R.color.black)
        )
        .into(this)
    return this
}

