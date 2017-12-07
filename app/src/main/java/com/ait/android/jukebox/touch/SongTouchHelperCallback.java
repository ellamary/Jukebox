package com.ait.android.jukebox.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import static android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags;

/**
 * Created by Mari Kamiya on 12/7/2017.
 */

public class SongTouchHelperCallback extends ItemTouchHelper.Callback {

    private SongTouchHelperAdapter songTouchHelperAdapter;
    public SongTouchHelperCallback(SongTouchHelperAdapter songTouchHelperAdapter) {
        this.songTouchHelperAdapter = songTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        songTouchHelperAdapter.onItemMove(
                viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        songTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
