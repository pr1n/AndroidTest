package com.pr1n.androidtest.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.library.sdk.ext.tryArea
import com.pr1n.androidtest.R
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation

abstract class CustomDialogFragment<VB : ViewBinding, T>(
    private var isBottom: Boolean = false,
    private var isCanceledOnTouchOutside: Boolean = false,
    private var backgroundColor: Int = Color.RED,
    private var width: Int = ScreenUtils.getScreenWidth() / 5 * 4,
    private var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
) : AppCompatDialogFragment() {
    protected lateinit var viewBinding: VB

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        if (isBottom) BottomSheetDialog(requireContext())
        else super.onCreateDialog(savedInstanceState)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = viewBinding(inflater).also { viewBinding = it }.root

    abstract fun viewBinding(inflater: LayoutInflater): VB

    override fun onStart() {
        super.onStart()
        init()
    }

    private fun init() {
        requireDialog().setCancelable(isCanceledOnTouchOutside)
        requireDialog().setCanceledOnTouchOutside(isCanceledOnTouchOutside)
        val window = requireDialog().window ?: return
        if (isBottom) tryArea {
            window.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(backgroundColor)
        }
        else
            window.apply {
                setBackgroundDrawable(ColorDrawable(backgroundColor))
                attributes = window.attributes.also {
                    it.width = width
                    it.height = height
                }
            }
    }

    suspend fun show(fragmentManager: FragmentManager) =
        suspendCancellableCoroutine<T> { cancellableContinuation ->
            showNow(fragmentManager, "dialog")
            cancellableContinuation.invokeOnCancellation { onDestroy() }
            invokeShow(cancellableContinuation)
        }

    abstract fun invokeShow(continuation: Continuation<T>)
}