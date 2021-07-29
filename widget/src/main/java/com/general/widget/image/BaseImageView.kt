package com.general.widget.image

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.general.widget.WidgetManage

/**
 * 创建者：yinshuai
 * 创建时间：2021/7/29 19:38
 * 作用描述：ImageView的基类
 */
open class BaseImageView : AppCompatImageView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    private val debugInfoPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        drawDebugInfo(canvas)
    }


    /**
     * 绘制debug信息
     */
    private fun drawDebugInfo(canvas: Canvas) {
        if (WidgetManage.isDebug) {
            debugInfoPaint.color = Color.parseColor("#FF0000")
            debugInfoPaint.textSize = 35f
            val value = getImgDisplaySize()
            canvas.drawText("尺寸:${value.first}*${value.second}", 20f, 45f, debugInfoPaint)
        }
    }

    /**
     * 正在显示的真实图片宽高
     */
    private fun getImgDisplaySize(): Pair<Int, Int> {
        if (drawable != null) {
            //获得ImageView中Image的真实宽高
            val dw = drawable.bounds.width()
            val dh = drawable.bounds.height()
            //获得ImageView中Image的变换矩阵
            val m = imageMatrix
            val values = FloatArray(10)
            m.getValues(values)
            //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
            val sx = values[0]
            val sy = values[4]

            val width = (dw * sx).toInt()
            val height = (dh * sy).toInt()
            return Pair(width, height)
        }
        return Pair(0, 0)
    }
}