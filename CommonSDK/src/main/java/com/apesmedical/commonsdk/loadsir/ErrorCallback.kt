package com.apesmedical.commonsdk.loadsir

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.apesmedical.commonsdk.R
import com.kingja.loadsir.callback.Callback


class ErrorCallback : Callback() {
    override fun onCreateView() = R.layout.loadsir_error
    override fun onAttach(context: Context, view: View) {
        super.onAttach(context, view)
        val spannableString = SpannableString("读取数据失败\n点击 重新加载 试试~")
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#1E69FF")),
            10,
            14,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            AbsoluteSizeSpan(14, true),
            10,
            14,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            10,
            14,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        view.findViewById<TextView>(R.id.errorText).text = spannableString
    }
}