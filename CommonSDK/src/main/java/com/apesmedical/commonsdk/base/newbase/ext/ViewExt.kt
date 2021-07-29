package com.apesmedical.commonsdk.base.newbase.ext

import android.view.View

fun View.visibility(isVisibility: Boolean) {
    visibility = if (isVisibility) View.VISIBLE else View.GONE
}

fun View.gone() {
    if (this.isShow()) visibility(false)
}

fun View.invisible() {
    if (this.isShow()) this.visibility = View.INVISIBLE
}

fun View.show() {
    if (!this.isShow()) visibility(true)
}

fun View.isShow() = visibility == View.VISIBLE