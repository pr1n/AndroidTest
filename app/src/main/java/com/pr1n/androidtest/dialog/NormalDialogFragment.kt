package com.pr1n.androidtest.dialog

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.library.sdk.ext.logi
import com.pr1n.androidtest.databinding.DialogNormalBinding
import kotlinx.coroutines.flow.channelFlow
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume


suspend fun FragmentActivity.showNormalDialogFragment(): Boolean {
    return NormalDialogFragment().show(supportFragmentManager)
}

suspend fun Fragment.showNormalDialogFragment(): Boolean {
    return NormalDialogFragment().show(parentFragmentManager)
}

class NormalDialogFragment : CustomDialogFragment<DialogNormalBinding, Boolean>() {
    override fun viewBinding(inflater: LayoutInflater) = DialogNormalBinding.inflate(inflater)
    override fun invokeShow(continuation: Continuation<Boolean>) {
        viewBinding.confirm.setOnClickListener {
            //TODO 横竖屏切换后无法响应
            continuation.resume(false)
            dismissAllowingStateLoss()
        }
        channelFlow<String> {
            send("")
        }
        viewBinding.cancel.setOnClickListener {
            continuation.resume(true)
            dismissAllowingStateLoss()
        }
    }
}