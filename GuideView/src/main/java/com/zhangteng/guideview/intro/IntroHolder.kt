package com.zhangteng.guideview.intro

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.fragment.app.Fragment
import com.zhangteng.guideview.base.LayerBaseHold

internal open class IntroHolder : LayerBaseHold() {
    var facePanel: IntroPanel? = null
    val rectF: RectF?
        get() = facePanel?.rectF

    override fun build(activity: Activity?) {}
    override fun build(fragment: Fragment?) {}
    override fun draw(
        canvas: Canvas,
        paint: Paint?,
        clipRectF: RectF?,
        parentWidth: Float,
        parentHeight: Float
    ) {
        facePanel?.draw(canvas, paint, clipRectF)
    }
}