package com.bookncart.app.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.activities.BookDetailActivity;
import com.bookncart.app.activities.ShopActivity;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.BookObject;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.serverApi.ImageRequestManager;

public class ShopActivityListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	public List<BookObject> mData;
	Context context;
	int imageHeightGrid, imageHeightLongList;
	MyOnClickListener onClickListener;

	public ShopActivityListAdapter(List<BookObject> obj, Context context,
			boolean isMoreAllowed) {
		super();
		this.mData = obj;
		this.context = context;
		imageHeightGrid = (int) (context.getResources().getDisplayMetrics().widthPixels / 1.8f);
		imageHeightLongList = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.6);
		if (isMoreAllowed) {
			mData.add(new BookObject(0, loadMoreString, false, 0, ""));
		}
		onClickListener = new MyOnClickListener();
	}

	public void addData(List<BookObject> data, boolean isMoreAllowed) {
		mData.remove(mData.size() - 1);
		notifyItemRemoved(mData.size());
		int previousSize = mData.size();
		int newSize = mData.size() + data.size();
		mData.addAll(data);
		notifyItemRangeInserted(previousSize, newSize);
		if (isMoreAllowed) {
			mData.add(new BookObject(0, loadMoreString, false, 0, ""));
			notifyItemInserted(mData.size() - 1);
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (mData.get(position).getImage_url().equals(loadMoreString))
			return BNC_SHOP_TYPE_LOADING;
		else
			return BNC_SHOP_TYPE_NORMAL;
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCommon, int pos) {
		if (getItemViewType(pos) == BNC_SHOP_TYPE_NORMAL) {
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
			} else if (((ShopActivity) context).currentDisplayType == BNC_SHOP_DISPLAY_TYPE_LIST) {

			}

			holder.bookName.setText(mData.get(pos).getName());
			holder.bookPrice.setText("₹ " + mData.get(pos).getPrice());

			holder.containerLayout.setTag(R.integer.bnc_shop_tag_bookname,
					mData.get(pos).getName());
			holder.containerLayout.setTag(R.integer.bnc_shop_tag_bookid, mData
					.get(pos).getId());
			holder.containerLayout.setTag(
					R.integer.bnc_shop_tag_book_isfavourite, mData.get(pos)
							.isIs_favourite());

			holder.containerLayout.setOnClickListener(onClickListener);

			ImageRequestManager.get(context).requestImage(
					context,
					holder.bookImage,
					ZApplication.getInstance().getImageUrl(
							mData.get(pos).getImage_url()), -1);
		} else {

		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == BNC_SHOP_TYPE_LOADING) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.loading_more_content_progress, parent, false);
			LoadingMoreHolder holder = new LoadingMoreHolder(view);
			return holder;
		} else {
			int resourceId = 0;
			if (((ShopActivity) context).currentDisplayType == BNC_SHOP_DISPLAY_TYPE_GRID) {
				resourceId = R.layout.shop_activity_recycler_view_list_item_layout_gridlike;
			} else if (((ShopActivity) context).currentDisplayType == BNC_SHOP_DISPLAY_TYPE_LIST) {
				resourceId = R.layout.shop_activity_recycler_view_list_item_layout_listlike;
			} else if (((ShopActivity) context).currentDisplayType == BNC_SHOP_DISPLAY_TYPE_LONG_LIST) {
				resourceId = R.layout.shop_activity_recycler_view_list_item_layout_longlistlike;
			}
			View view = LayoutInflater.from(context).inflate(resourceId,
					parent, false);
			ShopItemHolder holder = new ShopItemHolder(view);
			return holder;
		}
	}

	class LoadingMoreHolder extends RecyclerView.ViewHolder {

		public LoadingMoreHolder(View arg0) {
			super(arg0);
		}
	}

	class ShopItemHolder extends RecyclerView.ViewHolder {

		ImageView bookImage;
		TextView bookName;
		TextView bookPrice;
		FrameLayout containerLayout;

		public ShopItemHolder(View v) {
			super(v);
			bookImage = (ImageView) v.findViewById(R.id.shop_layout_image_grid);
			bookName = (TextView) v.findViewById(R.id.book_name_shop_grid);
			bookPrice = (TextView) v.findViewById(R.id.book_price_shop_grid);
			containerLayout = (FrameLayout) v
					.findViewById(R.id.shopactivityitemviewholdercontainer);
		}
	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.shopactivityitemviewholdercontainer:
				int bookid = (int) v.getTag(R.integer.bnc_shop_tag_bookid);
				Intent intent = new Intent(context, BookDetailActivity.class);
				intent.putExtra("bookid", bookid);
				context.startActivity(intent);
				break;
			}
		}
	}
}
