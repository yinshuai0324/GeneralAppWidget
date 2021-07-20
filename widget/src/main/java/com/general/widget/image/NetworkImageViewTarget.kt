package com.general.widget.image

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import coil.annotation.ExperimentalCoilApi
import coil.target.ImageViewTarget
import coil.target.PoolableViewTarget
import coil.transition.TransitionTarget

/**
 * 创建者：yinshuai
 * 创建时间：2021/7/20 16:00
 * 作用描述：
 */
@ExperimentalCoilApi
open class NetworkImageViewTarget(override val view: AppCompatImageView) :
    PoolableViewTarget<ImageView>,
    TransitionTarget,
    DefaultLifecycleObserver {

    lateinit var listener:OnLoadingStatusChangeListener

    private var isStarted = false

    override val drawable: Drawable? get() = view.drawable

    override fun onStart(placeholder: Drawable?) {
        setDrawable(placeholder)
        if (::listener.isInitialized){
            listener.onStart()
        }
    }

    override fun onError(error: Drawable?) {
        setDrawable(error)
        if (::listener.isInitialized){
            listener.onError()
        }
    }

    override fun onSuccess(result: Drawable) {
        setDrawable(result)
        if (::listener.isInitialized){
            listener.onSuccess()
        }
    }

    override fun onClear() = setDrawable(null)

    override fun onStart(owner: LifecycleOwner) {
        isStarted = true
        updateAnimation()
    }

    override fun onStop(owner: LifecycleOwner) {
        isStarted = false
        updateAnimation()
    }

    /** Replace the [ImageView]'s current drawable with [drawable]. */
    protected open fun setDrawable(drawable: Drawable?) {
        (view.drawable as? Animatable)?.stop()
        view.setImageDrawable(drawable)
        updateAnimation()
    }

    /** Start/stop the current [Drawable]'s animation based on the current lifecycle state. */
    protected open fun updateAnimation() {
        val animatable = view.drawable as? Animatable ?: return
        if (isStarted) animatable.start() else animatable.stop()
    }

    override fun equals(other: Any?): Boolean {
        return (this === other) || (other is ImageViewTarget && view == other.view)
    }

    override fun hashCode() = view.hashCode()

    override fun toString() = "NetworkImageViewTarget(view=$view)"


    interface OnLoadingStatusChangeListener {
        fun onStart()
        fun onError()
        fun onSuccess()
    }
}