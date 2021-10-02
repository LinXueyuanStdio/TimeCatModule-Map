package com.same.ui

import android.content.Intent
import android.view.ViewGroup
import com.didichuxing.doraemonkit.DoKit
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.layout.ui.business.form.Next
import com.timecat.middle.setting.BaseSettingActivity

class MainActivity : BaseSettingActivity() {
    override fun title(): String = "测试"
    override fun addSettingItems(container: ViewGroup) {
        DoKit.Builder(application)
            .productId("需要使用平台功能的话，需要到dokit.cn平台申请id")
            .build()
        LogUtil.OPEN_LOG = true
        LogUtil.DEBUG = true
        container.Next("地图") {
            startActivity(Intent(this, MapActivity::class.java))
        }
    }
}