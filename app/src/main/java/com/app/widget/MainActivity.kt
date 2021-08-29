package com.app.widget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.app.widget.databinding.ActivityMainBinding
import com.app.widget.ui.ImageActivity
import com.app.widget.ui.ToastActivity

class MainActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
    }


    fun onHomeBtnClick(view: View) {
        when (view.id) {
            R.id.imageBtn -> startActivity(Intent(this, ImageActivity::class.java))
            R.id.toastBtn -> startActivity(Intent(this, ToastActivity::class.java))
        }
    }

}