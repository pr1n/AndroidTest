package com.pr1n.androidtest.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.apesmedical.commonsdk.widget.CustomDialog
import com.blankj.utilcode.util.ScreenUtils
import com.pr1n.androidtest.databinding.DialogNormalBinding
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.anko.sdk27.coroutines.onClick
import kotlin.coroutines.resume

suspend fun Activity.showNormalDialog() = NormalDialog(this, layoutInflater).show()
suspend fun Fragment.showNormalDialog() = NormalDialog(requireContext(), layoutInflater).show()

private class NormalDialog(
    private val context: Context,
    private val layoutInflater: LayoutInflater
) {

    private val contentBinding by lazy { DialogNormalBinding.inflate(layoutInflater) }

    private val dialog by lazy {
        CustomDialog(
            context = context,
            contentView = contentBinding.root,
            gravity = Gravity.CENTER,
            isCanceledOnTouchOutside = false,
            width = ScreenUtils.getScreenWidth(),
            backgroundColor = Color.parseColor("#00FFFFFF"),
            animations = 0
        )
    }

    suspend fun show() = suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
        cancellableContinuation.invokeOnCancellation {
            dialog.cancel()
        }
        contentBinding.cancel.setOnClickListener {
            cancellableContinuation.resume(false)
            dialog.dismiss()
        }
        contentBinding.confirm.setOnClickListener {
            cancellableContinuation.resume(true)
            dialog.dismiss()
        }
        dialog.show()
    }

}