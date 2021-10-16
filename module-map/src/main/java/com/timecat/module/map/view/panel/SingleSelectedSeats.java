package com.timecat.module.map.view.panel;

import org.osmdroid.views.MapView;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;

/**
 * 选中
 */
public class SingleSelectedSeats {

    @Nullable
    private Seat seats;

    private final WeakReference<MapView> seatSelectionView;

    public SingleSelectedSeats(MapView seatSelectionView) {
        if (seatSelectionView == null) {
            throw new RuntimeException("[SelectedSeats]seatSelectionView is null");
        }

        this.seatSelectionView = new WeakReference<>(seatSelectionView);
        this.seats = null;
    }

    /**
     * 用这个方法拦截SeatSelectionListener.onSeatSelect方法
     */
    public boolean onSelect(@Nullable Seat seat) {
        if (seat == null) {
            return true;
        }
        return false;
    }

    /**
     * 用这个方法拦截SeatSelectionListener.onSeatDeselect方法
     */
    public boolean onDeselect(@Nullable Seat seat) {
        if (seat == seats) {
            this.seats = null;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 使得某个座位被选中, 改变座位状态, 刷新SeatSelectionView显示, 不从选中座位中移除
     */
    public void selectSeat(Seat seat) {
        if (seat == null || seat.getState() == SeatState.UNAVAILABLE) {
            return;
        }
        //获得主座位
        Seat host = null;
        if (seat.getType() == SeatType.MULTI_SEAT_PLACEHOLDER) {
            host = seat.getHost();
        }
        //默认为本身
        if (host == null) {
            host = seat;
        }
        host.setState(SeatState.SELECTED);
        //刷新seatSelectionView显示
        MapView view = seatSelectionView.get();
        if (view != null) {
            view.postInvalidate();
        }
    }

    /**
     * 使得某个座位取消选中, 改变座位状态, 刷新SeatSelectionView显示, 不从选中座位中移除
     */
    public void deselectSeat(Seat seat) {
        if (seat == null || seat.getState() == SeatState.UNAVAILABLE) {
            return;
        }
        //获得主座位
        Seat host = null;
        if (seat.getType() == SeatType.MULTI_SEAT_PLACEHOLDER) {
            host = seat.getHost();
        }
        //默认为本身
        if (host == null) {
            host = seat;
        }
        host.setState(SeatState.AVAILABLE);
        //刷新seatSelectionView显示
        MapView view = seatSelectionView.get();
        if (view != null) {
            //            view.refreshOutlineMap();//TODO 刷新 地标
            view.postInvalidate();
        }
    }

}
