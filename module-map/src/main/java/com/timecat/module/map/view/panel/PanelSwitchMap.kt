package com.timecat.module.map.view.panel

import android.content.Context
import com.timecat.module.map.view.PanelView
import com.timecat.module.map.view.source.MapTileSource
import com.timecat.module.map.view.source.TileSourceManager
import kotlin.random.Random

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/16
 * @description null
 * @usage null
 */
fun PanelView.PanelSwitchMap(context: Context, tileSourceManager: TileSourceManager, onSwitchMap: (source: MapTileSource) -> Unit) {
    val allSources = tileSourceManager.getAllSources()
    show {
        headerView.title = "地图"
        container.apply {
            bottomChip("传送") {
                goTo(0.45 + Random.nextFloat() / 10, 0.45 + Random.nextFloat() / 10)
                hide()
            }
        }
    }
}