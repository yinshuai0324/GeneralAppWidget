package com.general.widget.image

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.general.widget.R
import kotlin.math.max
import kotlin.math.min

/**
 * 创建者：yinshuai
 * 创建时间：2021/7/20 16:51
 * 作用描述：圆角图片
 */
class RoundImageView : AppCompatImageView {

    private var radius: Float = 0f
    private var borderColor: Int = 0
    private var borderWidth: Float = 0f
    private var borderIsOverlay: Boolean = false


    /******参数部分******/

    /**
     * 圆角的区域
     */
    private var mRoundF = RectF()

    /**
     * 画笔
     */
    private val paint = Paint()

    /**
     * 边框矩形
     */
    private val mBorderRect = RectF()

    /**
     * 内容绘制区域
     */
    private val mDrawableRect = RectF()


    constructor(context: Context) : this(context, null)


    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.RoundImageView)
        radius = attrs.getDimension(R.styleable.RoundImageView_radius, 0f)
        borderWidth = attrs.getDimension(R.styleable.RoundImageView_borderWidth, 0f)
        borderColor = attrs.getColor(R.styleable.RoundImageView_borderColor, 0)
        attrs.recycle()
        initData()
    }

    private fun initData() {
        paint.isAntiAlias = true
        mRoundF = RectF()
    }


    /**
     * 更新绘制参数
     */
    private fun updateParams() {
        //边框的绘制区域
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom
        //边长 已最小的边为准
        val sideLength = min(availableWidth, availableHeight)
        mBorderRect.left = paddingLeft + (availableWidth - sideLength) / 2f
        mBorderRect.top = paddingTop + (availableHeight - sideLength) / 2f
        mBorderRect.right = mBorderRect.left + sideLength
        mBorderRect.bottom = mBorderRect.top + sideLength

        //内容绘制区域
        mDrawableRect.set(mBorderRect)
        if (!borderIsOverlay && borderWidth > 0) {
            mDrawableRect.inset(borderWidth - 1.0f, borderWidth - 1.0f)
        }

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }


    override fun onDraw(canvas: Canvas) {
    }


    /**
     * 获取bitmap
     */
    private fun getBitmap(): Bitmap? {
        if (drawable != null) {
            if (drawable is BitmapDrawable) {
                //如果是BitmapDrawable 则直接返回Bitmap
                return (drawable as BitmapDrawable).bitmap
            }
            val drawableHeight = drawable.intrinsicHeight
            val drawableWidth = drawable.intrinsicWidth
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val bitmapCanvas = Canvas(bitmap)
            //如果图片的宽或者高与view的宽高不匹配,计算出需要缩放的比例,缩放后的图片的宽高,一定要大于我们view的宽高,所以我们这里取大值
            val scale = max(width / 1.0f / drawableWidth, height * 1.0f / drawableHeight)
            //根据缩放比例,设置bounds,相当于缩放图片了
            val scaleWidth = (scale * drawableWidth).toInt()
            val scaleHeight = (scale * drawableHeight).toInt()
            drawable.setBounds(0, 0, scaleWidth, scaleHeight)
            drawable.draw(bitmapCanvas)
            return bitmap
        }
        return null
    }
}