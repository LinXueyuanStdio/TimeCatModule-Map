package com.same.ui

import androidx.fragment.app.Fragment
import com.timecat.component.router.app.NAV
import com.timecat.page.base.friend.compact.BaseFragmentActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/1
 * @description null
 * @usage null
 */
class MapActivity : BaseFragmentActivity() {
    override fun createFragment(): Fragment {
        return NAV.fragment("map/MapFragment")
    }
}