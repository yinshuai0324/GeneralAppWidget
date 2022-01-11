package com.general.widget.layout

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.general.widget.R

/**
 * 创建者：yinshuai
 * 创建时间：2021/8/4 15:56
 * 作用描述：圆角帮助类
 */
class RoundHelper {

    private lateinit var mContext: Context

    /**
     * View的矩形
     */
    private val viewRectF = RectF()

    /**
     * 边框矩形
     */
    private val borderRectF = RectF()

    /**
     * 绘制的Path
     */
    private val drawPath = Path()

    /**
     * 兼容高版本的临时Path
     */
    private val mTempPath = Path()

    /**
     * 圆角值
     */
    private val roundParams: FloatArray = FloatArray(8)

    /**
     * 边框圆角参数
     */
    private val borderRoundParams: FloatArray = FloatArray(8)

    /**
     * 画笔
     */
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 混合模式
     */
    private val xfermode =
        PorterDuffXfermode(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PorterDuff.Mode.DST_OUT else PorterDuff.Mode.DST_IN)

    /**
     * View
     */
    private lateinit var rootView: View

    /**********参数部分************/

    /**
     * 边框宽度
     */
    private var borderWidth = 0f

    /**
     * 边框颜色
     */
    private var borderColor = Color.parseColor("#FF0000")

    /**
     * 四个角的圆角
     */
    private var radius = 0f

    /**
     * 左上角圆角
     */
    private var radiusTopLeft = 0f

    /**
     * 右上角圆角
     */
    private var radiusTopRight = 0f

    /**
     * 左下角圆角
     */
    private var radiusBottomLeft = 0f

    /**
     * 右下角圆角
     */
    private var radiusBottomRight = 0f

    /**
     * 是否是圆
     */
    private var isCircle = false

    /**
     * View的宽度
     */
    private var width: Int = 0

    /**
     * View的高度
     */
    private var height: Int = 0

    /**
     * 背景颜色
     */
    private var backgroundColor: Int = 0


    /**
     * 初始化配置
     */
    fun init(context: Context, attributeSet: AttributeSet?, view: View) {
        mContext = context
        rootView = view
        if (rootView.background != null && rootView.background is ColorDrawable) {
            backgroundColor = (rootView.background as ColorDrawable)?.color
        }
        //把背景设置为透明 等下自己绘制颜色
        val defaultColor = Color.parseColor("#00000000")
        rootView.setBackgroundColor(defaultColor)
        attributeSet?.let {
            val attr = context.obtainStyledAttributes(it, R.styleable.RoundLayout)
            borderWidth = attr.getDimension(R.styleable.RoundLayout_borderWidth, 0f)
            borderColor = attr.getColor(R.styleable.RoundLayout_borderColor, Color.WHITE)
            radiusTopLeft = attr.getDimension(R.styleable.RoundLayout_radiusTopLeft, 0f)
            radiusTopRight = attr.getDimension(R.styleable.RoundLayout_radiusTopRight, 0f)
            radiusBottomLeft = attr.getDimension(R.styleable.RoundLayout_radiusBottomLeft, 0f)
            radiusBottomRight = attr.getDimension(R.styleable.RoundLayout_radiusBottomRight, 0f)
            radius = attr.getDimension(R.styleable.RoundLayout_radius, 0f)
            isCircle = attr.getBoolean(R.styleable.RoundLayout_isCircle, false)
            attr.recycle()
        }
    }


    /**
     * 大小改变
     */
    fun onSizeChange(width: Int, height: Int) {
        this.width = width
        this.height = height
        updateParams()
    }

