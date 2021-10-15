package com.timecat.module.map.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.timecat.component.commonsdk.extension.isVisible
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.map.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/3
 * @description null
 * @usage null
 */
class PanelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    //初始化动画
    val bottomBarInAnimation by lazy { AnimationUtils.loadAnimation(context, R.anim.seat_selection_bottom_bar_in) }
    val bottomBarOutAnimation by lazy { AnimationUtils.loadAnimation(context, R.anim.seat_selection_bottom_bar_out) }

    var headerView: PanelHeaderView

    init {
        setBackgroundResource(R.color.trans)
        gravity = Gravity.CENTER_HORIZONTAL
        orientation = VERTICAL


        headerView = PanelHeaderView(context).apply {
            layout_width = match_parent
            layout_height = 48
            onCloseClick = {
                hide()
            }
        }.also {
            addView(it)
        }
    }

    fun show(panelBuilder: PanelView.() -> Unit) {
        if (!isVisible()) {
            //显示底边栏
            apply(panelBuilder)
            startAnimation(bottomBarInAnimation)
            setVisibility(View.VISIBLE)
        }
    }

    fun hide() {
        if (isVisible()) {
            //隐藏底边栏
            startAnimation(bottomBarOutAnimation)
            setVisibility(View.GONE)
        }
    }
}

class PanelHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    var iconIv: ImageView
    var titleTv: TextView
    var closeIv: ImageView

    init {
        setBackgroundResource(R.color.trans)
        gravity = Gravity.CENTER_HORIZONTAL
        orientation = HORIZONTAL
        iconIv = ImageView {
            layout_width = 24
            layout_height = 24
        }
        titleTv = TextView {
            layout_width = 0
            layout_height = match_parent
            weight = 1.0f
        }
        closeIv = ImageView {
            layout_width = 24
            layout_height = 24
            src = R.drawable.ic_close
            setOnClickListener {
                onCloseClick()
            }
        }
    }

    var icon: String = ""
        set(value) {
            IconLoader.loadIcon(context, iconIv, value)
        }
    var title: String
        get() = titleTv.text.toString()
        set(value) {
            titleTv.text = value
        }
    var onCloseClick: () -> Unit = {}
}