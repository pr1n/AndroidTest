package com.pr1n.androidtest.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

private val IMAGE_SIZE = 200.dp.toInt()

class CoordinateMultiTouchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, IMAGE_SIZE)

    private var currentX = 0F
    private var currentY = 0F
    private var startX = 0F
    private var startY = 0F
    private var offsetX = 0F
    private var offsetY = 0F

    private var isPointerUp = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(bitmap, currentX, currentY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val focusX: Float
        val focusY: Float
        var pointerCount = event.pointerCount
        var sumX = 0F
        var sumY = 0F
        isPointerUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP
        for (i in 0 until pointerCount) {
            if (!(isPointerUp && i == event.actionIndex)) {
                sumX += event.getX(i)
                sumY += event.getY(i)
            }
        }
        if (isPointerUp) pointerCount--
        focusX = sumX / pointerCount
        focusY = sumY / pointerCount
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                startX = focusX
                startY = focusY
                offsetX = startX - currentX
                offsetY = startY - currentY
                !(currentX > startX || currentY > startY || (currentX + IMAGE_SIZE) < startX || (currentY + IMAGE_SIZE) < startY)
            }
            MotionEvent.ACTION_MOVE -> {
                currentX = (focusX - offsetX).coerceAtLeast(0F)
                    .coerceAtMost(width - IMAGE_SIZE.toFloat())
                currentY = (focusY - offsetY).coerceAtLeast(0F)
                    .coerceAtMost(height - IMAGE_SIZE.toFloat())
                invalidate()
                false
            }
            else -> false
        }
    }

}