    /**
     * 更新绘制参数
     */
    private fun updateParams() {
        //更新View的最新宽高区域
        viewRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        //更新边框矩形区域
        val left = borderWidth / 2
        val top = borderWidth / 2
        val right = width - borderWidth / 2
        val bottom = height - borderWidth / 2
        borderRectF.set(left, top, right, bottom)

        if (isCircle) {
            radius = height.coerceAtMost(width) * 1f / 2 - borderWidth
        }

        if (radius > 0) {
            radiusTopLeft = radius
            radiusTopRight = radius
            radiusBottomLeft = radius
            radiusBottomRight = radius
        }
        //更新圆角的值
        roundParams[0] = radiusTopLeft - borderWidth
        roundParams[1] = radiusTopLeft - borderWidth
        roundParams[2] = radiusTopRight - borderWidth
        roundParams[3] = radiusTopRight - borderWidth
        roundParams[4] = radiusBottomRight - borderWidth
        roundParams[5] = radiusBottomRight - borderWidth
        roundParams[6] = radiusBottomLeft - borderWidth
        roundParams[7] = radiusBottomLeft - borderWidth
        //更新边框圆角的值
        borderRoundParams[0] = radiusTopLeft
        borderRoundParams[1] = radiusTopLeft
        borderRoundParams[2] = radiusTopRight
        borderRoundParams[3] = radiusTopRight
        borderRoundParams[4] = radiusBottomRight
        borderRoundParams[5] = radiusBottomRight
        borderRoundParams[6] = radiusBottomLeft
        borderRoundParams[7] = radiusBottomLeft
    }


    /**
     * 在super.draw()之前绘制
     */
    fun onBeforeDraw(canvas: Canvas) {
        canvas.saveLayer(viewRectF, null)
        if (borderWidth > 0) {
            //处理画布会被边框覆盖的问题
            val sx = (width - 2 * borderWidth) / width
            val sy = (height - 2 * borderWidth) / height
            // 缩小画布，使内容不被borders覆盖
            canvas.scale(sx, sy, width / 2.0f, height / 2.0f)
        }
        //绘制背景颜色
        paint.color = backgroundColor
        canvas.drawRect(viewRectF, paint)
        paint.reset()
    }


    /**
     * 在super.draw()之后绘制
     */
    fun onAfterDraw(canvas: Canvas) {
        paint.reset()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        //开始绘制圆角
        paint.xfermode = xfermode
        drawPath.reset()

        val offset = 1
        if (borderWidth > 0) {
            //加这个的原因的 缩小画布的时候 会导致有1个像素的误差 在这里补齐
            viewRectF.left = viewRectF.left - offset
            viewRectF.top = viewRectF.top - offset
            viewRectF.right = viewRectF.right + offset
            viewRectF.bottom = viewRectF.bottom + offset
        }

        drawPath.addRoundRect(viewRectF, roundParams, Path.Direction.CCW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTempPath.reset()
            mTempPath.addRect(viewRectF, Path.Direction.CCW)
            mTempPath.op(drawPath, Path.Op.DIFFERENCE)
            canvas.drawPath(mTempPath, paint)
        } else {
            canvas.drawPath(drawPath, paint)
        }

        //去除混合模式
        paint.xfermode = null
        //恢复
        canvas.restore()


        //绘制边框
        if (borderWidth > 0) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = borderWidth
            paint.color = borderColor
            drawPath.reset()
            drawPath.addRoundRect(borderRectF, borderRoundParams, Path.Direction.CCW)
            canvas.drawPath(drawPath, paint)


        }
    }


    /**
     * 设置边框宽度
     */
    fun setBorderWidth(width: Float) {
        borderWidth = width
        updateParams()
        rootView.invalidate()
    }


    /**
     * 设置边框颜色
     */
    fun setBorderColor(color: Int) {
        borderColor = getColor(color)
        updateParams()
        rootView.invalidate()
    }

    /**
     * 设置边框参数
     */
    fun setBorder(width: Float, color: Int) {
        borderWidth = width
        borderColor = getColor(color)
        updateParams()
        rootView.invalidate()
    }

    /**
     * 设置圆角
     */
    fun setRadius(radius: Float) {
        this.radius = radius
        updateParams()
        rootView.invalidate()
    }

    /**
     * 设置圆角
     */
    fun setRadius(topL: Float, topR: Float, bottomL: Float, bottomR: Float) {
        this.radiusTopLeft = topL
        this.radiusTopRight = topR
        this.radiusBottomLeft = bottomL
        this.radiusBottomRight = bottomR
        updateParams()
        rootView.invalidate()
    }

    /**
     * 设置是否是圆形
     */
    fun setIsCircle(circle: Boolean) {
        this.isCircle = circle
        updateParams()
        rootView.invalidate()
    }


    /**
     * 获取颜色
     */
    fun getColor(color: Int): Int {
        return ContextCompat.getColor(mContext, color)
    }

}