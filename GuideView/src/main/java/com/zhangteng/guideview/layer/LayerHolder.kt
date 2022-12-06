package com.zhangteng.guideview.layer

import com.zhangteng.guideview.GuideHelper
import com.zhangteng.guideview.base.BaseClip
import com.zhangteng.guideview.clip.CustomClip
import com.zhangteng.guideview.clip.ViewRectClip
import com.zhangteng.guideview.intro.IntroPanel
import com.zhangteng.guideview.view.GuidView

class LayerHolder private constructor(
    private val mView: GuidView,
    private val guideHelper: GuideHelper,
    tag: String
) : GuidView.OnGuidClickListener {
    private val currentLayer: Layer?
    private var mListener: GuideHelper.OnGuidClickListener? = null

    init {
        mView.setOnInnerOnGuidClickListener(this)
        currentLayer = Layer(tag)
    }

    /**
     * 根据VIEW 所在区域定位裁剪区域位置
     *
     * @param viewRectClip
     * @return
     */
    fun buildViewRectClip(viewRectClip: ViewRectClip): LayerHolder {
        buildDstTarget(viewRectClip)
        return this
    }

    /**
     * 自定义裁剪区域位置（全屏定位）
     *
     * @param customClip
     * @return
     */
    fun buildCustomClip(customClip: CustomClip): LayerHolder {
        buildDstTarget(customClip)
        return this
    }

    /**
     * 设置一个引导说明图形
     *
     * @param introPanel
     * @return
     */
    fun buildIntroPanel(introPanel: IntroPanel): LayerHolder {
        currentLayer?.setFacePanel(introPanel)
        return this
    }

    /**
     * 添加点击事件
     *
     * @param listener
     * @return
     */
    fun setOnGuidClickListener(listener: GuideHelper.OnGuidClickListener?): LayerHolder {
        mListener = listener
        return this
    }

    fun show(delayMilliseconds: Long = 0) {
        and()
        guideHelper.show(delayMilliseconds)
    }

    private fun <T : BaseClip> buildDstTarget(clipPosition: T) {
        currentLayer?.setClipTarget(clipPosition)
    }

    fun and(): GuideHelper {
        mView.addLayer(currentLayer)
        return guideHelper
    }

    override fun onDestroyed() {
        mListener?.onDestroyed()
    }

    override fun onEmptyClicked(): Boolean {
        return mListener?.onEmptyClicked(guideHelper) == true
    }

    override fun onEmptyLongClicked(): Boolean {
        return mListener?.onEmptyLongClicked(guideHelper) == true
    }

    override fun onClipClicked(tag: String): Boolean {
        return mListener?.onClipClicked(guideHelper, mView, tag) == true
    }

    override fun onClipLongClicked(tag: String): Boolean {
        return mListener?.onClipLongClicked(guideHelper, mView, tag) == true
    }

    override fun onIntroClicked(tag: String): Boolean {
        return mListener?.onIntroClicked(guideHelper, mView, tag) == true
    }

    override fun onIntroLongClicked(tag: String): Boolean {
        return mListener?.onIntroLongClicked(guideHelper, mView, tag) == true
    }

    companion object {
        fun newCreator(guidView: GuidView, guide: GuideHelper, tag: String): LayerHolder {
            return LayerHolder(guidView, guide, tag)
        }
    }
}