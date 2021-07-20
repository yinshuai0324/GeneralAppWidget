package com.general.widget.image

import android.content.Context
import coil.ImageLoader

/**
 * 创建者：yinshuai
 * 创建时间：2021/7/20 15:38
 * 作用描述：图片加载管理
 */
object ImageLoaderManage {
    private lateinit var loaderManage: ImageLoader

    /**
     * 获取图片加载器
     */
    fun getImageLoader(context: Context): ImageLoader {
        if (!::loaderManage.isInitialized) {
            loaderManage = ImageLoader.Builder(context)
                .availableMemoryPercentage(0.25)
                .crossfade(true)
                .build()
        }
        return loaderManage
    }
}