package com.same.ui

import android.content.Intent
import android.view.ViewGroup
import com.timecat.layout.ui.business.form.Next
import com.timecat.middle.setting.BaseSettingActivity

class MainActivity : BaseSettingActivity() {
    override fun title(): String = "测试"
    override fun addSettingItems(container: ViewGroup) {
        container.Next("地图") {
            startActivity(Intent(this, MapActivity::class.java))
        }
    }
}