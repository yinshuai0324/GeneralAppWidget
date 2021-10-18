package com.general.widget.button

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.general.widget.R

class SwitchButton : LinearLayout, View.OnClickListener {
    private var switchIcon: ImageView
    private var offIcon: Int = 0
    private var onIcon: Int = 0
    private var isOn: Boolean = false

    lateinit var onSwitchChangeListener: (isOn: Boolean) -> Unit

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        View.inflate(context, R.layout.view_swicth_button, this)
        switchIcon = findViewById(R.id.switchIcon)
        switchIcon.setOnClickListener(this)

        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.SwitchButton)
        offIcon = attrs.getResourceId(R.styleable.SwitchButton_offIcon, 0)
        onIcon = attrs.getResourceId(R.styleable.SwitchButton_onIcon, 0)
        isOn = attrs.getBoolean(R.styleable.SwitchButton_isOn, false)
        attrs.recycle()
        updateUI()
    }

    private fun updateUI() {
        val iconRes = if (isOn) {
            onIcon
        } else {
            offIcon
        }
        switchIcon.setImageResource(iconRes)
    }


    fun setOn(on: Boolean) {
        this.isOn = on
        updateUI()
    }

    fun isOn(): Boolean {
        return isOn
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.switchIcon -> {
                isOn = !isOn
                updateUI()
                if (::onSwitchChangeListener.isInitialized) {
                    onSwitchChangeListener.invoke(isOn)
                }
            }
        }
    }
}