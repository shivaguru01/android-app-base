package com.app.core.common

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

object AnimationUtils {
    fun getRotateAnimation(): Animation {
        val rotate = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.interpolator = LinearInterpolator()
        rotate.duration = 700
        rotate.repeatCount = Animation.INFINITE
        return rotate
    }
}