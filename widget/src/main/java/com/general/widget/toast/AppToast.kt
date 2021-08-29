package com.general.widget.toast

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.general.widget.R
import com.general.widget.expand.dp

object AppToast {

    private var lastToast: Toast? = null


    /**
     * 显示info Toast
     */
    fun showInfoToast(context: Context, msg: String, duration: Int) {
        customToast(
            context,
            ToastType.INFO,
            msg,
            true,
            R.color.color_FFFFFF,
            duration
        ).show()
    }


    /**
     * 显示警告 Toast
     */
    fun showWarningToast(context: Context, msg: String, duration: Int) {
        customToast(
            context,
            ToastType.WARNING,
            msg,
            true,
            R.color.color_FFFFFF,
            duration
        ).show()
    }


    /**
     * 显示警告 Toast
     */
    fun showErrorToast(context: Context, msg: String, duration: Int) {
        customToast(
            context,
            ToastType.ERROR,
            msg,
            true,
            R.color.color_FFFFFF,
            duration
        ).show()
    }


    /**
     * 显示警告 Toast
     */
    fun showSucceedToast(context: Context, msg: String, duration: Int) {
        customToast(
            context,
            ToastType.SUCCEED,
            msg,
            true,
            R.color.color_FFFFFF,
            duration
        ).show()
    }


    /**
     * 自定义Toast
     */
    private fun customToast(
        context: Context,
        type: ToastType,
        msgStr: CharSequence,
        showIcon: Boolean,
        textColor: Int,
        duration: Int,
    ): Toast {
        val toast = Toast.makeText(context, "", duration)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.toast_view, null)
        view.background = getToastDrawable(context, type)
        val icon = view.findViewById<ImageView>(R.id.icon)
        val msg = view.findViewById<TextView>(R.id.msg)
        icon.setImageResource(getTypeIcon(type))
        msg.text = "${msgStr ?: ""}"
        msg.setTextColor(ContextCompat.getColor(context,textColor))
        icon.visibility = if (showIcon) View.VISIBLE else View.GONE
        toast.view = view
        if (lastToast != null) {
            lastToast?.cancel()
        }
        lastToast = toast
        return toast
    }


    /**
     * 获取Toast的背景
     */
    private fun getToastDrawable(context: Context, type: ToastType): Drawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = 25f.dp
        val color = when (type) {
            ToastType.INFO -> R.color.color_AFB4DB
            ToastType.ERROR -> R.color.color_EF4136
            ToastType.WARNING -> R.color.color_F47920
            ToastType.SUCCEED -> R.color.color_45B97C
        }
        drawable.setColor(ContextCompat.getColor(context, color))
        return drawable
    }


    /**
     * 获取Icon类型
     */
    private fun getTypeIcon(type: ToastType): Int {
        return when (type) {
            ToastType.INFO -> R.drawable.ic_toast_info
            ToastType.ERROR -> R.drawable.ic_toast_error
            ToastType.WARNING -> R.drawable.ic_toast_warning
            ToastType.SUCCEED -> R.drawable.ic_toast_succeed
        }
    }


    enum class ToastType {
        INFO,
        ERROR,
        WARNING,
        SUCCEED
    }

}