package com.library.sdk.ext

import android.view.View
import com.blankj.utilcode.util.ClickUtils

/**
 * Created by Beetle_Sxy on 2019-07-03.
 */
//
inline fun <T : View> T.singleClick(time: Long = 1000, crossinline block: (T) -> Unit) {
    ClickUtils.applySingleDebouncing(this, time) {
        block(this)
    }
}