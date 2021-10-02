package com.timecat.module.map.view

import org.osmdroid.util.TileSystem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/2
 * @description null
 * @usage null
 */
class GameTileSystem : TileSystem() {
    override fun getX01FromLongitude(pLongitude: Double): Double {
        return (pLongitude - minLongitude) / (maxLongitude - minLongitude)
    }

    override fun getY01FromLatitude(pLatitude: Double): Double {
        val sinus = Math.sin(pLatitude * Math.PI / 180)
        return 0.5 - Math.log((1 + sinus) / (1 - sinus)) / (4 * Math.PI)
    }

    override fun getLongitudeFromX01(pX01: Double): Double {
        return minLongitude + (maxLongitude - minLongitude) * pX01
    }

    override fun getLatitudeFromY01(pY01: Double): Double {
        return 90 - 360 * Math.atan(Math.exp((pY01 - 0.5) * 2 * Math.PI)) / Math.PI
    }

    override fun getMinLatitude(): Double = MinLatitude

    override fun getMaxLatitude(): Double = MaxLatitude

    override fun getMinLongitude(): Double = MinLongitude

    override fun getMaxLongitude(): Double = MaxLongitude

    companion object {
        const val MinLatitude = 45.05112877980658
        const val MaxLatitude = 85.05112877980658
        const val MinLongitude = -180.0
        const val MaxLongitude = -90.0
    }
}