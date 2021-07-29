package com.general.widget.image

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.general.widget.R
import java.lang.ref.WeakReference
import kotlin.math.max

/**
 * 创建者：yinshuai
 * 创建时间：2021/7/20 16:51
 * 作用描述：圆角图片
 */
open class RoundImageView : BaseImageView {

    private var radius: Float = 0f
    private var borderColor: Int = 0
    private var borderWidth: Float = 0f
    private var radiusTopLeft: Float = 0f
    private var radiusTopRight: Float = 0f
    private var radiusBottomRight: Float = 0f
    private var radiusBottomLeft: Float = 0f
    private var paddingColor: Int = 0
    private var borderCover: Boolean = false


    /**
     * 圆角Path
     */
    private var roundPath = Path()

    /**
     * 边框Path
     */
    private var borderPath = Path()

    /**
     * padding的矩形
     */
    private var paddingPath = Path()

    /**
     * 圆角值
     */
    private val roundParams: FloatArray = FloatArray(8)

    /**
     * 画笔
     */
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 混合模式
     */
    private val xFermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    /**
     * bit
     */
    private val bitmapScope = Rect()

    /**
     * bitmap绘制的位置
     */
    private val bitmapPosition = RectF()

    /**
     * 要显示的图片
     */
    private var bitmap: WeakReference<Bitmap?>? = null


