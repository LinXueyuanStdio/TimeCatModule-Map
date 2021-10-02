package com.timecat.module.map.view

import android.location.Location
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/1
 * @description null
 * @usage null
 */
class UserLocationProvider(

) : IMyLocationProvider{
    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
        return true
    }

    override fun stopLocationProvider() {
    }

    override fun getLastKnownLocation(): Location {
        return Location("")
    }

    override fun destroy() {
    }
}