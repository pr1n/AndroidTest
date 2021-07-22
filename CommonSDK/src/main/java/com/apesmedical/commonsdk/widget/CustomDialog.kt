package com.apesmedical.commonsdk.widget

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDialog
import com.blankj.utilcode.util.ScreenUtils


class CustomDialog(
    context: Context,
    private val contentView: View,
    private val gravity: Int = Gravity.CENTER,
    private val isCanceledOnTouchOutside: Boolean = true,
    private val width: Int = ScreenUtils.getScreenWidth() * 4 / 5, // 设置Dialog显示的宽度为屏幕宽度的5分之4
    @ColorInt private val backgroundColor: Int = Color.parseColor("#00FFFFFF"),
    @AnimRes private val animations: Int = 0
) : AppCompatDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialogWindow = window ?: return
        // 设置window的背景
        dialogWindow.decorView.setBackgroundColor(backgroundColor)
        // window显示Gravity
        dialogWindow.setGravity(gravity)
        if (Gravity.BOTTOM == gravity)
            dialogWindow.setBackgroundDrawableResource(0)
        // 加载需要显示的resId
        setContentView(contentView)

        val lp = dialogWindow.attributes
        lp.width =
            if (Gravity.BOTTOM == gravity) ScreenUtils.getScreenWidth() else width
        dialogWindow.attributes = lp
        //点击外部Dialog消失
        setCanceledOnTouchOutside(isCanceledOnTouchOutside)

        dialogWindow.setWindowAnimations(animations)
    }
}