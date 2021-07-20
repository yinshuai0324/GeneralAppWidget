package com.app.widget.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.widget.R
import com.app.widget.databinding.ActivityImageBinding
import com.app.widget.databinding.ActivityMainBinding

class ImageActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initData()
    }


    private fun initData() {
        viewBinding.networkImage.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201708%2F23%2F20170823102305_WeA2y.thumb.700_0.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1629362162&t=8c9d0da851c6f1ea43770ddde4746e28")
        viewBinding.networkImage.onLoaderListener = {
            Log.i("===>>>", "图片加载状态:${it}")
        }
    }
}