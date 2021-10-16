package com.timecat.module.map.mark

import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.map.view.PanelView
import com.timecat.module.map.view.panel.GameMap
import com.timecat.module.map.view.panel.PanelIntro
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ClickableIconOverlay
import kotlin.random.Random

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/16
 * @description null
 * @usage null
 */
data class GameMarkerData(
    var icon: String,
    var title: String,
    var content: String,
    var x: Double = 0.45 + Random.nextFloat() / 10,
    var y: Double = 0.45 + Random.nextFloat() / 10,
)

data class GameMarker(
    val data: GameMarkerData,
    val panelView: PanelView,
    val box: BoundingBox,
    val listener :MarkerListener
) : ClickableIconOverlay<GameMarkerData>(data) {

    init {
        IconLoader.loadIcon(panelView.context, {
            set(GameMap(box).pos(data.x, data.y), it)
        }, data.icon)
    }

    override fun onMarkerClicked(mapView: MapView, markerId: Int, makerPosition: IGeoPoint, markerData: GameMarkerData): Boolean {
        return listener.onMarkerClicked(mapView, markerId, makerPosition, this)
    }
}

interface MarkerListener {
    fun onMarkerClicked(mapView: MapView, markerId: Int, makerPosition: IGeoPoint, markerData: GameMarker): Boolean
}
