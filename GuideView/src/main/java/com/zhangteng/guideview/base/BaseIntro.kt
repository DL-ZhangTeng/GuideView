package com.zhangteng.guideview.base

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.fragment.app.Fragment

abstract class BaseIntro {
    var offsetX = 0f
        protected set
    var offsetY = 0f
        protected set
    var rectF: RectF? = null
        protected set

    open fun build(activity: Activity?) {}
    open fun build(fragment: Fragment?) {}
    abstract fun draw(canvas: Canvas, paint: Paint?, clipRectF: RectF?)
}