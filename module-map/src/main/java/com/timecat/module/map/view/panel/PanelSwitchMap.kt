package com.timecat.module.map.view.panel

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.adapter.SubTypeCard
import com.timecat.middle.block.ext.bindAdapter
import com.timecat.module.map.view.PanelView
import com.timecat.module.map.view.source.MapTileSource
import com.timecat.module.map.view.source.TileSourceManager

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
            RecyclerView {
                layout_width = match_parent
                layout_height = match_parent

                val callback = object : SubTypeCard.Listener {
                    override fun loadFor(subItem: SubItem) {
                        val source = allSources.find { it.uuid == subItem.uuid } ?: return
                        onSwitchMap(source)
                    }

                    override fun more(subItem: SubItem) {
                        val source = allSources.find { it.uuid == subItem.uuid } ?: return
                        onSwitchMap(source)
                    }
                }
                val sources = allSources.map {
                    val item = SubItem(
                        0, 0,
                        it.tileSource.name(),
                        it.tileSource.copyrightNotice,
                        IconLoader.randomAvatar(it.uuid),
                        "", "", it.uuid
                    )
                    SubTypeCard(item, context, callback)
                }.toMutableList()
                val mAdapter = BaseAdapter(null)
                mAdapter.reload(sources)
                bindAdapter(mAdapter, this) {
                    GridLayoutManager(context, 2)
                }
            }
        }
    }
}