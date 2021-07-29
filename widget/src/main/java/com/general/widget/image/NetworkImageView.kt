package com.general.widget.image

import android.content.Context
import android.graphics.Canvas
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import coil.annotation.ExperimentalCoilApi
import coil.request.ImageRequest
import coil.target.ImageViewTarget
import com.general.widget.R
import com.general.widget.WidgetManage

/**
 * 创建者：yinshuai
 * 创建时间：2021/7/19 18:38
 * 作用描述：网络图片
 */

class NetworkImageView : BaseImageView, NetworkImageViewTarget.OnLoadingStatusChangeListener {
    private var imageUrl: String = ""
    private var loadingRes: Int = 0
    private var errorRes: Int = 0
    private var loadCallback: Boolean = false

    /**
     * 图片加载监听
     */
    lateinit var onLoaderListener: (isSucceed: Boolean) -> Unit

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.NetworkImageView)
        imageUrl = attrs.getString(R.styleable.NetworkImageView_url) ?: ""
        loadingRes = attrs.getResourceId(R.styleable.NetworkImageView_loadingRes, 0)
        errorRes = attrs.getResourceId(R.styleable.NetworkImageView_errorRes, 0)
        loadCallback = attrs.getBoolean(R.styleable.NetworkImageView_loadCallback, false)
        attrs.recycle()
        //加载图片
        load(imageUrl)
    }


    /**
     * 加载图片
     */
    fun load(url: String?) {
        if (!TextUtils.isEmpty(url)) {
            this.imageUrl = url ?: ""
            loadImage()
        }
    }


    /**
     * 加载图片 入队
     */
    private fun loadImage() {
        val target = NetworkImageViewTarget(this)
        target.listener = this
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .target(target)
            .build()
        val imageLoaderManage = ImageLoaderManage.getImageLoader(context)
        val result = imageLoaderManage.enqueue(request)
    }

    override fun onStart() {
    }

    override fun onError() {
        if (::onLoaderListener.isInitialized) {
            onLoaderListener.invoke(false)
        }
    }

    override fun onSuccess() {
        if (::onLoaderListener.isInitialized) {
            onLoaderListener.invoke(true)
        }
    }
}