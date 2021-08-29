package com.app.widget.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.app.widget.R
import com.general.widget.toast.AppToast

class ToastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toast)
    }


    fun onToastClick(view: View) {
        when (view.id) {
            R.id.infoBtn -> {
                AppToast.showInfoToast(this, "信息正在提交中", Toast.LENGTH_LONG)
            }
            R.id.errorBtn -> {
                AppToast.showErrorToast(this, "网络请求失败", Toast.LENGTH_LONG)
            }
            R.id.wBtn -> {
                AppToast.showWarningToast(this, "上传图片失败，请重试", Toast.LENGTH_LONG)
            }
            R.id.succeedBtn -> {
                AppToast.showSucceedToast(this, "提交数据成功", Toast.LENGTH_LONG)
            }
        }
    }
}