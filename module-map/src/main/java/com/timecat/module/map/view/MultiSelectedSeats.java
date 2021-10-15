package com.timecat.module.map.view;

import org.osmdroid.views.MapView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 选中
 */
public class MultiSelectedSeats {

    private int maxSeatNum;

    private final List<Seat> seats;

    private final WeakReference<MapView> seatSelectionView;

    public MultiSelectedSeats(MapView seatSelectionView, int maxSeatNum) {
        if (seatSelectionView == null){
            throw new RuntimeException("[SelectedSeats]seatSelectionView is null");
        }
        if (maxSeatNum < 0){
            throw new RuntimeException("[SelectedSeats]maxSeatNum must >= 0");
        }

        this.seatSelectionView = new WeakReference<>(seatSelectionView);
        this.maxSeatNum = maxSeatNum;
        this.seats = new ArrayList<>(maxSeatNum);
    }

    /**
     * 用这个方法拦截SeatSelectionListener.onSeatSelect方法
     */
    public boolean onSelect(Seat seat){
        if (seat == null){
            return true;
        }
        return false;
    }

    /**
     * 用这个方法拦截SeatSelectionListener.onSeatDeselect方法
     */
    public boolean onDeselect(Seat seat){
        return removeSeat(seat);
    }

    /**
     * 从选中座位中移除座位(不改变座位选中状态, 也不刷新SeatSelectionView, 需要改变状态和刷新显示, 请使用
     * selectSeat方法)
     */
    public boolean removeSeat(Seat seat){
        if (seat == null){
            return true;
        }
        Seat host = null;
        //占位类型获取主座位
        if (seat.getType() == SeatType.MULTI_SEAT_PLACEHOLDER){
            host = seat.getHost();
        }
        //默认为本身
        if (host == null){
            host = seat;
        }
        //移除主座位
        remove(host);
        //占位类型座位
        List<Seat> placeHolders = host.getPlaceHolders();
        if (placeHolders == null){
            return true;
        }
        //移除所有占位类型座位
        for (Seat placeHolder : placeHolders){
            remove(placeHolder);
        }
        return true;
    }

    private boolean remove(Seat seat){
        if (seat == null){
            return false;
        }
        boolean removed = false;
        for (int i = 0 ; i < seats.size() ; i++){
            if (seats.get(i) == seat){
                seats.remove(i);
                i--;
                removed = true;
            }
        }
        return removed;
    }

    public Seat getSeat(int index){
        if (index < 0 || index >= seats.size()){
            return null;
        }
        return seats.get(index);
    }

    public int getSeatNum(){
        return seats.size();
    }

    public int getMaxSeatNum(){
        return maxSeatNum;
    }

    /**
     * 使得某个座位被选中, 改变座位状态, 刷新SeatSelectionView显示, 不从选中座位中移除
     */
    public void selectSeat(Seat seat){
        if (seat == null || seat.getState() == SeatState.UNAVAILABLE){
            return;
        }
        //获得主座位
        Seat host = null;
        if (seat.getType() == SeatType.MULTI_SEAT_PLACEHOLDER){
            host = seat.getHost();
        }
        //默认为本身
        if (host == null){
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
    public void deselectSeat(Seat seat){
        if (seat == null || seat.getState() == SeatState.UNAVAILABLE){
            return;
        }
        //获得主座位
        Seat host = null;
        if (seat.getType() == SeatType.MULTI_SEAT_PLACEHOLDER){
            host = seat.getHost();
        }
        //默认为本身
        if (host == null){
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
