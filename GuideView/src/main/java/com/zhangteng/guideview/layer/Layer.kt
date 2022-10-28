package com.zhangteng.guideview.layer

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import androidx.fragment.app.Fragment
import com.zhangteng.guideview.base.BaseClip
import com.zhangteng.guideview.clip.ClipHolder
import com.zhangteng.guideview.intro.IntroHolder
import com.zhangteng.guideview.intro.IntroPanel

class Layer(var tag: String) {
    private val clipHolder: ClipHolder = ClipHolder()
    private val face: IntroHolder = IntroHolder()

    fun setClipTarget(target: BaseClip?) {
        clipHolder.target = target
    }

    fun setFacePanel(facePanel: IntroPanel?) {
        face.facePanel = facePanel
    }

    fun draw(
        canvas: Canvas,
        paint: Paint?,
        clip: Boolean,
        parentWidth: Float,
        parentHeight: Float
    ) {
        if (clip) {
            clipHolder.draw(canvas, paint, null, parentWidth, parentHeight)
        } else {
            face.draw(canvas, paint, clipHolder.rectF, parentWidth, parentHeight)
        }
    }

    fun build(activity: Activity?) {
        clipHolder.build(activity)
        face.build(activity)
    }

    fun build(fragment: Fragment?) {
        clipHolder.build(fragment)
        face.build(fragment)
    }

    val isClipEventPassThrough: Boolean
        get() = clipHolder.isEventPassThrough

    fun isTouchInClip(x: Float, y: Float): Boolean {
        if (clipHolder.rectF != null) {
            if (clipHolder.rectF!!.contains(x, y)) {
                return true
            }
        }
        return false
    }

    fun isTouchInIntro(x: Float, y: Float): Boolean {
        if (face.rectF != null) {
            if (face.rectF!!.contains(x, y)) {
                return true
            }
        }
        return false
    }
}