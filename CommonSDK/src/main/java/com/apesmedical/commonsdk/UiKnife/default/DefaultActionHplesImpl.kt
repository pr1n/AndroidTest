package com.apesmedical.commonsdk.UiKnife

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import com.apesmedical.commonsdk.R
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by Beetle_Sxy on 2019-06-22.
 * 默认标题栏帮助
 */
class DefaultActionHplesImpl : ActionHplesInte {
	
	private var mBackView: ImageView? = null
	private var mTitleView: TextView? = null
	private var mTitleText: CharSequence = ""
	private var mOtherView: _FrameLayout? = null
	private var onClickListener: suspend CoroutineScope.(View?) -> Unit = {}
	
	override fun bind(viewGroup: ViewGroup): ViewGroup {
		val mViewGroup = viewGroup.context.frameLayout {
			id = R.id.id_action_container
			lparams(matchParent, dip(50))
			
			mBackView = imageView {
				padding = dip(12)
				//imageResource = R.drawable.svg_arrow_left_gray
				imageResource = R.drawable.svg_back
			}.lparams(dip(50), dip(50)) {
				gravity = Gravity.START
			}
			mBackView?.onClick { onClickListener(it) }
			
			mTitleView = textView(mTitleText) {
				textSize = 20f
				textColor = "#333333".toColorInt()
				gravity = Gravity.CENTER
			}.lparams { gravity = Gravity.CENTER }
			
			frameLayout { mOtherView = this }.lparams(dip(50), dip(50)) {
				gravity = Gravity.END
			}
			
			//
			backgroundColorResource = R.color.colorActionBar
		}
		viewGroup.addView(mViewGroup, 0)
		return mViewGroup
	}
	
	override suspend fun setBackClick(onClickListener: suspend CoroutineScope.(View?) -> Unit) = this.apply {
		this.onClickListener = onClickListener
		mBackView?.onClick { onClickListener(it) }
	}
	
	fun setOtherClick(onClickListener: (View?) -> Unit) = this.apply { mOtherView?.onClick { onClickListener(it) } }
	
	fun getOtherView() = mOtherView
	fun getBackView() = mBackView
	
	fun setIsBack(vis: Boolean): DefaultActionHplesImpl {
		mBackView?.isVisible = vis
		return this
	}
	
	fun setIsTitle(vis: Boolean): DefaultActionHplesImpl {
		mTitleView?.isVisible = vis
		return this
	}
	
	fun setIsOther(vis: Boolean): DefaultActionHplesImpl {
		mOtherView?.isVisible = vis
		return this
	}
	
	/*fun setBlackgroudColor(@ColorInt colorInt: Int?): DefaultActionHplesImpl {
		if (colorInt != null) mBlackgroudColor = colorInt
		return this
	}*/
	
	fun setTitleText(text: CharSequence?): DefaultActionHplesImpl {
		mTitleText = text ?: ""
		mTitleView?.text = text
		return this
	}
	
	fun ankoOtherView(otherLayout: (@AnkoViewDslMarker _FrameLayout).() -> Unit) {
		mOtherView?.otherLayout()
	}
	
}