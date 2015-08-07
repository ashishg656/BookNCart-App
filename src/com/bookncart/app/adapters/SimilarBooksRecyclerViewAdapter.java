package com.bookncart.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.objects.HomeTopRatedBookObject;
import com.bookncart.app.widgets.RoundedImageView;

public class SimilarBooksRecyclerViewAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	ArrayList<HomeTopRatedBookObject> mData;
	Context context;
	int imageHeightGrid;

	public SimilarBooksRecyclerViewAdapter(
			ArrayList<HomeTopRatedBookObject> mData, Context context) {
		super();
		this.mData = mData;
		this.context = context;
		imageHeightGrid = (int) (context.getResources().getDisplayMetrics().widthPixels / 2.5f);
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCommon, int pos) {
		ShopItemHolder holder = (ShopItemHolder) holderCommon;

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
		ShopItemHolder holder = new ShopItemHolder(view);
		return holder;
	}

	class ShopItemHolder extends RecyclerView.ViewHolder {

		RoundedImageView bookImage;
		TextView bookName;

		public ShopItemHolder(View v) {
			super(v);
			bookImage = (RoundedImageView) v
					.findViewById(R.id.shop_layout_image_grid);
			bookName = (TextView) v.findViewById(R.id.book_name_shop_grid);
		}
	}
}
