package com.apesmedical.commonsdk.binding_adapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.apesmedical.commonsdk.utlis.numEllipsis

/**
 * Created by zhaojin on 2020/12/25.
 */

@SuppressLint("SetTextI18n")
@BindingAdapter("number-format")
fun numberFormat(text: TextView, data: Int = 0) {
    text.setText((data.toString().numEllipsis()))
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["number-format", "number-unit"])
fun numberFormatUnit(text: TextView, data: Int = 0, unit: String = "") {
    text.setText((data.toString().numEllipsis()) + unit)
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["number-format", "number-left-text"])
fun numberFormatLeft(text: TextView, data: Int = 0, unit: String = "") {
    text.setText(unit + (data.toString().numEllipsis()))
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["number-format", "number-right-text"])
fun numberFormatRight(text: TextView, data: Int = 0, unit: String = "") {
    text.setText((data.toString().numEllipsis())+unit)
}