package com.apesmedical.commonsdk.UiKnife

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import com.library.sdk.ext.setLayout
import org.jetbrains.anko.backgroundColor

/**
 * Created by Beetle_Sxy on 2019-06-22.
 * 基本的底部导航栏帮助类
 */
class DefaultNavigationHplesImpl(private val layoutRes: Int = -1) : NavigationHplesInte {

    private var mBackgroundColor = Color.TRANSPARENT
    private var layoutView: View? = null

    constructor() : this(-1)

    override fun bind(viewGroup: ViewGroup): ViewGroup {
        /* viewGroup.setLayout(layoutRes).run {
             backgroundColor = mBackgroundColor
         }*/
        when {
            layoutRes != -1 -> {
                viewGroup.setLayout(layoutRes).run {
                    backgroundColor = mBackgroundColor
                    layoutView = this
                    viewGroup.addView(this)
                    return viewGroup
                }
            }
            layoutView != null -> {
                layoutView!!.backgroundColor = mBackgroundColor
                viewGroup.addView(layoutView)
                return viewGroup
            }
            else->return viewGroup
        }
    }

    override fun getView() = layoutView

    fun setLayoutView(view: View): DefaultNavigationHplesImpl {
        layoutView = view
        return this
    }

    fun setBackgroundColor(@ColorInt color: Int): DefaultNavigationHplesImpl {
        mBackgroundColor = color
        return this
    }
}