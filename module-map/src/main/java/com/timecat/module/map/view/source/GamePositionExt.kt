package com.timecat.module.map.view.panel

import android.graphics.Point
import androidx.annotation.FloatRange
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.TileSystem
import org.osmdroid.util.TileSystemWebMercator
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
    val mapSize = TileSystem.getTileSize(9.0)
    val px = (x * mapSize).roundToInt()
    val py = (y * mapSize).roundToInt()
    MapView.getTileSystem().PixelXYToLatLong(px, py, 9.0, initGeoPoint)
    return initGeoPoint
}

fun gameBoundBox(): BoundingBox {
    val p = Point()
    MapView.getTileSystem().TileXYToPixelXY(4, 4, p)
    val geoPoint = GeoPoint(0.0, 0.0)
    MapView.getTileSystem().PixelXYToLatLong(p.x, p.y, 6.0, geoPoint)
    return BoundingBox(
        TileSystemWebMercator.MaxLatitude,
        geoPoint.longitude,
        geoPoint.latitude,
        TileSystemWebMercator.MinLongitude
    )
}

fun worldBoundBox(): BoundingBox {
    return BoundingBox(
        TileSystemWebMercator.MaxLatitude,
        TileSystemWebMercator.MaxLongitude,
        TileSystemWebMercator.MinLatitude,
        TileSystemWebMercator.MinLongitude
    )
}

class GameMap(val box: BoundingBox) {
    fun pos(
        @FloatRange(from = 0.0, to = 1.0) x: Double,
        @FloatRange(from = 0.0, to = 1.0) y: Double
    ): GeoPoint {
        return box.getGeoPointOfRelativePositionWithLinearInterpolation(x.toFloat(), y.toFloat())
    }
}
