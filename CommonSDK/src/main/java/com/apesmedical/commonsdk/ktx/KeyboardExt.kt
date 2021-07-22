package com.apesmedical.commonsdk.ktx

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @Description: 键盘
 * @Author: Liuyang
 * @Create: 2021-04-27
 */
object KeyboardExt {
    /**
     * 处理点击非 EditText 区域时，自动关闭键盘
     *
     * @param isAutoCloseKeyboard 是否自动关闭键盘
     * @param currentFocusView    当前获取焦点的控件
     * @param motionEvent         触摸事件
     * @param dialogOrActivity    Dialog 或 Activity
     */
    fun handleAutoCloseKeyboard(
        isAutoCloseKeyboard: Boolean,
        currentFocusView: View?,
        motionEvent: MotionEvent,
        dialogOrActivity: Any?
    ) {
        if (isAutoCloseKeyboard && motionEvent.action == MotionEvent.ACTION_DOWN && currentFocusView != null && currentFocusView is EditText && dialogOrActivity != null) {
            val leftTop = intArrayOf(0, 0)
            currentFocusView.clearFocus()
            currentFocusView.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + currentFocusView.getHeight()
            val right = left + currentFocusView.getWidth()
            if (!(motionEvent.x > left && motionEvent.x < right && motionEvent.y > top && motionEvent.y < bottom)) {
                if (dialogOrActivity is Dialog) {
                    closeKeyboard(dialogOrActivity)
                } else if (dialogOrActivity is Activity) {
                    closeKeyboard(dialogOrActivity)
                }
            }
        }
    }

    /**
     * 关闭dialog中打开的键盘
     *
     * @param dialog
     */
    private fun closeKeyboard(dialog: Dialog) {
        val view = dialog.window!!.peekDecorView()
        closeKeyboard(view)
    }

    /**
     * 关闭activity中打开的键盘
     *
     * @param activity
     */
    private fun closeKeyboard(activity: Activity) {
        val view = activity.window.peekDecorView()
        closeKeyboard(view)
    }

    /**
     * 关闭软键盘
     *
     * @param view
     */
    private fun closeKeyboard(view: View?) {
        if (view != null) {
            val inputMethodManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}