package com.zhangteng.guideview

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.zhangteng.guideview.base.BaseClip
import com.zhangteng.guideview.layer.LayerHolder
import com.zhangteng.guideview.view.GuidView

class GuideHelper private constructor(context: Context) {
    private val mView by lazy { GuidView(context) }
    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null

    /**
     * layer层基础颜色
     */
    fun initBaseColor(color: Int): GuideHelper {
        mView.setBackgroundColor(color)
        return this
    }

    /**
     * 新建一个引导
     *
     * @return
     */
    fun newLayer(): LayerHolder {
        return LayerHolder.newCreator(mView, this, createTag())
    }

    /**
     * 新建一个引导
     *
     * @return
     */
    fun newLayer(tag: String): LayerHolder {
        return if (tag.isEmpty()) {
            LayerHolder.newCreator(mView, this, createTag())
        } else {
            LayerHolder.newCreator(mView, this, tag)
        }
    }

    fun show(delayMilliseconds: Long = 0) {
        if (mActivity != null) {
            mView.build(mActivity, delayMilliseconds)
        } else if (mFragment != null) {
            mView.build(mFragment, delayMilliseconds)
        }
    }

    fun clearLayers() {
        mView.clearLayers()
    }

    fun removeLayerByTag(tag: String) {
        mView.removeLayerByTag(tag)
    }

    fun dismiss() {
        mView.dismiss()
    }

    private fun createTag(): String {
        val builder = StringBuilder()
        builder.append("layer_")
            .append(System.currentTimeMillis())
            .append((Math.random() * 99999).toInt())
            .append("_")
            .append((Math.random() * 99999).toInt())
        return builder.toString()
    }

    interface ClipPositionBuilder<T : BaseClip> {
        fun buildTarget(): T
    }

    /**
     * 引导层点击事件监听器
     */
    abstract class OnGuidClickListener {
        /**
         * 引导层销毁回调
         */
        open fun onDestroyed() {}

        /**
         * 点击蒙层非裁剪和信息区域回调，返回true，直接退出引导，返回false则不退出
         *
         * @return
         */
        abstract fun onEmptyClicked(guide: GuideHelper): Boolean

        /**
         * 长按蒙层非裁剪和信息区域回调，返回true，直接退出引导，返回false则不退出
         *
         * @return
         */
        open fun onEmptyLongClicked(guide: GuideHelper): Boolean {
            return false
        }

        /**
         * 引导镂空区域点击回调，如果镂空区域设置了事件透传，则不回调，返回true，直接退出引导，返回false则不退出
         *
         * @param guide
         * @param tag
         */
        abstract fun onClipClicked(guide: GuideHelper, view: GuidView, tag: String): Boolean

        /**
         * 引导镂空区域长按回调，如果镂空区域设置了事件透传，则不回调，返回true，直接退出引导，返回false则不退出
         *
         * @param guide
         * @param tag
         */
        open fun onClipLongClicked(guide: GuideHelper, view: GuidView, tag: String): Boolean {
            return false
        }

        /**
         * 引导介绍区域点击回调，返回true，直接退出引导，返回false则不退出
         *
         * @param guide
         * @param tag
         */
        abstract fun onIntroClicked(guide: GuideHelper, view: GuidView, tag: String): Boolean

        /**
         * 引导介绍区域长按回调，返回true，直接退出引导，返回false则不退出
         *
         * @param guide
         * @param tag
         */
        open fun onIntroLongClicked(guide: GuideHelper, view: GuidView, tag: String): Boolean {
            return false
        }
    }

    enum class AlignY(var align: Int) {
        ALIGN_TOP(0), ALIGN_BOTTOM(1);
    }

    enum class AlignX(var align: Int) {
        ALIGN_LEFT(0), ALIGN_RIGHT(1);
    }

    companion object {
        fun newGuide(activity: Activity): GuideHelper {
            val guide = GuideHelper(activity)
            guide.mActivity = activity
            return guide
        }

        fun newGuide(fragment: Fragment): GuideHelper {
            val guide = GuideHelper(fragment.requireActivity())
            guide.mFragment = fragment
            return guide
        }
    }
}