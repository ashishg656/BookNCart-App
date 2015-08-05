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
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.activities.ShopActivity;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.objects.HomeTopRatedBookObject;

public class ShopActivityListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	ArrayList<HomeTopRatedBookObject> mData;
	Context context;
	int imageHeightGrid, imageHeightLongList;

	public ShopActivityListAdapter(ArrayList<HomeTopRatedBookObject> mData,
			Context context) {
		super();
		this.mData = mData;
		this.context = context;
		imageHeightGrid = (int) (context.getResources().getDisplayMetrics().widthPixels / 1.8f);
		imageHeightLongList = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.6);
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCommon, int pos) {
		ShopItemHolder holder = (ShopItemHolder) holderCommon;
		if (((ShopActivity) context).currentDisplayType == BNC_SHOP_DISPLAY_TYPE_GRID) {
			LinearLayout.LayoutParams params = (LayoutParams) holder.bookImage
					.getLayoutParams();
			params.height = imageHeightGrid;
			holder.bookImage.setLayoutParams(params);
		} else if (((ShopActivity) context).currentDisplayType == BNC_SHOP_DISPLAY_TYPE_LONG_LIST) {
			LinearLayout.LayoutParams params = (LayoutParams) holder.bookImage
					.getLayoutParams();
			params.height = imageHeightLongList;
			holder.bookImage.setLayoutParams(params);
		}
		holder.bookName.setText(mData.get(pos).getName());
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		int resourceId = 0;
		if (((ShopActivity) context).currentDisplayType == BNC_SHOP_DISPLAY_TYPE_GRID) {
			resourceId = R.layout.shop_activity_recycler_view_list_item_layout_gridlike;
		} else if (((ShopActivity) context).currentDisplayType == BNC_SHOP_DISPLAY_TYPE_LIST) {
			resourceId = R.layout.shop_activity_recycler_view_list_item_layout_listlike;
		} else if (((ShopActivity) context).currentDisplayType == BNC_SHOP_DISPLAY_TYPE_LONG_LIST) {
			resourceId = R.layout.shop_activity_recycler_view_list_item_layout_longlistlike;
		}
		View view = LayoutInflater.from(context).inflate(resourceId, parent,
				false);
		ShopItemHolder holder = new ShopItemHolder(view);
		return holder;
	}

	class ShopItemHolder extends RecyclerView.ViewHolder {

		ImageView bookImage;
		TextView bookName;

		public ShopItemHolder(View v) {
			super(v);
			bookImage = (ImageView) v.findViewById(R.id.shop_layout_image_grid);
			bookName = (TextView) v.findViewById(R.id.book_name_shop_grid);
		}
	}
}
