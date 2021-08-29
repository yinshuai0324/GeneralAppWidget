package com.general.widget.layout

/**
 * 创建者：yinshuai
 * 创建时间：2021/8/4 13:56
 * 作用描述：
 */
interface RoundLayout {
    /**
     * 设置边框宽度
     */
    fun setBorderWidth(width: Float)

    /**
     * 设置边框颜色
     */
    fun setBorderColor(color: Int)

    /**
     * 设置边框信息
     */
    fun setBorder(width: Float, color: Int)

    /**
     * 设置圆角
     */
    fun setRadius(radius: Float)

    /**
     * 设置圆角
     */
    fun setRadius(topL: Float, topR: Float, bottomL: Float, bottomR: Float)

    /**
     * 设置是否圆形
     */
    fun setIsCircle(circle: Boolean)
}