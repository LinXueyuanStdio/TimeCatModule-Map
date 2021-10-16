package com.timecat.module.map.view.panel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/3
 * @description null
 * @usage null
 */
interface Dismissable {
    fun dismiss()
}

fun Dismissable.lifecycleOwner(owner: LifecycleOwner) {
    val observer = DialogLifecycleObserver(::dismiss)
    owner.lifecycle.addObserver(observer)
}

internal class DialogLifecycleObserver(private val dismiss: () -> Unit) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = dismiss()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() = dismiss()
}
