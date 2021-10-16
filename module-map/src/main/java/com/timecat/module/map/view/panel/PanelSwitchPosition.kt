package com.timecat.module.map.view.panel

import android.content.Context
import com.timecat.layout.ui.business.form.Body
import com.timecat.layout.ui.business.form.VerticalContainer
import com.timecat.layout.ui.layout.*
import com.timecat.module.map.view.Header
import com.timecat.module.map.view.PanelView
import kotlin.random.Random

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/16
 * @description null
 * @usage null
 */
fun PanelView.PanelSwitchPosition(context: Context, data: PanelPosition, goTo: (x: Double, y: Double) -> Unit) {
    show {
        Header(data.title)
        container.apply {
            NestedScrollView {
                layout_width = match_parent
                layout_height = 0
                top_toTopOf = parent_id
                bottom_toTopOf = bottomChipId

                VerticalContainer {
                    Body(data.content)
                }
            }
            BottomChip("传送") {
                goTo(data.x, data.y)
                hide()
            }
        }
    }
}

data class PanelPosition(
    var title: String,
    var content: String,
    var x: Double = 0.45 + Random.nextFloat() / 10,
    var y: Double = 0.45 + Random.nextFloat() / 10,
)
