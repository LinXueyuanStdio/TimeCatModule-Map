package com.timecat.module.map.view.panel

import org.osmdroid.util.GeoPoint
import org.osmdroid.util.TileSystem
import org.osmdroid.views.MapView
import kotlin.math.roundToInt

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/15
 * @description null
 * @usage null
 */
fun pos(x: Double, y: Double): GeoPoint {
    val initGeoPoint = GeoPoint(0.0, 0.0)
    val mapSize = TileSystem.MapSize(11.0)
    val px = (x * mapSize).roundToInt()
    val py = (y * mapSize).roundToInt()
    MapView.getTileSystem().PixelXYToLatLong(px, py, 9.0, initGeoPoint)
    return initGeoPoint
}