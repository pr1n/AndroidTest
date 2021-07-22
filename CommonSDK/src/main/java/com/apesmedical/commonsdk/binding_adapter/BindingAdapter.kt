package com.apesmedical.commonsdk.binding_adapter

import android.view.View
import androidx.annotation.DimenRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lihang.ShadowLayout

/**
 * Created by Beetle_Sxy on 2020/10/10.
 */

@BindingAdapter("bind-isSelected")
fun setSelected(view: View, isSelected: Boolean) {
	if (view.isSelected != isSelected) {
		view.isSelected = isSelected
	}
}

@BindingAdapter("bind-adapter")
fun setLinearLayoutManager(view: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
	if (adapter != null) view.adapter = adapter
}

@BindingAdapter("leftTop", "rightTop", "leftBottom", "rightBottom")
fun setShadowLayoutRadius(view: ShadowLayout, leftTop: Int = 0, rightTop: Int = 0, @DimenRes leftBottom: Int = 0, rightBottom: Int = 0) {
	view.setSpecialCorner(leftTop, rightTop, leftBottom, rightBottom)
}

@BindingAdapter("constraintHorizontal_bias")
fun setConstraintHorizontalBias(view: View, bias: Float) {
	val layoutParams = view.layoutParams
	if (layoutParams is ConstraintLayout.LayoutParams) {
		view.layoutParams = layoutParams.also {
			it.horizontalBias = bias
		}
	}
}

@BindingAdapter("width", "height", requireAll = true)
fun setSize(view: View, width: Float, height: Float) {
	view.layoutParams = view.layoutParams.apply {
		this.height = height.toInt()
		this.width = width.toInt()
	}
}