package com.timecat.module.map.view.panel

import android.content.Context
import com.timecat.layout.ui.business.form.Body
import com.timecat.layout.ui.business.form.VerticalContainer
import com.timecat.layout.ui.layout.*
import com.timecat.module.map.mark.GameMarkerData
import com.timecat.module.map.view.Header
import com.timecat.module.map.view.PanelView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/16
 * @description 介绍型标记
 * @usage null
 */
fun PanelView.PanelIntro(context: Context, intro: GameMarkerData) {
    show {
        Header(intro.icon, intro.title)
        container.apply {
            NestedScrollView {
                layout_width = match_parent
                layout_height = 0
                top_toTopOf = parent_id
                bottom_toTopOf = bottomChipId

                VerticalContainer {
                    Body(intro.content)
                }
            }
            BottomChip("确认") {
                hide()
            }
        }
    }
}
