package com.timecat.module.map.fragment

import android.view.View
import com.timecat.component.setting.DEF
import com.timecat.module.map.R
import com.timecat.page.base.friend.main.BaseMainFragment
import com.xiaojinzi.component.anno.FragmentAnno
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/1
 * @description null
 * @usage null
 */
@FragmentAnno("map/MapFragment")
class MapFragment : BaseMainFragment() {
    override fun layout(): Int = R.layout.map_layout_main
    var mapView: MapView? = null
    override fun bindView(view: View) {
        super.bindView(view)
        mapView = view.findViewById(R.id.mapView)
    }

    override fun lazyInit() {
    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(_mActivity.applicationContext, DEF.block())
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(_mActivity.applicationContext, DEF.block())
        mapView?.onPause()
    }
}