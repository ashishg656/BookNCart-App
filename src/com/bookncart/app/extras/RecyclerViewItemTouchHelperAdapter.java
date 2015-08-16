package com.bookncart.app.extras;

public interface RecyclerViewItemTouchHelperAdapter {

	boolean onItemMove(int fromPosition, int toPosition);

	void onItemDismiss(int position);
}