    constructor(context: Context) : this(context, null)


    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.RoundImageView)

        radius = attrs.getDimension(R.styleable.RoundImageView_radius, 0f)
        borderWidth = attrs.getDimension(R.styleable.RoundImageView_borderWidth, 0f)
        borderColor = attrs.getColor(R.styleable.RoundImageView_borderColor, 0)
        radiusTopLeft = attrs.getDimension(R.styleable.RoundImageView_radiusTopLeft, 0f)
        radiusTopRight = attrs.getDimension(R.styleable.RoundImageView_radiusTopRight, 0f)
        radiusBottomRight = attrs.getDimension(R.styleable.RoundImageView_radiusBottomRight, 0f)
        radiusBottomLeft = attrs.getDimension(R.styleable.RoundImageView_radiusBottomLeft, 0f)
        borderCover = attrs.getBoolean(R.styleable.RoundImageView_borderCover, false)
        paddingColor =
            attrs.getColor(R.styleable.RoundImageView_paddingColor, Color.parseColor("#FFFFFF"))
        attrs.recycle()
        updateBitmap(true)
        updateRoundParams()
    }


    override fun onDraw(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()
        //设置圆角矩形的参数
        roundPath.addRoundRect(0f, 0f, width, height, roundParams, Path.Direction.CW)
        roundPath.close()
        //设置边框的参数
        borderPath.addPath(roundPath)
        //设置padding 参数
        val pLeft = paddingLeft.toFloat()
        val pTop = paddingTop.toFloat()
        val pRight = width - paddingRight
        val pBottom = height - paddingBottom
        paddingPath.addRect(pLeft, pTop, pRight, pBottom, Path.Direction.CW)
        paddingPath.close()
        //设置绘制模式
        borderPath.fillType = Path.FillType.WINDING
        roundPath.fillType = Path.FillType.INVERSE_WINDING
        //新建图层
        canvas.saveLayer(0f, 0f, width, height, null)
        val bitmap = bitmap?.get()
        if (bitmap != null) {
            //锁定画布
            canvas.save()
            if (borderWidth > 0) {
                bitmapScope.left = 0
                bitmapScope.top = 0
                bitmapScope.right = bitmap.width
                bitmapScope.bottom = bitmap.height

                val borderValue = if (borderCover) 0f else borderWidth / 2

                bitmapPosition.left = borderValue + paddingLeft
                bitmapPosition.top = borderValue + paddingTop
                bitmapPosition.right = width - borderValue - paddingRight
                bitmapPosition.bottom = height - borderValue - paddingBottom
                //更新画笔
                updateBitmapPaint(bitmap)
                //画图片
                canvas.drawBitmap(bitmap, bitmapScope, bitmapPosition, paint)
            } else {
                //如果没有边框的话 直接调用原本的方法进行绘制
                super.onDraw(canvas)
            }
            //恢复
            canvas.restore()
        }
        super.onDraw(canvas)
        //绘制边框
        if (borderWidth > 0 && borderColor != 0) {
            //画padding的矩形
            updatePaddingPaint()
            canvas.drawPath(paddingPath, paint)
            //话边框的矩形
            updateDrawBorderPaint()
            canvas.drawPath(borderPath, paint)
        }
        //绘制圆角
        updateDrawRoundPaint()
        canvas.drawPath(roundPath, paint)
        canvas.restore()
    }

    private fun updateBitmapPaint(bitmap: Bitmap) {
        if (paint.shader == null) {
            paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
    }

    private fun updatePaddingPaint() {
        paint.shader = null
        paint.strokeWidth = paddingLeft.toFloat()
        paint.style = Paint.Style.STROKE
        paint.color = paddingColor
        paint.xfermode = null
    }

    private fun updateDrawBorderPaint() {
        paint.strokeWidth = borderWidth
        paint.style = Paint.Style.STROKE
        paint.color = borderColor
        paint.xfermode = null
    }

    private fun updateDrawRoundPaint() {
        paint.style = Paint.Style.FILL
        paint.xfermode = xFermode
        paint.color = Color.parseColor("#FF0000")
    }

    /**
     * 更新圆角参数
     */
    private fun updateRoundParams() {
        roundParams?.let {
            if (radius > 0) {
                roundParams.forEachIndexed { index, _ ->
                    roundParams[index] = radius
                }
            } else {
                roundParams[0] = radiusTopLeft
                roundParams[1] = radiusTopLeft
                roundParams[2] = radiusTopRight
                roundParams[3] = radiusTopRight
                roundParams[4] = radiusBottomLeft
                roundParams[5] = radiusBottomLeft
                roundParams[6] = radiusBottomRight
                roundParams[7] = radiusBottomRight
            }
        }

    }

    /**
     * 更新Bitmap
     */
    private fun updateBitmap(isCache: Boolean) {
        if (isCache) {
            if (bitmap == null) {
                bitmap = WeakReference(getBitmap())
            }
        } else {
            bitmap = WeakReference(getBitmap())
        }
    }

    /**
     * 获取bitmap
     */
    private fun getBitmap(): Bitmap? {
        if (drawable != null) {
            if (drawable is BitmapDrawable) {
                //如果是BitmapDrawable 则直接返回Bitmap
                return (drawable as BitmapDrawable).bitmap
            }
            val drawableHeight = drawable.intrinsicHeight
            val drawableWidth = drawable.intrinsicWidth
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val bitmapCanvas = Canvas(bitmap)
            //如果图片的宽或者高与view的宽高不匹配,计算出需要缩放的比例,缩放后的图片的宽高,一定要大于我们view的宽高,所以我们这里取大值
            val scale = max(width / 1.0f / drawableWidth, height * 1.0f / drawableHeight)
            //根据缩放比例,设置bounds,相当于缩放图片了
            val scaleWidth = (scale * drawableWidth).toInt()
            val scaleHeight = (scale * drawableHeight).toInt()
            drawable.setBounds(0, 0, scaleWidth, scaleHeight)
            drawable.draw(bitmapCanvas)
            return bitmap
        }
        return null
    }

    /**
     * 设置圆角
     */
    fun setRadius(radius: Float) {
        this.radius = radius
        updateRoundParams()
        invalidate()
    }

    /**
     * 设置圆角
     */
    fun setRadius(topL: Float, topR: Float, bottomL: Float, bottomR: Float) {
        this.radiusTopLeft = topL
        this.radiusTopRight = topR
        this.radiusBottomLeft = bottomL
        this.radiusBottomRight = bottomR
        updateRoundParams()
        invalidate()
    }

    /**
     * 设置边框参数
     */
    fun setBorder(width: Float, color: Int) {
        this.borderWidth = width
        this.borderColor = color
        updateRoundParams()
        invalidate()
    }

    /**
     * 设置边框宽度
     */
    fun setBorderWidth(width: Float) {
        this.borderWidth = width
        updateRoundParams()
        invalidate()
    }

    /**
     * 设置边框颜色
     */
    fun setBorderColor(color: Int) {
        this.borderColor = color
        updateRoundParams()
        invalidate()
    }

    /**
     * 设置边框是否覆盖图片
     */
    fun setBorderCover(cover: Boolean) {
        this.borderCover = cover
        updateRoundParams()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateRoundParams()
        invalidate()
    }


    override fun invalidateDrawable(dr: Drawable) {
        super.invalidateDrawable(dr)
        updateRoundParams()
        invalidate()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        updateRoundParams()
        invalidate()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        updateRoundParams()
        invalidate()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        updateBitmap(false)
        invalidate()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        updateBitmap(false)
        invalidate()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        updateBitmap(false)
        invalidate()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        updateBitmap(false)
        invalidate()
    }

}