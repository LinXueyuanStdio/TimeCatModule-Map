package com.timecat.module.map.view.panel

import android.view.View.TEXT_ALIGNMENT_CENTER
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.*
import com.timecat.module.map.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/16
 * @description null
 * @usage null
 */
const val bottomChipId = "bottomChip"
fun ConstraintLayout.BottomChip(name: String, onClick: () -> Unit) {
    Chip(context).apply {
        layout_id = bottomChipId
        layout_width = 0
        layout_height = 48
        margin_start = 8
        margin_end = 8
        margin_top = 4
        margin_bottom = 4
        start_toStartOf = parent_id
        end_toEndOf = parent_id
        bottom_toBottomOf = parent_id

        text = name
        textAlignment = TEXT_ALIGNMENT_CENTER
        gravity = gravity_center
        setChipIconResource(R.drawable.ic_ok_circle_white_24dp)
        setChipIconTintResource(R.color.master_icon_view)
        setTextColor(Attr.getPrimaryTextColor(context))
        setOnClickListener {
            onClick()
        }
    }.also {
        addView(it)
    }
}