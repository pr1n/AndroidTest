package com.library.sdk.ext

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RectShape
import android.util.LruCache
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorInt
import com.android.sdklibrary.ext.constrain
import com.blankj.utilcode.util.ConvertUtils

/**
 * Created by Beetle_Sxy on 2018/11/13.
 * View shape 扩展
 */

/**
 * 为 View 设置带边的 Background
 * @param strokeSize 边框粗细
 * @param strokeColor 边框颜色
 * @param strokeRadius 边框圆角
 * @param strokeDashWidth 虚线的长度
 * @param strokeDashGap 虚线的间隙
 * @param backgroundColor 背景颜色
 */
fun <T : View> T.shape(
	@ColorInt backgroundColor: Int = Color.TRANSPARENT,
	strokeRadius: Float = ConvertUtils.dp2px(4f).toFloat(),
	strokeSize: Int = 0,
	@ColorInt strokeColor: Int = Color.GRAY,
	strokeDashWidth: Float = 0f,
	strokeDashGap: Float = 0f,
	radius: RadiusDrawable? = null,
	customize: (GradientDrawable) -> Unit = {}
): T {
	background = shapeDrawable(strokeSize, strokeColor, strokeRadius, strokeDashWidth, strokeDashGap, backgroundColor, radius, customize)
	return this
}

/**
 * 生成 shape Drawable
 * @param strokeSize 边框粗细
 * @param strokeColor 边框颜色
 * @param strokeRadius 边框圆角
 * @param strokeDashWidth 虚线的长度
 * @param strokeDashGap 虚线的间隙
 * @param backgroundColor 背景颜色
 */
fun shapeDrawable(
	strokeSize: Int = 1,
	@ColorInt strokeColor: Int = Color.GRAY,
	strokeRadius: Float = ConvertUtils.dp2px(4f).toFloat(),
	strokeDashWidth: Float = 0f,
	strokeDashGap: Float = 0f,
	backgroundColor: Int = 0,
	radius: RadiusDrawable? = null,
	customize: (GradientDrawable) -> Unit = {}
): Drawable {
	val shape = GradientDrawable()
	shape.setStroke(strokeSize, strokeColor, strokeDashWidth, strokeDashGap)
	if (radius == null) shape.cornerRadius = strokeRadius
	else shape.cornerRadii = floatArrayOf(
		radius.LT, radius.LT,
		radius.RT, radius.RT,
		radius.RB, radius.RB,
		radius.LB, radius.LB,
	)
	shape.setColor(backgroundColor)
	customize(shape)
	return shape
}

data class RadiusDrawable(
	val LT: Float = 0f,
	val RT: Float = 0f,
	val RB: Float = 0f,
	val LB: Float = 0f,
)


/**
 * @param state_default 默认图片
 * @param state_pressed 是否触摸
 * @param state_focused 是否获取到焦点
 * @param state_hovered 光标是否经过
 * @param state_selected 是否选中
 * @param state_checkable 是否可勾选
 * @param state_checked 是否勾选
 * @param state_enabled 是否可用
 * @param state_activated 是否激活
 * @param state_window_focused 所在窗口是否获取焦点
 */
fun <T : View> T.selector(
	state_pressed: Drawable? = null,
	state_focused: Drawable? = null,
	state_hovered: Drawable? = null,
	state_selected: Drawable? = null,
	state_checkable: Drawable? = null,
	state_checked: Drawable? = null,
	state_enabled: Drawable? = null,
	state_activated: Drawable? = null,
	state_window_focused: Drawable? = null
): T {
	background = selectorDrawable(
		background,
		state_pressed,
		state_focused,
		state_hovered,
		state_selected,
		state_checkable,
		state_checked,
		state_enabled,
		state_activated,
		state_window_focused
	)
	return this
}


/**
 *
 * @param state_default 默认图片
 * @param state_pressed 是否触摸
 * @param state_focused 是否获取到焦点
 * @param state_hovered 光标是否经过
 * @param state_selected 是否选中
 * @param state_checkable 是否可勾选
 * @param state_checked 是否勾选
 * @param state_enabled 是否可用
 * @param state_activated 是否激活
 * @param state_window_focused 所在窗口是否获取焦点
 */
