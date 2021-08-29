package com.app.widget.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.widget.R
import com.general.widget.button.AppButton

class ButtonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button)

        findViewById<AppButton>(R.id.btn).onEnableClickEvent = {
            Toast.makeText(this, "触发", Toast.LENGTH_SHORT).show()
        }
    }
}