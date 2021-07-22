package com.pr1n.androidtest.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathEffect
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

const val OPEN_ANGLE = 120
val RADIUS = 150.dp
val LENGTH = 120.dp

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var pathEffect: PathEffect
    private val path = Path()
    private val dashPath = Path().also {
        it.addRect(0F, 0F, 2.dp, 10.dp, Path.Direction.CW)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.BLACK
        it.strokeWidth = 3.dp
        it.style = Paint.Style.STROKE
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.RED
        it.strokeWidth = 3.dp
        it.strokeCap = Paint.Cap.ROUND
        it.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        path.reset()
        path.addArc(
            width / 2F - RADIUS,
            height / 2F - RADIUS,
            width / 2 + RADIUS,
            height / 2F + RADIUS,
            90F + OPEN_ANGLE / 2,
            360F - OPEN_ANGLE,
        )
        val pathMeasure = PathMeasure(path, false)
        pathEffect = PathDashPathEffect(
            dashPath,
            (pathMeasure.length - 2.dp) / 20,
            0F,
            PathDashPathEffect.Style.ROTATE
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)

        paint.pathEffect = pathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null

        canvas.drawLine(
            width / 2F,
            height / 2F,
            width / 2F + LENGTH * cos(markToRadius(10)).toFloat(),
            height / 2F + LENGTH * sin(markToRadius(10)).toFloat(),
            linePaint
        )
    }

    fun markToRadius(i: Int) =
        Math.toRadians(((90F + OPEN_ANGLE / 2) + (360F - OPEN_ANGLE) / 20 * i).toDouble())


}