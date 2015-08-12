package com.bookncart.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.bookncart.app.R;
import com.bookncart.app.objects.WishlistObject;

public class WishlistRecyclerAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	ArrayList<WishlistObject> mData;
	Context context;
	int imageHeightGrid;

	public WishlistRecyclerAdapter(ArrayList<WishlistObject> mData,
			Context context) {
		super();
		this.mData = mData;
		this.context = context;
		imageHeightGrid = (int) (context.getResources().getDisplayMetrics().widthPixels / 1.8f);
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder commonHolder, int pos) {
		WishlistItemHolder holder = (WishlistItemHolder) commonHolder;
		LinearLayout.LayoutParams params = (LayoutParams) holder.bookImage
				.getLayoutParams();
		params.height = imageHeightGrid;
		holder.bookImage.setLayoutParams(params);

		holder.bookName.setText(mData.get(pos).getName());
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.shop_activity_recycler_view_list_item_layout_gridlike,
				parent, false);
		WishlistItemHolder holder = new WishlistItemHolder(view);
		return holder;
	}

	class WishlistItemHolder extends RecyclerView.ViewHolder {

		ImageView bookImage;
		TextView bookName;

		public WishlistItemHolder(View v) {
			super(v);
			bookImage = (ImageView) v.findViewById(R.id.shop_layout_image_grid);
			bookName = (TextView) v.findViewById(R.id.book_name_shop_grid);
		}
	}
}