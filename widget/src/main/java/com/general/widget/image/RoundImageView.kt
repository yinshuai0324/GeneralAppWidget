package com.general.widget.image

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.general.widget.R

/**
 * 创建者：yinshuai
 * 创建时间：2021/7/20 16:51
 * 作用描述：圆角图片
 */
class RoundImageView : AppCompatImageView {

    private var radius: Float = 0f
    private var borderColor: Int = 0
    private var borderWidth: Float = 0f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.RoundImageView)
        radius = attrs.getDimension(R.styleable.RoundImageView_radius, 0f)
        borderWidth = attrs.getDimension(R.styleable.RoundImageView_borderWidth, 0f)
        borderColor = attrs.getColor(R.styleable.RoundImageView_borderColor, 0)
        attrs.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}