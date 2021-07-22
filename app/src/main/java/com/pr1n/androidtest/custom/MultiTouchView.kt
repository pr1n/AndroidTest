package com.pr1n.androidtest.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View

class MultiTouchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.RED
        it.strokeWidth = 4.dp
        it.style = Paint.Style.STROKE
        it.strokeCap = Paint.Cap.ROUND
        it.strokeJoin = Paint.Join.ROUND
    }
    private val paths = SparseArray<Path>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in 0 until paths.size()) {
            canvas.drawPath(paths.valueAt(i), paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex
                paths.append(event.getPointerId(actionIndex), Path().also {
                    it.moveTo(event.getX(actionIndex), event.getY(actionIndex))
                })
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                paths.remove(event.getPointerId(event.actionIndex))
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until paths.size()){
                    paths[event.getPointerId(i)].lineTo(event.getX(i), event.getY(i))
                }
                invalidate()
            }
        }
        return true
    }

}