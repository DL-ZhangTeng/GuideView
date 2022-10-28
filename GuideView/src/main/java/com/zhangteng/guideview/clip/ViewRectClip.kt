package com.zhangteng.guideview.clip

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.zhangteng.guideview.base.BaseClip
import kotlin.math.roundToInt

class ViewRectClip : BaseClip() {
    private var padding = 0f

    @IdRes
    private var clipViewId = 0
    private var clipDestView: View? = null
    private var parentWidth = 0f
    private var parentHeight = 0f

    /**
     * 剪裁区域View
     *
     * @param view 被介绍的view
     * @return
     */
    fun setDstView(view: View?): ViewRectClip {
        clipDestView = view
        return this
    }

    /**
     * 剪裁区域View
     *
     * @param viewId 被介绍的view的资源ID
     * @return
     */
    fun setDstView(@IdRes viewId: Int): ViewRectClip {
        clipViewId = viewId
        return this
    }

    /**
     * 剪裁区域内边距
     *
     * @param padding 内边距px
     * @return
     */
    fun setPadding(padding: Float): ViewRectClip {
        this.padding = padding
        return this
    }

    /**
     * 水平定位偏移
     *
     * @param offsetX 偏移值px
     * @return
     */
    fun setOffsetX(offsetX: Float): ViewRectClip {
        this.offsetX = offsetX
        return this
    }

    /**
     * 垂直定位偏移
     *
     * @param offsetY 偏移值px
     * @return
     */
    fun setOffsetY(offsetY: Float): ViewRectClip {
        this.offsetY = offsetY
        return this
    }

    /**
     * 裁剪图形圆角
     *
     * @param radius 圆角半价
     * @return
     */
    fun clipRadius(radius: Float): ViewRectClip {
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
    fun asIrregularShape(context: Context, @DrawableRes bitmapId: Int): ViewRectClip {
        irregularClip = BitmapFactory.decodeResource(context.resources, bitmapId)
        return this
    }

    /**
     * 设置不规则裁剪图形
     *
     * @param bitmap 图片资源
     * @return
     */
    fun asIrregularShape(bitmap: Bitmap): ViewRectClip {
        irregularClip = bitmap
        return this
    }

    /**
     * 镂空区域事件穿透
     *
     * @param eventPassThrough 是否事件穿透
     */
    fun setEventPassThrough(eventPassThrough: Boolean): ViewRectClip {
        this.isEventPassThrough = eventPassThrough
        return this
    }

    override fun build(activity: Activity?) {
        if (clipDestView == null && clipViewId != 0) {
            clipDestView = activity!!.findViewById(clipViewId)
        }
    }

    override fun build(fragment: Fragment?) {
        if (clipDestView == null && clipViewId != 0 && fragment!!.view != null) {
            clipDestView = fragment.requireView().findViewById(clipViewId)
        }
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
            if (clipDestView != null) {
                val pos = IntArray(2)
                clipDestView!!.getLocationInWindow(pos)
                val left = pos[0] + offsetX - padding
                val top = pos[1] + offsetY - padding
                val width = clipDestView!!.measuredWidth + padding * 2
                val height = clipDestView!!.height + padding * 2
                rectF = RectF(left, top, left + width, top + height)
                buildDstSizeBitmap()
            }
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
            val bitmap = Bitmap.createScaledBitmap(
                irregularClip!!, rectF!!.width().roundToInt(), rectF!!.height().roundToInt(), true
            )
            if (bitmap != null && !bitmap.isRecycled) {
                irregularClip = bitmap
            }
        }
    }
}