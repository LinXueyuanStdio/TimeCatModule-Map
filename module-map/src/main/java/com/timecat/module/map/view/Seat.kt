package com.timecat.module.map.view

import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/15
 * @description null
 * @usage null
 */
data class Seat(
    var x: Double,
    var y: Double,
    var type: SeatType,
    var state: SeatState = SeatState.AVAILABLE,
    var host: Seat? = null,
    var placeHolders: List<Seat>? = null,
) : LabelledGeoPoint(pos(x, y)) {
    fun title(): String = label
}

enum class SeatState {
    AVAILABLE,
    UNAVAILABLE,
    SELECTED,
}

enum class SeatType {
    MULTI_SEAT_PLACEHOLDER,

    /**
     * 地图
     */
    MAP,

    /**
     * 传送门
     */
    PORTAL,
}