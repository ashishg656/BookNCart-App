package com.bookncart.app.extras;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class RecyclerViewSwipeToRemoveCallback extends ItemTouchHelper.Callback {

	private final RecyclerViewItemTouchHelperAdapter mAdapter;

	public RecyclerViewSwipeToRemoveCallback(
			RecyclerViewItemTouchHelperAdapter adapter) {
		mAdapter = adapter;
	}

	@Override
	public int getMovementFlags(RecyclerView recyclerView,
			RecyclerView.ViewHolder viewHolder) {
		int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
				| ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
		int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
		return makeMovementFlags(dragFlags, swipeFlags);
	}

	@Override
	public boolean onMove(RecyclerView recyclerView,
			RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

		mAdapter.onItemMove(viewHolder.getAdapterPosition(),
				target.getAdapterPosition());

		return true;
	}

	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
		mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
	}

	@Override
	public boolean isLongPressDragEnabled() {
		return true;
	}

	@Override
	public boolean isItemViewSwipeEnabled() {
		return true;
	}
}
