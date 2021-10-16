package com.timecat.module.map.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.timecat.component.commonsdk.extension.isVisible
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.drawabe.selectableItemBackground
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.map.R
import com.timecat.module.map.view.panel.Dismissable

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
) : LinearLayout(context, attrs, defStyleAttr), Dismissable {
    //初始化动画
    val bottomBarInAnimation by lazy { AnimationUtils.loadAnimation(context, R.anim.seat_selection_bottom_bar_in) }
    val bottomBarOutAnimation by lazy { AnimationUtils.loadAnimation(context, R.anim.seat_selection_bottom_bar_out) }

    var headerView: PanelHeaderView
    var container: ConstraintLayout

    init {
        setBackgroundColor(Attr.getBackgroundColor(context))
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

        container = ConstraintLayout {
            layout_width = match_parent
            layout_height = 0
            weight = 1.0f
        }
    }

    fun show(panelBuilder: PanelView.() -> Unit) {
        if (!isVisible()) {
            //显示底边栏
            container.removeAllViews()
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

    override fun dismiss() = hide()
}

fun PanelView.Header(title: String) {
    Header(IconLoader.randomAvatar(title), title)
}

fun PanelView.Header(icon: String = IconLoader.randomAvatar(), title: String) {
    headerView.icon = icon
    headerView.title = title
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
            layout_width = 48
            layout_height = 48
            padding = 8
            layout_gravity = Gravity.CENTER_VERTICAL

            src = R.drawable.ic_launcher
            background = selectableItemBackground(context)
        }
        titleTv = TextView {
            layout_width = 0
            layout_height = match_parent
            weight = 1.0f

            gravity = Gravity.CENTER_VERTICAL
            setTextColor(Attr.getPrimaryTextColor(context))
            setOnClickListener {
                onTitleClick()
            }
        }
        closeIv = ImageView {
            layout_width = 48
            layout_height = 48
            padding = 8
            layout_gravity = Gravity.CENTER_VERTICAL

            src = R.drawable.ic_close
            background = selectableItemBackground(context)
            setOnClickListener {
                onCloseClick()
            }
        }
    }

    var icon: String = "R.drawable.ic_launcher"
        set(value) {
            IconLoader.loadIcon(context, iconIv, value)
        }
    var title: String
        get() = titleTv.text.toString()
        set(value) {
            titleTv.text = value
        }
    var onCloseClick: () -> Unit = {}
    var onTitleClick: () -> Unit = {}
}