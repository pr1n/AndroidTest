package com.apesmedical.commonsdk.binding_adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

/**
 * Created by Beetle_Sxy on 2020/10/14.
 */
@BindingAdapter("img-url", "img-circle", "img-error", "img-placeholder", "img-isAllPlaceholder", requireAll = false)
fun setLoad(
	view: ImageView,
	url: Any?,
	isCircle: Boolean = false,
	error: Drawable? = null,
	placeholder: Drawable? = null,
	isAllPlaceholder: Drawable? = null,
) {
	/*view.loadAny(url) {
		//crossfade(iscrossfade)
		if (isAllPlaceholder != null) {
			error(isAllPlaceholder)
			placeholder(isAllPlaceholder)
		}
		
		if (error != null) error(error)
		if (placeholder != null) placeholder(placeholder)
		if (isCircle) transformations(CircleCropTransformation())
	}*/
	
	Glide.with(view)
		.load(url)
		.apply {
//			centerCrop()
			error(error ?: isAllPlaceholder)
			placeholder(placeholder ?: isAllPlaceholder)
			if (isCircle) transform(CircleCrop())
		}
		.into(view)
}