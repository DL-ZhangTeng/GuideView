package com.zhangteng.guideview.clip

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.fragment.app.Fragment
import com.zhangteng.guideview.base.BaseClip
import com.zhangteng.guideview.base.LayerBaseHold

internal open class ClipHolder : LayerBaseHold() {
    var target: BaseClip? = null
    val rectF: RectF?
        get() = target?.rectF
    val isEventPassThrough: Boolean
        get() = target?.isEventPassThrough == true

    override fun build(activity: Activity?) {
        if (target != null) {
            target!!.build(activity)
        }
    }

    override fun build(fragment: Fragment?) {
        if (target != null) {
            target!!.build(fragment)
        }
    }

    override fun draw(
        canvas: Canvas,
        paint: Paint?,
        clipRectF: RectF?,
        parentWidth: Float,
        parentHeight: Float
    ) {
        if (target != null) {
            target!!.draw(canvas, paint, parentWidth, parentHeight)
        }
    }
}