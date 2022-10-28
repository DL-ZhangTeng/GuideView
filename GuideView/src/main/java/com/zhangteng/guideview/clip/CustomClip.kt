package com.zhangteng.guideview.clip

import android.app.Activity
import android.content.Context
import android.graphics.*
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.zhangteng.guideview.GuideHelper.AlignX
import com.zhangteng.guideview.GuideHelper.AlignY
import com.zhangteng.guideview.base.BaseClip

class CustomClip : BaseClip() {
    private var alignX = AlignX.ALIGN_LEFT
    private var alignY = AlignY.ALIGN_TOP
    private var clipWidth = 0
    private var clipHeight = 0
    private var parentWidth = 0f
    private var parentHeight = 0f

    /**
     * 剪裁区域宽高
     *
     * @param width 剪裁区域宽
     * @param height 剪裁区域高
     * @return
     */
    fun setClipSize(width: Int, height: Int): CustomClip {
        clipWidth = width
        clipHeight = height
        return this
    }

    /**
     * 设置定位水平定位偏向
     *
     * @param alignX ALIGN_LEFT(0), ALIGN_RIGHT(1);
     * @return
     */
    fun setAlignX(alignX: AlignX): CustomClip {
        this.alignX = alignX
        return this
    }

    /**
     * 设置定位垂直定位偏向
     *
     * @param alignY  ALIGN_TOP(0), ALIGN_BOTTOM(1);
     * @return
     */
    fun setAlignY(alignY: AlignY): CustomClip {
        this.alignY = alignY
        return this
    }

    /**
     * 根据水平定位偏向设置偏移，如果是ALIGN_LEFT,则是距离屏幕左侧偏移量，如果是ALIGN_RIGHT 则是距离屏幕右侧偏移量
     *
     * @param offsetX 偏移值px
     * @return
     */
    fun setOffsetX(offsetX: Float): CustomClip {
        this.offsetX = offsetX
        return this
    }

    /**
     * 根据垂直定位偏向设置偏移，如果是ALIGN_TOP,则是距离屏幕上侧偏移量，如果是ALIGN_BOTTOM 则是距离屏幕下侧偏移量
     *
     * @param offsetY 偏移值px
     * @return
     */
    fun setOffsetY(offsetY: Float): CustomClip {
        this.offsetY = offsetY
        return this
    }

    /**
     * 裁剪图形圆角
     *
     * @param radius 圆角半价
     * @return
     */
    fun clipRadius(radius: Float): CustomClip {
        this.radius = radius
        return this
    }

    /**
     * 设置不规则裁剪图形
     *
     * @param context 上下文
     * @param bitmapId 图片资源id
     * @return
     */
    fun asIrregularShape(context: Context, @DrawableRes bitmapId: Int): CustomClip {
        irregularClip = BitmapFactory.decodeResource(context.resources, bitmapId)
        return this
    }

    /**
     * 设置不规则裁剪图形
     *
     * @param bitmap 图片资源
     * @return
     */
    fun asIrregularShape(bitmap: Bitmap): CustomClip {
        irregularClip = bitmap
        return this
    }

    /**
     * 镂空区域事件穿透
     *
     * @param eventPassThrough 是否事件穿透
     */
    fun setEventPassThrough(eventPassThrough: Boolean): CustomClip {
        this.isEventPassThrough = eventPassThrough
        return this
    }

    override fun build(activity: Activity?) {
        buildDstSizeBitmap()
    }

    override fun build(fragment: Fragment?) {
        buildDstSizeBitmap()
    }

    override fun draw(canvas: Canvas, paint: Paint?, parentWidth: Float, parentHeight: Float) {
        initRect(parentWidth, parentHeight)
        this.parentHeight = parentHeight
        this.parentWidth = parentWidth
        if (irregularClip == null) {
            drawClipShape(canvas, paint)
        } else {
            drawClipBitmap(canvas, paint)
        }
    }

    private fun initRect(parentWidth: Float, parentHeight: Float) {
        if (rectF != null || parentHeight != this.parentHeight || parentWidth != this.parentWidth) {
            val left: Float = if (alignX == AlignX.ALIGN_RIGHT) {
                parentWidth - offsetX - clipWidth
            } else {
                offsetX
            }
            val top: Float = if (alignY == AlignY.ALIGN_BOTTOM) {
                parentHeight - offsetY - clipHeight
            } else {
                offsetY
            }
            rectF = RectF(left, top, left + clipWidth, top + clipHeight)
        }
    }

    private fun drawClipShape(canvas: Canvas, paint: Paint?) {
        if (rectF == null) {
            return
        }
        paint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRoundRect(rectF!!, radius, radius, paint)
        paint.xfermode = null
    }

    private fun drawClipBitmap(canvas: Canvas, paint: Paint?) {
        if (rectF == null) {
            return
        }
        paint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        canvas.drawBitmap(irregularClip!!, rectF!!.left, rectF!!.top, paint)
        paint.xfermode = null
    }

    private fun buildDstSizeBitmap() {
        if (irregularClip != null) {
            val bitmap = Bitmap.createScaledBitmap(irregularClip!!, clipWidth, clipHeight, true)
            if (bitmap != null && !bitmap.isRecycled) {
                irregularClip = bitmap
            }
        }
    }
}