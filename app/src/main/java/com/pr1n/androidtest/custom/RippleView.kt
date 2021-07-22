package com.pr1n.androidtest.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.pr1n.androidtest.R

// 中心图片的默认大小
private val IMAGE_DEFAULT_SIZE = 80.dp

// 默认绘制刷新的延时
private const val DETAULT_DELAYMILLISECONDS = 300

private const val DEFAULT_CURRENT_COUNT = 1

/**
 * 涟漪效果的ImageView
 */
class RippleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    // 中心位置的画笔
    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 涟漪效果的画笔
    private val spreadPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 中心显示的图片Bitmap
    private var centerBitmap: Bitmap

    // 中心点X
    private var centerX = 0

    // 中心点Y
    private var centerY = 0

    // 中心图片的大小
    private var centerImageSize: Float

    // 控件的大小
    private var maxSize: Float

    // 涟漪的大小
    private var rippleSize: Float

    // 涟漪数量
    private var rippleCount: Int

    // 涟漪效果的延时
    private var delayMilliSeconds: Int

    // 中心图片的位置
    private val centerImageRect = Rect()

    // 裁剪中心图片
    private val centerImageClipPath = Path()

    // 涟漪颜色
    private var spreadColor: Int

    init {
        // 初始化
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleView)
        val centerImageRes = typedArray.getResourceId(R.styleable.RippleView_centerImageRes, 0)
        centerImageSize =
            typedArray.getDimension(R.styleable.RippleView_centerImageSize, IMAGE_DEFAULT_SIZE)
        rippleSize =
            typedArray.getDimension(R.styleable.RippleView_rippleSize, IMAGE_DEFAULT_SIZE / 2)
        rippleCount = typedArray.getInteger(R.styleable.RippleView_rippleCount, 4)
        spreadColor = typedArray.getColor(R.styleable.RippleView_spreadColor, Color.BLUE)
        delayMilliSeconds = typedArray.getInteger(
            R.styleable.RippleView_delayMilliSeconds,
            DETAULT_DELAYMILLISECONDS
        )
        typedArray.recycle()
        // 计算空间大小
        maxSize = centerImageSize + 2 * rippleSize * rippleCount
        // 通过资源ID获取Bitmao
        centerBitmap = BitmapFactory.decodeResource(resources, centerImageRes)
        // 设置涟漪画笔风格
        spreadPaint.style = Paint.Style.STROKE
        // 设置涟漪画笔宽度
        spreadPaint.strokeWidth = rippleSize
        // 设置涟漪画笔颜色
        spreadPaint.color = spreadColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 空间大小改变刷新中心点的坐标
        centerX = w / 2
        centerY = h / 2

        // 设置中心图片的位置
        centerImageRect.set(
            (centerX - centerImageSize / 2).toInt(),
            (centerY - centerImageSize / 2).toInt(),
            (centerX + centerImageSize / 2).toInt(),
            (centerY + centerImageSize / 2).toInt()
        )

        // 裁剪中心图片为圆形
        centerImageClipPath.addCircle(
            centerX.toFloat(),
            centerY.toFloat(),
            centerImageSize / 2,
            Path.Direction.CCW
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 设置控件的大小（正矩形）
        setMeasuredDimension(maxSize.toInt(), maxSize.toInt())
    }

    // 当前那个涟漪（1开始）
    private var currentCount = DEFAULT_CURRENT_COUNT

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //
        spreadPaint.alpha = 255 - (255 * ((currentCount + 1) / (rippleCount + 2).toFloat())).toInt()
        canvas.drawCircle(
            centerX.toFloat(),
            centerY.toFloat(),
            centerImageSize / 2 + (rippleSize / 2) + ((currentCount - 1) * rippleSize),
            spreadPaint
        )
        currentCount++
        if (currentCount > rippleCount) currentCount = DEFAULT_CURRENT_COUNT


        // 绘制中心的图片并裁剪
        canvas.save()
        canvas.clipPath(centerImageClipPath)
        canvas.drawBitmap(centerBitmap, null, centerImageRect, centerPaint)
        canvas.restore()

        // 设置绘制延时
        postInvalidateDelayed(if (currentCount == rippleCount) (delayMilliSeconds / 2).toLong() else delayMilliSeconds.toLong())
    }

}