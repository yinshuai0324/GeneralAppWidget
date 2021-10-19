package com.general.widget.button

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView
import com.general.widget.R
import com.general.widget.layout.RoundHelper
import com.general.widget.layout.RoundLayout
import com.general.widget.utils.ColorUtils

/**
 * 创建者：yinshuai
 * 创建时间：2021/8/4 17:21
 * 作用描述：按钮
 */
class AppButton : AppCompatTextView, RoundLayout, View.OnClickListener {

    /**
     * 圆角工具类
     */
    private val helper = RoundHelper()

    /**
     * 背景Path
     */
    private val bodyPath = Path()

    /**
     * 背景区域
     */
    private val bodyRectF = RectF()

    /**
     * 画笔
     */
    private val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 点击时的Color
     */
    private var clickColor: Int = 0

    /**
     * 背景颜色
     */
    private var bodyColor: Int = 0

    /**
     * 禁用时的颜色
     */
    private var disableColor: Int = 0

    /**
     * 是否禁用
     */
    private var disable: Boolean = false

    /**
     * 动画类型 1:默认效果 2.缩放动画效果
     */
    private var clickAnimType: Int = 1

    /**
     * 文字颜色
     */
    private var mTextColor: Int = 0

    /**
     * 按钮启用的情况下点击事件
     */
    lateinit var onEnableClickEvent: (view: View) -> Unit

    /**
     * 按钮禁用的情况下点击事件
     */
    lateinit var onDisableClickEvent: (view: View) -> Unit

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        helper.init(context, attributeSet, this)
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.AppButton)
        disable = attrs.getBoolean(R.styleable.AppButton_disable, false)
        disableColor = attrs.getColor(
            R.styleable.AppButton_disableColor,
            helper.getColor(R.color.color_D3D3D3)
        )
        clickAnimType = attrs.getInt(R.styleable.AppButton_clickAnimType, 1)
        attrs.recycle()
        //获取背景颜色
        bodyColor = if (background == null) {
            helper.getColor(R.color.color_3A5FCD)
        } else {
            if (background is ColorDrawable) {
                (background as ColorDrawable).color
            } else {
                helper.getColor(R.color.color_3A5FCD)
            }
        }
        //获取点击时的颜色
        clickColor = ColorUtils.getColorAlpha(0.5f, bodyColor)
        //设置画笔颜色
        bodyPaint.color = if (disable) disableColor else bodyColor
        //默认字体居中
        gravity = Gravity.CENTER
        //设置默认字体颜色
        setTextColor(currentTextColor)
        //设置点击事件
        this.setOnClickListener(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bodyRectF.set(0f, 0f, w.toFloat(), h.toFloat())
        bodyPath.addRect(bodyRectF, Path.Direction.CW)
        helper.onSizeChange(w, h)
    }

    override fun draw(canvas: Canvas) {
        helper.onBeforeDraw(canvas)
        super.draw(canvas)
        helper.onAfterDraw(canvas)
    }


    override fun onDraw(canvas: Canvas) {
        //绘制背景
        onDrawBody(canvas)
        super.onDraw(canvas)
    }


    private fun onDrawBody(canvas: Canvas) {
        canvas.drawPath(bodyPath, bodyPaint)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downClickAnim()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                upClickAnim()
            }
        }
        return super.onTouchEvent(event)
    }


    /**
     * 点击时的效果
     */
    private fun downClickAnim() {
        when (clickAnimType) {
            1 -> {
                //绘制背景颜色
                bodyPaint.color = clickColor
                super.setTextColor(ColorUtils.getColorAlpha(0.5f, mTextColor))
                invalidate()
            }
            2 -> {
                //缩放动画
                startScaleAnim(false)
            }
        }
    }

    /**
     * 手指抬起时的效果
     */
    private fun upClickAnim() {
        when (clickAnimType) {
            1 -> {
                //绘制背景颜色
                bodyPaint.color = bodyColor
                super.setTextColor(mTextColor)
                invalidate()
            }
            2 -> {
                //缩放动画
                startScaleAnim(true)
            }
        }
    }

    /**
     * 开始动画
     */
    private fun startScaleAnim(isUp: Boolean) {
        val startValue = if (isUp) {
            0.95f
        } else {
            1f
        }
        val endValue = if (isUp) {
            1f
        } else {
            0.95f
        }
        val animatorSet = AnimatorSet()
        val scaleX = ObjectAnimator.ofFloat(this, "scaleX", startValue, endValue)
        val scaleY = ObjectAnimator.ofFloat(this, "scaleY", startValue, endValue)
        animatorSet.duration = 100
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.play(scaleX).with(scaleY)
        animatorSet.start()
    }

    override fun setBorderWidth(width: Float) {
        helper.setBorderWidth(width)
    }

    override fun setBorderColor(color: Int) {
        helper.setBorderColor(color)
    }

    override fun setBorder(width: Float, color: Int) {
        helper.setBorder(width, color)
    }

    override fun setRadius(radius: Float) {
        helper.setRadius(radius)
    }

    override fun setRadius(topL: Float, topR: Float, bottomL: Float, bottomR: Float) {
        helper.setRadius(topL, topR, bottomL, bottomR)
    }

    override fun setIsCircle(circle: Boolean) {
        helper.setIsCircle(circle)
    }

    /**
     * 是否是禁用状态
     */
    fun isDisable(): Boolean {
        return disable
    }

    /**
     * 设置是否禁用
     */
    fun setDisable(isDisable: Boolean){
        this.disable = isDisable
        bodyPaint.color = if (disable) disableColor else bodyColor
        invalidate()
    }

    /**
     * 设置文字颜色
     */
    override fun setTextColor(color: Int) {
        this.mTextColor = color
        super.setTextColor(color)
    }

    override fun onClick(v: View) {
        if (disable) {
            if (::onDisableClickEvent.isInitialized) {
                onDisableClickEvent.invoke(this)
            }
        } else {
            if (::onEnableClickEvent.isInitialized) {
                onEnableClickEvent.invoke(this)
            }
        }
    }
}