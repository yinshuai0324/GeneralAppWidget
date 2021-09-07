package com.app.widget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.app.widget.databinding.ActivityMainBinding
import com.app.widget.ui.ButtonActivity
import com.app.widget.ui.ImageActivity
import com.app.widget.ui.LayoutActivity

class MainActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }


    fun onHomeBtnClick(view: View) {
        when (view.id) {
            R.id.imageBtn -> startActivity(Intent(this, ImageActivity::class.java))
            R.id.layoutBtn -> startActivity(Intent(this, LayoutActivity::class.java))
            R.id.buttonBtn -> startActivity(Intent(this, ButtonActivity::class.java))
        }
    }

}