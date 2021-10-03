package com.timecat.module.map.view.zoom

import android.widget.SeekBar
import androidx.annotation.IntRange

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/3
 * @description null
 * @usage null
 */
class SeekBarListener(
    seekBar: SeekBar,
    @IntRange(from = 0, to = 10000)
    val debounce: Int = 0,
    cb: (Int) -> Unit
) : SeekBar.OnSeekBarChangeListener {
    val callbackRunner = Runnable { cb(seekBar.progress) }

    override fun onProgressChanged(
        seekBar: SeekBar,
        progress: Int,
        fromUser: Boolean
    ) {
        if (!fromUser) return
        seekBar.removeCallbacks(callbackRunner)
        if (debounce == 0) {
            callbackRunner.run()
        } else {
            seekBar.postDelayed(callbackRunner, debounce.toLong())
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
}