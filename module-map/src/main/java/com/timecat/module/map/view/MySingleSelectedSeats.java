package com.timecat.module.map.view;

import android.content.Context;

import org.osmdroid.views.MapView;

import androidx.annotation.Nullable;

/**
 * 选中座位控制器强化, 增加对底部栏的刷新
 */
public class MySingleSelectedSeats extends SingleSelectedSeats {

    private final PanelView bottomBar;

    public MySingleSelectedSeats(Context context, MapView seatSelectionView, PanelView bottomBar) {
        super(seatSelectionView);

        this.bottomBar = bottomBar;
    }

    @Override
    public boolean onSelect(@Nullable Seat seat) {
        boolean result = super.onSelect(seat);
        //刷新数据
        if (result) {
            show(seat);
        }
        return result;
    }

    @Override
    public boolean onDeselect(@Nullable  Seat seat) {
        boolean result = super.onDeselect(seat);
        //刷新数据
        hide();
        return result;
    }

    public void show(Seat seat) {
//        bottomBar.show(seat);
    }

    public void hide() {
        bottomBar.hide();
    }

}