fun selectorDrawable(
	state_default: Drawable,
	state_pressed: Drawable? = null,
	state_focused: Drawable? = null,
	state_hovered: Drawable? = null,
	state_selected: Drawable? = null,
	state_checkable: Drawable? = null,
	state_checked: Drawable? = null,
	state_enabled: Drawable? = null,
	state_activated: Drawable? = null,
	state_window_focused: Drawable? = null
): Drawable {
	val selector = StateListDrawable()
	if (state_pressed != null) selector.addState(intArrayOf(android.R.attr.state_pressed), state_pressed)//是否触摸
	if (state_focused != null) selector.addState(intArrayOf(android.R.attr.state_focused), state_focused)//是否获取到焦点
	if (state_hovered != null) selector.addState(intArrayOf(android.R.attr.state_hovered), state_hovered)//光标是否经过
	if (state_selected != null) selector.addState(intArrayOf(android.R.attr.state_selected), state_selected)//是否选中
	if (state_checkable != null) selector.addState(intArrayOf(android.R.attr.state_checkable), state_checkable)//是否可勾选
	if (state_checked != null) selector.addState(intArrayOf(android.R.attr.state_checked), state_checked)//是否勾选
	if (state_enabled != null) selector.addState(intArrayOf(android.R.attr.state_enabled), state_enabled)//是否可用
	if (state_activated != null) selector.addState(intArrayOf(android.R.attr.state_activated), state_activated)//是否激活
	if (state_window_focused != null) selector.addState(
		intArrayOf(android.R.attr.state_window_focused), state_window_focused
	)//所在窗口是否获取焦点
	selector.addState(intArrayOf(), state_default)
	return selector
}

/**
 * 渐变背景
 */
@SuppressLint("RtlHardcoded")
fun makeCubicGradientScrimDrawable(
	gravity: Int,
	alpha: Int = 0xFF,
	red: Int = 0x0,
	green: Int = 0x0,
	blue: Int = 0x0,
	requestedStops: Int = 8
): Drawable {
	val cubicGradientScrimCache = LruCache<Int, Drawable>(10)
	var numStops = requestedStops
	
	// Generate a cache key by hashing together the inputs, based on the method described in the Effective Java book
	var cacheKeyHash = Color.argb(alpha, red, green, blue)
	cacheKeyHash = 31 * cacheKeyHash + numStops
	cacheKeyHash = 31 * cacheKeyHash + gravity
	
	val cachedGradient = cubicGradientScrimCache.get(cacheKeyHash)
	if (cachedGradient != null) {
		return cachedGradient
	}
	
	numStops = Math.max(numStops, 2)
	
	val paintDrawable = PaintDrawable().apply {
		shape = RectShape()
	}
	
	val stopColors = IntArray(numStops)
	
	for (i in 0 until numStops) {
		val x = i * 1f / (numStops - 1)
		val opacity = Math.pow(x.toDouble(), 3.0).toFloat().constrain(0f, 1f)
		stopColors[i] = Color.argb((alpha * opacity).toInt(), red, green, blue)
	}
	
	val x0: Float
	val x1: Float
	val y0: Float
	val y1: Float
	when (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
		Gravity.LEFT -> {
			x0 = 1f
			x1 = 0f
		}
		Gravity.RIGHT -> {
			x0 = 0f
			x1 = 1f
		}
		else -> {
			x0 = 0f
			x1 = 0f
		}
	}
	when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
		Gravity.TOP -> {
			y0 = 1f
			y1 = 0f
		}
		Gravity.BOTTOM -> {
			y0 = 0f
			y1 = 1f
		}
		else -> {
			y0 = 0f
			y1 = 0f
		}
	}
	
	paintDrawable.shaderFactory = object : ShapeDrawable.ShaderFactory() {
		override fun resize(width: Int, height: Int): Shader {
			return LinearGradient(
				width * x0,
				height * y0,
				width * x1,
				height * y1,
				stopColors, null,
				Shader.TileMode.CLAMP
			)
		}
	}
	
	cubicGradientScrimCache.put(cacheKeyHash, paintDrawable)
	return paintDrawable
}

