package com.general.widget.layout

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.general.widget.R


/**
 * 创建者：yinshuai
 * 创建时间：2021/8/4 14:08
 * 作用描述：圆角帧布局
 */
class RoundFrameLayout : FrameLayout, RoundLayout {

    private val helper = RoundHelper()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        helper.init(context, attributeSet, this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        helper.onSizeChange(w, h)
    }


    override fun dispatchDraw(canvas: Canvas) {
        helper.onBeforeDraw(canvas)
        super.dispatchDraw(canvas)
        helper.onAfterDraw(canvas)
    }

    override fun setBorderWidth(width: Float) {
        helper.setBorderWidth(width)
    }

    override fun setBorderColor(color: Int) {
        helper.setBorderColor(color)
    }

    override fun setBorder(width: Float, color: Int) {
        helper.setBorder(width, color)
    }

    override fun setRadius(radius: Float) {
        helper.setRadius(radius)
    }

    override fun setRadius(topL: Float, topR: Float, bottomL: Float, bottomR: Float) {
        helper.setRadius(topL, topR, bottomL, bottomR)
    }

    override fun setIsCircle(circle: Boolean) {
        helper.setIsCircle(circle)
    }

    override fun setBackgroundColors(color: Int) {
        helper.setBackgroundColor(color)
    }
}