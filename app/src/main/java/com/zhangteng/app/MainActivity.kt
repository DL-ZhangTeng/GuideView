package com.zhangteng.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.zhangteng.guideview.GuideHelper
import com.zhangteng.guideview.GuideHelper.OnGuidClickListener
import com.zhangteng.guideview.clip.CustomClip
import com.zhangteng.guideview.clip.ViewRectClip
import com.zhangteng.guideview.intro.IntroPanel
import com.zhangteng.guideview.view.GuidView
import com.zhangteng.utils.dp2px
import com.zhangteng.utils.getStatusHeight

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun showAbsPosLayer(view: View?) {
        //构建引导
        GuideHelper.newGuide(this)
            //设置引蒙层背景颜色
            .initBaseColor(-0x80000000)
            //新建一个引导
            .newLayer("guide_1")
            //创建一个镂空区域
            .buildCustomClip(
                CustomClip()
                    //设置定位水平定位偏向
                    .setAlignX(GuideHelper.AlignX.ALIGN_RIGHT)
                    //设置定位垂直定位偏向
                    .setAlignY(GuideHelper.AlignY.ALIGN_TOP)
                    //镂空区域是否事件穿透
                    .setEventPassThrough(true)
                    //根据水平定位偏向设置偏移，如果是ALIGN_LEFT,则是距离屏幕左侧偏移量，如果是ALIGN_RIGHT 则是距离屏幕右侧偏移量
                    .setOffsetX(dp2px(14f).toFloat())
                    .setOffsetY((getStatusHeight() + dp2px(4f)).toFloat())
                    //设置镂空裁剪区域尺寸
                    .setClipSize(dp2px(48f), dp2px(48f))
                    .clipRadius(dp2px(24f).toFloat())
            )
            .buildIntroPanel(
                //设置介绍图片与clipInfo的对齐信息
                IntroPanel(applicationContext)
                    .setIntroBmp(R.mipmap.ic_launcher)
                    .setAlign(GuideHelper.AlignX.ALIGN_LEFT, GuideHelper.AlignY.ALIGN_BOTTOM)
                    .setSize(dp2px(50f), dp2px(50f))
                    .setOffset(dp2px(-20f).toFloat(), 0f)
            )
            .setOnGuidClickListener(object : OnGuidClickListener() {
                override fun onEmptyClicked(guide: GuideHelper): Boolean { //点击蒙层空白区域
                    return true //返回true，引导消失，false不消失
                }

                override fun onClipClicked(guide: GuideHelper, view: GuidView, tag: String) {
                    //由于设置了setEventPassThrough 为true，所以这里这个方法不会回调
                }

                override fun onIntroClicked(guide: GuideHelper, view: GuidView, tag: String) {
                    //点击文字区域
                    Toast.makeText(applicationContext, "点击了介绍区域", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .and()
            .newLayer("guide_2")
            .buildViewRectClip(
                ViewRectClip()
                    .setDstView(R.id.btn)
                    .setEventPassThrough(true)
                    .setPadding(dp2px(5f).toFloat())
                    .clipRadius(dp2px(24f).toFloat())
            )
            .buildIntroPanel(
                IntroPanel(applicationContext)
                    .setIntroBmp(R.mipmap.ic_launcher)
                    .setAlign(GuideHelper.AlignX.ALIGN_RIGHT, GuideHelper.AlignY.ALIGN_BOTTOM)
                    .setSize(dp2px(50f), dp2px(50f))
                    .setOffset(dp2px(-50f).toFloat(), 0f)
            )
            .setOnGuidClickListener(object : OnGuidClickListener() {
                override fun onEmptyClicked(guide: GuideHelper): Boolean { //点击蒙层空白区域
                    return true //返回true，引导消失，false不消失
                }

                override fun onClipClicked(guide: GuideHelper, view: GuidView, tag: String) {
                    //由于设置了setEventPassThrough 为true，所以这里这个方法不会回调
                }

                override fun onIntroClicked(guide: GuideHelper, view: GuidView, tag: String) {
                    //点击文字区域
                    Toast.makeText(applicationContext, "点击了介绍区域", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .show()
    }
}