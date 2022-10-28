package com.zhangteng.guideview.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.zhangteng.guideview.layer.Layer
import kotlin.math.abs

class GuidView constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val gvTag: String = "GuidView"
    private var mPaint: Paint? = null
    private var mLayerList: MutableList<Layer>? = null
    private var drawTaskId = 0
    private var mRectF: RectF? = null
    private var backgroundColor = -0x80000000
    private var mMinTouchSlop = 0f
    private var mClickListener: OnGuidClickListener? = null

    private var isTouchIn = false
    private var mPressX = 0f
    private var mPressY = 0f
    private var mLastPressTime: Long = 0

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLayerList = ArrayList()
        mMinTouchSlop = ViewConfiguration.get(context).scaledTouchSlop.toFloat()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val width = right - left
        val height = bottom - top
        if (mRectF == null || mRectF!!.width() < width || mRectF!!.height() < height) {
            mRectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        }
        postInvalidate()
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mRectF == null) {
            return
        }
        drawTaskId = canvas.saveLayer(mRectF, mPaint)
        canvas.drawColor(backgroundColor)
        if (mLayerList != null) {
            for (i in mLayerList!!.indices) {
                mLayerList!![i].draw(canvas, mPaint, true, mRectF!!.width(), mRectF!!.height())
            }
            for (i in mLayerList!!.indices) {
                mLayerList!![i].draw(canvas, mPaint, false, mRectF!!.width(), mRectF!!.height())
            }
        }
        canvas.restoreToCount(drawTaskId)
    }

    fun addLayer(layer: Layer?) {
        if (layer == null) {
            return
        }
        mLayerList!!.add(layer)
        postInvalidate()
    }

    fun build(activity: Activity?) {
        val rootView = activity!!.window.decorView as FrameLayout
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val oldView = rootView.findViewWithTag<View>(gvTag)
        if (oldView != null) {
            rootView.removeView(oldView)
        }
        tag = gvTag
        rootView.addView(this, params)
        if (mLayerList != null) {
            for (i in mLayerList!!.indices) {
                mLayerList!![i].build(activity)
            }
        }
        postInvalidate()
    }

    fun build(fragment: Fragment?) {
        val rootView = fragment!!.requireActivity().window.decorView as FrameLayout
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val oldView = rootView.findViewWithTag<View>(gvTag)
        if (oldView != null) {
            rootView.removeView(oldView)
        }
        tag = gvTag
        rootView.addView(this, params)
        if (mLayerList != null) {
            for (i in mLayerList!!.indices) {
                mLayerList!![i].build(fragment)
            }
        }
        postInvalidate()
    }

    fun clearLayers() {
        if (mLayerList != null) {
            mLayerList!!.clear()
        }
        postInvalidate()
    }

    fun removeLayerByTag(tag: String) {
        try {
            if (mLayerList != null && tag.isNotEmpty()) {
                var i = 0
                while (i < mLayerList!!.size) {
                    if (tag == mLayerList!![i].tag) {
                        mLayerList!!.removeAt(i)
                        i--
                    }
                    i++
                }
            }
        } catch (e: Exception) {
            Log.e("GuideView", e.toString())
        }
        postInvalidate()
    }

    fun dismiss() {
        val parentView = parent as ViewGroup
        parentView.removeView(this)
        if (mClickListener != null) {
            mClickListener!!.onDestroyed()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isTouchIn = false
                mPressX = event.x
                mPressY = event.y
                mLastPressTime = System.currentTimeMillis()
                val result = checkIsLayerIn(mPressX, mPressY)
                isTouchIn = !(result[0] && result[1])
            }
            MotionEvent.ACTION_MOVE -> return true
            MotionEvent.ACTION_UP -> {
                val x = event.x
                val y = event.y
                if (isTouchIn) {
                    if (System.currentTimeMillis() - mLastPressTime < 300) {
                        if (abs(x - mPressX) < mMinTouchSlop && abs(y - mPressY) < mMinTouchSlop) {
                            executeClick(mPressX, mPressY)
                        }
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> isTouchIn = false
            else -> {}
        }
        return if (isTouchIn) true else super.onTouchEvent(event)
    }

    private fun executeClick(x: Float, y: Float) {
        Log.i(
            "Layer",
            "mPressX:"
                    + mPressX
                    + "   mPressY:"
                    + mPressY
                    + "    mLastPressTime:"
                    + mLastPressTime
                    + "    mMinTouchSlop:"
                    + mMinTouchSlop
        )
        if (mClickListener == null) {
            return
        }
        for (i in mLayerList!!.indices) {
            val item = mLayerList!![i]
            if (item.isTouchInClip(x, y)) {
                mClickListener!!.onClipClicked(item.tag)
                return
            } else if (item.isTouchInIntro(x, y)) {
                mClickListener!!.onIntroClicked(item.tag)
                return
            }
        }
        if (isTouchIn && mClickListener != null) {
            val result = mClickListener!!.onEmptyClicked()
            if (result) {
                dismiss()
            }
        }
    }

    private fun checkIsLayerIn(x: Float, y: Float): BooleanArray {
        //0为 标识 区域内，1为标识透传事件
        val result = booleanArrayOf(false, false)
        for (i in mLayerList!!.indices) {
            val item = mLayerList!![i]
            if (item.isTouchInClip(x, y)) {
                result[0] = true
                result[1] = item.isClipEventPassThrough
                break
            }
        }
        return result
    }

    /**
     * 设置点击事件
     *
     * @param listener
     */
    fun setOnInnerOnGuidClickListener(listener: OnGuidClickListener?) {
        mClickListener = listener
    }

    /**
     * 引导层点击事件监听器
     */
    interface OnGuidClickListener {
        /**
         * 引导层销毁回调
         */
        fun onDestroyed()

        /**
         * 点击蒙层非裁剪和信息区域回调，返回true，直接退出引导，返回false则不退出
         *
         * @return
         */
        fun onEmptyClicked(): Boolean

        /**
         * 引导镂空区域点击回调，如果镂空区域设置了事件透传，则不回调
         *
         * @param tag
         */
        fun onClipClicked(tag: String)

        /**
         * 引导介绍区域点击回调
         *
         * @param tag
         */
        fun onIntroClicked(tag: String)
    }
}