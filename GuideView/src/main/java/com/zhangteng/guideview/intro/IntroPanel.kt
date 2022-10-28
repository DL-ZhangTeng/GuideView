package com.zhangteng.guideview.intro

import android.content.Context
import android.graphics.*
import androidx.annotation.DrawableRes
import com.zhangteng.guideview.GuideHelper.AlignX
import com.zhangteng.guideview.GuideHelper.AlignY
import com.zhangteng.guideview.base.BaseIntro
import kotlin.math.abs

class IntroPanel constructor(private val context: Context) : BaseIntro() {
    private var with = 0
    private var height = 0
    private var introBitmap: Bitmap? = null
    private var alignX = AlignX.ALIGN_RIGHT
    private var alignY = AlignY.ALIGN_BOTTOM

    /**
     * 设置偏移
     *
     * @param offsetX 水平偏移值px
     * @param offsetY 垂直偏移值px
     * @return
     */
    fun setOffset(offsetX: Float, offsetY: Float): IntroPanel {
        this.offsetX = offsetX
        this.offsetY = offsetY
        return this
    }

    /**
     * 引导图片大小
     *
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    fun setSize(width: Int, height: Int): IntroPanel {
        with = width
        this.height = height
        return this
    }

    /**
     * 偏向
     *
     * @param alignX ALIGN_LEFT(0), ALIGN_RIGHT(1)
     * @param alignY  ALIGN_TOP(0), ALIGN_BOTTOM(1)
     * @return
     */
    fun setAlign(alignX: AlignX, alignY: AlignY): IntroPanel {
        this.alignX = alignX
        this.alignY = alignY
        return this
    }

    /**
     * 引导图片
     *
     * @param bitmap 介绍图片
     * @return
     */
    fun setIntroBmp(bitmap: Bitmap?): IntroPanel {
        introBitmap = bitmap
        return this
    }

    /**
     * 引导图片
     *
     * @param bitmapRes 介绍图片资源id
     * @return
     */
    fun setIntroBmp(@DrawableRes bitmapRes: Int): IntroPanel {
        introBitmap = BitmapFactory.decodeResource(context.resources, bitmapRes)
        return this
    }

    override fun draw(
        canvas: Canvas,
        paint: Paint?,
        clipRectF: RectF?
    ) {
        if (clipRectF == null) {
            return
        }
        if (introBitmap == null) {
            return
        }
        if (rectF == null) {
            initRectF(clipRectF)
        }
        initBitmap()
        canvas.drawBitmap(introBitmap!!, rectF!!.left, rectF!!.top, paint)
    }

    private fun initRectF(clipRectF: RectF) {
        val left: Float = if (alignX == AlignX.ALIGN_LEFT) {
            clipRectF.left - with - offsetX
        } else {
            clipRectF.right + offsetX
        }
        val top: Float = if (alignY == AlignY.ALIGN_TOP) {
            clipRectF.top - height - offsetY
        } else {
            clipRectF.bottom + offsetY
        }
        rectF = RectF(left, top, left + with, top + height)
    }

    private fun initBitmap() {
        if (introBitmap != null) {
            if (abs(introBitmap!!.width - with) > 1 || abs(introBitmap!!.height - height) > 1) {
                introBitmap = Bitmap.createScaledBitmap(introBitmap!!, with, height, true)
            }
        }
    }
}