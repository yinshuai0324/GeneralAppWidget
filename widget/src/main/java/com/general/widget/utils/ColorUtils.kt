package com.general.widget.utils

/**
 * 创建者：yinshuai
 * 创建时间：2021/8/4 19:25
 * 作用描述：颜色工具类
 */
class ColorUtils {
    companion object {

        /**
         * 给color添加透明度
         * @param alpha 透明度 0f～1f
         * @param color 基本颜色
         */
        fun getColorAlpha(alpha: Float, color: Int): Int {
            val a = 255.coerceAtMost(0.coerceAtLeast((alpha * 255).toInt())) shl 24
            val rgb = 0x00ffffff and color
            return a + rgb
        }
    }
}