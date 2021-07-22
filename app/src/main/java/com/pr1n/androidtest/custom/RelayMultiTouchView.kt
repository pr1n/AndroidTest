package com.pr1n.androidtest.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

private val IMAGE_SIZE = 200.dp.toInt()

class RelayMultiTouchView @JvmOverloads constructor(
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

    private var trackingPointerId = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(bitmap, currentX, currentY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                trackingPointerId = event.getPointerId(0)
                startX = event.x
                startY = event.y
                offsetX = startX - currentX
                offsetY = startY - currentY
                !(currentX > startX || currentY > startY || (currentX + IMAGE_SIZE) < startX || (currentY + IMAGE_SIZE) < startY)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex
                if (!(currentX > event.getX(actionIndex) || currentY > event.getY(actionIndex) || (currentX + IMAGE_SIZE) < event.getX(
                        actionIndex
                    ) || (currentY + IMAGE_SIZE) < event.getY(actionIndex))
                ) {
                    trackingPointerId = event.getPointerId(actionIndex)
                    startX = event.getX(actionIndex)
                    startY = event.getY(actionIndex)
                    offsetX = startX - currentX
                    offsetY = startY - currentY
                }
                true
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                if (trackingPointerId == pointerId) {
                    val newActionIndex =
                        if (actionIndex == event.pointerCount - 1) event.pointerCount - 2
                        else event.pointerCount - 1
                    trackingPointerId = event.getPointerId(newActionIndex)
                    startX = event.getX(newActionIndex)
                    startY = event.getY(newActionIndex)
                    offsetX = startX - currentX
                    offsetY = startY - currentY
                }
                false
            }
            MotionEvent.ACTION_MOVE -> {
                val actionIndex = event.findPointerIndex(trackingPointerId)
                currentX = (event.getX(actionIndex) - offsetX).coerceAtLeast(0F)
                    .coerceAtMost(width - IMAGE_SIZE.toFloat())
                currentY = (event.getY(actionIndex) - offsetY).coerceAtLeast(0F)
                    .coerceAtMost(height - IMAGE_SIZE.toFloat())
                invalidate()
                false
            }
            else -> false
        }
    }

}