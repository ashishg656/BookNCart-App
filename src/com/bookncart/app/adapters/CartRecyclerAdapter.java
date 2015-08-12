package com.bookncart.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.objects.CartObject;

public class CartRecyclerAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	ArrayList<CartObject> mData;
	Context context;
	int imageHeightGrid;

	public CartRecyclerAdapter(ArrayList<CartObject> mData, Context context) {
		super();
		this.mData = mData;
		this.context = context;
	}

	@Override
	public int getItemCount() {
		return mData.size() == 0 ? 0 : mData.size() + 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0)
			return BNC_CART_LIST_TYPE_HEADER;
		else if (position == mData.size() + 1)
			return BNC_CART_LIST_TYPE_FOOTER;
		else
			return BNC_CART_LIST_TYPE_CART_ITEM;
	}

	@Override
	public void onBindViewHolder(ViewHolder commonHolder, int pos) {

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == BNC_CART_LIST_TYPE_CART_ITEM) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.cart_list_item_layout, parent, false);
			CartlistItemHolder holder = new CartlistItemHolder(view);
			return holder;
		} else if (viewType == BNC_CART_LIST_TYPE_FOOTER) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.cart_list_footer_item, parent, false);
			CartFooterViewHolder holder = new CartFooterViewHolder(view);
			return holder;
		} else {
			View view = LayoutInflater.from(context).inflate(
					R.layout.cart_list_header_item, parent, false);
			CartHeaderViewHolder holder = new CartHeaderViewHolder(view);
			return holder;
		}
	}

	class CartlistItemHolder extends RecyclerView.ViewHolder {

		ImageView bookImage;
		TextView bookName;

		public CartlistItemHolder(View v) {
			super(v);
			bookImage = (ImageView) v
					.findViewById(R.id.shop_layout_image_grid_cart);
			bookName = (TextView) v.findViewById(R.id.book_name_shop_grid_cart);
		}
	}

	class CartHeaderViewHolder extends RecyclerView.ViewHolder {

		public CartHeaderViewHolder(View v) {
			super(v);
		}
	}

	class CartFooterViewHolder extends RecyclerView.ViewHolder {

		public CartFooterViewHolder(View v) {
			super(v);
		}
	}
}