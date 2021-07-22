package com.pr1n.androidtest.custom

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import java.lang.Float.min
import kotlin.math.max

private val IMAGE_SIZE = 300.dp.toInt()
private const val EXTRA_SCALE_FACOR = 1.5F

class ScalableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, IMAGE_SIZE)

    private var originalOffsetX = 0F
    private var originalOffsetY = 0F
    private var offsetX = 0F
    private var offsetY = 0F

    private val myGestureListener = MyGestureListener()
    private val myFlingRunner = MyFlingRunner()
    private val gestureDetector = GestureDetectorCompat(context, myGestureListener)
    private val myScaleGestureListener = MyScaleGestureListener()
    private val scaleGestureDetector = ScaleGestureDetector(context, myScaleGestureListener)

    private var smallScale = 0F
    private var bigScale = 0F

    private var isBig = false

    var currentScale = 0F
        set(value) {
            field = value
            invalidate()
        }

    private val anim =
        ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale)
            .also { it.interpolator = FastOutSlowInInterpolator() }

    private val scroller = OverScroller(context)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originalOffsetX = (w - IMAGE_SIZE) / 2F
        originalOffsetY = (h - IMAGE_SIZE) / 2F

        if (bitmap.width / bitmap.height.toFloat() > w / h.toFloat()) {
            smallScale = w / bitmap.width.toFloat()
            bigScale = h / bitmap.height.toFloat() * EXTRA_SCALE_FACOR
        } else {
            smallScale = h / bitmap.height.toFloat()
            bigScale = w / bitmap.width.toFloat() * EXTRA_SCALE_FACOR
        }
        anim.setFloatValues(smallScale, bigScale)
        currentScale = smallScale
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scaleFraction = (currentScale - smallScale) / (bigScale - smallScale)
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction)
        canvas.scale(currentScale, currentScale, width / 2F, height / 2F)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress)
            gestureDetector.onTouchEvent(event)
        return true
    }

    private fun fixOffset() {
        offsetX = min(offsetX, (bitmap.width * bigScale - width) / 2)
        offsetX = max(offsetX, -(bitmap.width * bigScale - width) / 2)
        offsetY = min(offsetY, (bitmap.height * bigScale - height) / 2)
        offsetY = max(offsetY, -(bitmap.height * bigScale - height) / 2)
    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent) = true

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (isBig) {
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffset()
                invalidate()
            }
            return false
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (isBig) {
                val minX = -(bitmap.width * bigScale - width) / 2
                val maxX = -minX
                val minY = -(bitmap.height * bigScale - height) / 2
                val maxY = -minY
                scroller.fling(
                    offsetX.toInt(),
                    offsetY.toInt(),
                    velocityX.toInt(),
                    velocityY.toInt(),
                    minX.toInt(),
                    maxX.toInt(),
                    minY.toInt(),
                    maxY.toInt(),
                    30.dp.toInt(),
                    30.dp.toInt(),
                )
                ViewCompat.postOnAnimation(this@ScalableImageView, myFlingRunner)
            }
            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            isBig = !isBig
            if (isBig) {
                offsetX = (e.x - width / 2F) * (1 - bigScale / smallScale)
                offsetY = (e.y - height / 2F) * (1 - bigScale / smallScale)
                fixOffset()
                anim.start()
            } else anim.reverse()
            return true
        }
    }

    inner class MyFlingRunner : Runnable {
        override fun run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.currX.toFloat()
                offsetY = scroller.currY.toFloat()
                invalidate()
                ViewCompat.postOnAnimation(this@ScalableImageView, this)
            }
        }
    }

    inner class MyScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val tempCurrentScale = currentScale * detector.scaleFactor
            return if (tempCurrentScale < smallScale || tempCurrentScale > bigScale) false
            else {
                currentScale *= detector.scaleFactor
                true
            }
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            offsetX = (detector.focusX - width / 2F) * (1 - bigScale / smallScale)
            offsetY = (detector.focusY - height / 2F) * (1 - bigScale / smallScale)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            //currentScale = currentScale.coerceAtLeast(smallScale).coerceAtMost(bigScale)
        }
    }

}