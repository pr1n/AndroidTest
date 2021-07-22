package com.library.sdk.ext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Created by Beetle_Sxy on 2018/12/27.
 */
fun Context.getLayoutView(
    @LayoutRes layoutRes: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = false,
    onLyout: (View) -> Unit
): View {
    val layout = LayoutInflater.from(this).inflate(layoutRes, root, attachToRoot)
    onLyout(layout)
    return layout
}

fun Context.getLayoutView(
    @LayoutRes layoutRes: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = false
) = LayoutInflater.from(this).inflate(layoutRes, root, attachToRoot)

fun Fragment.getLayoutView(
    @LayoutRes layoutRes: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = false
) = LayoutInflater.from(this.context).inflate(layoutRes, root, attachToRoot)

fun Fragment.getLayoutView(
    @LayoutRes layoutRes: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = false,
    onLyout: (View) -> Unit
): View {
    val layout = LayoutInflater.from(this.context).inflate(layoutRes, root, attachToRoot)
    onLyout(layout)
    return layout
}

fun ViewGroup.setLayout(@LayoutRes layout: Int,attachRoot:Boolean = false) = this.context.getLayoutView(layout, this, attachRoot)

