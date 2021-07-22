package com.pr1n.androidtest.custom

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.pr1n.androidtest.R


class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object{
        private const val DEFAULT_DOT_COUNT = 3
        private val DEFAULT_DOT_RADIUS = 8.dp / 2
        private val DEFAULT_DOT_PADDING = 7.dp
        private const val DEFAULT_ANI_DURATION = 1200L
        private val DEFAULT_ANI_OFFSET = DEFAULT_DOT_RADIUS * 2 * 1.8F
        private val DEFAULT_DOT_COLOR = Color.parseColor("#3E67F9")
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.FILL
    }

    var aniValue0 = 0F
    var aniValue1 = 0F
    var aniValue2 = 0F
    var aniValue3 = 0F
    var aniValue4 = 0F
    private val aniValues = arrayOf(::aniValue0, ::aniValue1, ::aniValue2, ::aniValue3, ::aniValue4)

    private var centerX = 0
    private var centerY = 0

    private var dotCount = DEFAULT_DOT_COUNT
    private var dotRadius = DEFAULT_DOT_RADIUS
    private var dotPadding = DEFAULT_DOT_PADDING
    private var aniDuration = DEFAULT_ANI_DURATION
    private var maxAniOffset = DEFAULT_ANI_OFFSET
    private var dotColor = DEFAULT_DOT_COLOR

    private var maxWidth = 0F

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView)
        dotCount = typedArray.getInt(R.styleable.LoadingView_dotCount, DEFAULT_DOT_COUNT)
        dotRadius = typedArray.getDimension(R.styleable.LoadingView_dotRadius, DEFAULT_DOT_RADIUS)
        dotPadding = typedArray.getDimension(R.styleable.LoadingView_dotPadding, DEFAULT_DOT_PADDING)
        aniDuration = typedArray.getInt(R.styleable.LoadingView_aniDuration, DEFAULT_ANI_DURATION.toInt()).toLong()
        maxAniOffset = typedArray.getDimension(R.styleable.LoadingView_maxAniOffset, DEFAULT_ANI_OFFSET)
        dotColor = typedArray.getColor(R.styleable.LoadingView_dotColor, DEFAULT_DOT_COLOR)
        typedArray.recycle()

        dotPaint.color = dotColor
        maxWidth = dotCount * (dotRadius * 2) + dotPadding * (dotCount - 1)
        (0 until dotCount).forEach { index ->
            ObjectAnimator.ofFloat(this, "aniValue$index", 0F, 1F, 0F).also {
                it.duration = aniDuration
                it.interpolator = DecelerateInterpolator()
                it.repeatCount = ObjectAnimator.INFINITE
                it.repeatMode = ObjectAnimator.RESTART
                it.startDelay = (index + 1) * (aniDuration / 2.5 / dotCount).toLong()
                it.start()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        (0 until dotCount).forEach {
            val circleX =
                centerX - (maxWidth / 2) + dotRadius + (dotPadding * it + dotRadius * 2 * it)
            val circleY = (centerY - dotRadius) + maxAniOffset * aniValues[it]()
            canvas.drawCircle(circleX, circleY, dotRadius, dotPaint)
        }
        postInvalidateDelayed(45)
    }

}