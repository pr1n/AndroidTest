package com.pr1n.androidtest.custom

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

private val TEXT_SIZE = 12.dp
private val TEXT_MARGIN = 8.dp
private val HORIZONTAL_OFFSET = 5.dp
private val VERTICAL_OFFSET = 18.dp
private val EXTRA_VERTICAL_OFFSET = 16.dp

class MaterialEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var floatingLabelShow = false
    private var floatingLebelFraction = 0F
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.RED
        it.textSize = TEXT_SIZE
    }

    private val animator by lazy { ObjectAnimator.ofFloat(this, "floatingLebelFraction", 0F, 1F) }

    init {
        setPadding(
            paddingLeft,
            paddingTop + TEXT_SIZE.toInt() + TEXT_MARGIN.toInt(),
            paddingRight,
            paddingBottom
        )
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (floatingLabelShow && text.isNullOrEmpty()) {
            floatingLabelShow = false
            animator.reverse()
        } else if (!floatingLabelShow && !text.isNullOrEmpty()) {
            floatingLabelShow = true
            animator.start()
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.alpha = (floatingLebelFraction * 0xFF).toInt()
        val currentVerticalValue =
            VERTICAL_OFFSET + EXTRA_VERTICAL_OFFSET * (1 - floatingLebelFraction)
        canvas.drawText(hint.toString(), HORIZONTAL_OFFSET, currentVerticalValue, paint)
    }

}