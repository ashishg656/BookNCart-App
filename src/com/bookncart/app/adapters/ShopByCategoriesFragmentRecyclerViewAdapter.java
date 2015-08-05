package com.bookncart.app.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.LayoutParams;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookncart.app.R;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.objects.ShopByCategoriesObject;

public class ShopByCategoriesFragmentRecyclerViewAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	ShopByCategoriesObject mData;
	Context context;

	public ShopByCategoriesFragmentRecyclerViewAdapter(
			ShopByCategoriesObject mData, Context context) {
		super();
		this.mData = mData;
		this.context = context;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0)
			return BNC_SHOP_BY_CATEGORIES_TYPE_HEADER;
		else
			return BNC_SHOP_BY_CATEGORIES_TYPE_NORMAL;
	}

	@Override
	public int getItemCount() {
		return mData.getSubCategories().size() == 0 ? 0 : mData
				.getSubCategories().size() + 1;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int pos) {

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
		if (viewtype == BNC_SHOP_BY_CATEGORIES_TYPE_NORMAL) {
			View v = LayoutInflater
					.from(context)
					.inflate(
							R.layout.shop_by_categories_select_semester_grid_item_layout,
							parent, false);
			ShopByCategoryGridViewHolder holder = new ShopByCategoryGridViewHolder(
					v);
			return holder;
		} else {
			View v = LayoutInflater.from(context).inflate(
					R.layout.shop_by_categories_fake_header, parent, false);
			ShopByCategoriesHeaderHolder holder = new ShopByCategoriesHeaderHolder(
					v);
			return holder;
		}
	}

	class ShopByCategoryGridViewHolder extends RecyclerView.ViewHolder {

		public ShopByCategoryGridViewHolder(View v) {
			super(v);
		}
	}

	class ShopByCategoriesHeaderHolder extends RecyclerView.ViewHolder {

		public ShopByCategoriesHeaderHolder(View v) {
			super(v);
		}
	}
}
