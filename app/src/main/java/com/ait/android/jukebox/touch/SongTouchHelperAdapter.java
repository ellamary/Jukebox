package com.ait.android.jukebox.touch;

/**
 * Created by Mari Kamiya on 12/7/2017.
 */

public interface SongTouchHelperAdapter {

    void onItemDismiss(int position);

    void onItemMove(int fromPosition, int toPosition);

}
