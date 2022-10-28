package com.zhangteng.guideview.base

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.fragment.app.Fragment

abstract class LayerBaseHold {
    abstract fun build(activity: Activity?)
    abstract fun build(fragment: Fragment?)
    abstract fun draw(
        canvas: Canvas,
        paint: Paint?,
        clipRectF: RectF?,
        parentWidth: Float,
        parentHeight: Float
    )
}