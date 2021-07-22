package com.apesmedical.commonsdk.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.apesmedical.commonsdk.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.library.sdk.ext.tryArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.wrapContent


/**
 * Created by Beetle_Sxy on 2018/11/30.
 * 简单封装 DialogFragment
 */
abstract class BaseDialogFragment : AppCompatDialogFragment {
	protected var mLayoutRes = -1
	protected var mLayoutAnko: (context: Context?, dialog: BaseDialogFragment) -> View? = { _, _ -> null }
	protected var isBottom: Boolean = false
	protected var mWidth = wrapContent
	protected var mHeight = wrapContent
	protected val _ViewCreatedListening = mutableListOf<(View) -> Unit>()
	
	var isTransparentBackground = true
	
	//是否强制展示（点击外部不可取消）
	var isMust = false
	
	constructor() : super()
	constructor(must: Boolean) : this() {
		isMust = must
	}
	
	constructor(layoutRes: Int) : this() {
		mLayoutRes = layoutRes
	}
	
	final override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		initData(savedInstanceState)
	}
	
	final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return if (context != null && isBottom) BottomSheetDialog(requireContext()) else super.onCreateDialog(savedInstanceState)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View? {
		if (onLayoutRes() != -1) mLayoutRes = onLayoutRes()
		return if (mLayoutRes == -1) onLayoutAnko(context) ?: mLayoutAnko(context, this)
		else inflater.inflate(mLayoutRes, container)
	}
	
	final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setMustDialog()
		initView(view, savedInstanceState)
		lifecycleScope.launch { withContext(Dispatchers.IO) { _ViewCreatedListening.forEach { it(view) } } }
	}
	
	private fun setMustDialog(must: Boolean = isMust) {
		if (must) dialog?.also {
			it.setCancelable(false)
			it.setCanceledOnTouchOutside(false)
			dialog?.setOnKeyListener { dialog, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK }
		}
	}
	
	
	override fun onStart() {
		super.onStart()
		if (isTransparentBackground) dialog?.window.let { win ->
			if (isBottom) tryArea { win?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundResource(android.R.color.transparent) }
			else {
				win?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
				dialog?.window.let { win ->
					win?.attributes = win?.attributes.also {
						if (mWidth != matchParent && mWidth != wrapContent) mWidth = dip(mWidth)
						if (mHeight != matchParent && mHeight != wrapContent) mHeight = dip(mHeight)
						it?.width = mWidth
						it?.height = mHeight
					}
				}
			}
		}

	}
	
	open protected fun initData(savedInstanceState: Bundle?) {}
	
	@LayoutRes
	protected open fun onLayoutRes(): Int = -1
	
	protected open fun onLayoutAnko(container: Context?): View? = null
	
	protected open fun initView(view: View, savedInstanceState: Bundle?) {}
	
	override fun onDestroyView() {
		super.onDestroyView()
	}

	open fun show(manager: FragmentManager) {
		if (isAdded.not()) {//判断是否已经在展示
//			super.show(manager, this::class.java.canonicalName)
			val ft: FragmentTransaction = manager.beginTransaction()
			ft.add(this, this::class.java.canonicalName)
			ft.commitAllowingStateLoss()
		}
	}
	
	/* ######################################################## 提供配置方法 ######################################################## */
	
	fun setIsBottom(isBottom: Boolean): BaseDialogFragment {
		this.isBottom = isBottom
		return this
	}
	
	fun showBottom(): BaseDialogFragment {
		this.isBottom = true
		return this
	}
	
	fun setLayoutRes(@LayoutRes res: Int): BaseDialogFragment {
		mLayoutRes = res
		return this
	}
	
	fun setLayoutAnko(mLayoutAnko: (context: Context?, dialog: BaseDialogFragment) -> View?): BaseDialogFragment {
		this.mLayoutAnko = mLayoutAnko
		return this
	}
	
	fun setWidth(width: Int): BaseDialogFragment {
		mWidth = width
		return this
	}
	
	fun setHeight(height: Int): BaseDialogFragment {
		mHeight = height
		return this
	}
	
	fun setSize(width: Int, height: Int): BaseDialogFragment {
		mWidth = width
		mHeight = height
		return this
	}
	
	fun addViewCreatedListening(listening: (View) -> Unit) {
		_ViewCreatedListening.add(listening)
	}
	
}