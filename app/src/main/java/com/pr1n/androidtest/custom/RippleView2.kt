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

// Ripple控件的默认大小
//private val RIPPLE_MAX_SIZE = IMAGE_DEFAULT_SIZE * 5

// 默认涟漪每帧移动的距离
private const val DETAULT_DISTANCE = 5F

// 默认绘制刷新的延时
private const val DETAULT_DELAYMILLISECONDS = 40

// 默认涟漪集合的大小
//private const val SPREAD_COUNT = 6

/**
 * 涟漪效果的ImageView
 */
class RippleView2 @JvmOverloads constructor(
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

    // Ripple的大小
    private var maxSize: Float

    // 涟漪的大小
    private var rippleSize: Float

    //
    private var rippleCount: Int

    // 涟漪每帧移动的距离
    private var distance: Float

    // 涟漪效果的延时
    private var delayMilliSeconds: Int

    private var spreadCount: Int

    // 存储涟漪效果集合(first = alpha, second = width) 大小决定涟漪同时显示的上限
    private val spreadRadiusList = mutableListOf<Pair<Int, Int>>()

    // 中心图片的位置
    private val centerImageRect = Rect()

    // 裁剪中心图片
    private val centerImageClipPath = Path()

    init {
        // 初始化
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleView)
        val centerImageRes = typedArray.getResourceId(R.styleable.RippleView_centerImageRes, 0)
        centerImageSize =
            typedArray.getDimension(R.styleable.RippleView_centerImageSize, IMAGE_DEFAULT_SIZE)
        rippleSize =
            typedArray.getDimension(R.styleable.RippleView_rippleSize, IMAGE_DEFAULT_SIZE / 2)
        rippleCount = typedArray.getInteger(R.styleable.RippleView_rippleCount, 4)
        val spreadColor = typedArray.getColor(R.styleable.RippleView_spreadColor, Color.BLUE)
        typedArray.recycle()

        maxSize = centerImageSize + rippleSize * rippleCount
        distance = 2F
        delayMilliSeconds = DETAULT_DELAYMILLISECONDS * 10
        spreadCount = rippleCount + 2

        // 通过资源ID获取Bitmao
        centerBitmap = BitmapFactory.decodeResource(resources, centerImageRes)
        // 设置涟漪的颜色
        spreadPaint.color = spreadColor
        spreadRadiusList += 255 to 0
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
        // 设置空间的大小（正矩形）
        setMeasuredDimension(maxSize.toInt() + rippleSize.toInt(), maxSize.toInt() + rippleSize.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 循环涟漪集合
        (0 until spreadRadiusList.size).forEach {
            // 获取alpha和width
            val (alpha, width) = spreadRadiusList[it]
            // 设置透明度
            spreadPaint.alpha = alpha
            // 画涟漪
            canvas.drawCircle(
                centerX.toFloat(),
                centerY.toFloat(),
                (centerImageSize + width) / 2F,
                spreadPaint
            )

            // 更新涟漪的alpha和width
            if (alpha > 0 && width < maxSize)
                spreadRadiusList[it] =
                    //(255 * (width / (maxSize - centerImageSize))).toInt() to (width + distance.dp).toInt()
                    (if (alpha > 0) (alpha - distance * 2).toInt() else 0) to (width + distance.dp).toInt()

            // 当最后一个(最小的涟漪)大小超过rippleSize时加添新的涟漪
            if (spreadRadiusList.last().second > rippleSize.toInt())
                spreadRadiusList += 255 to 0

            // 达到涟漪上限时移除最外的涟漪
            if (spreadRadiusList.size >= spreadCount || spreadRadiusList.first().second >= maxSize)
                spreadRadiusList.removeAt(0)
        }

        //canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), centerImageSize / 2, centerPaint)

        // 绘制中心的图片并裁剪
        canvas.save()
        canvas.clipPath(centerImageClipPath)
        canvas.drawBitmap(centerBitmap, null, centerImageRect, centerPaint)
        canvas.restore()

        // 设置绘制延时
        postInvalidateDelayed(delayMilliSeconds.toLong())
    }

}