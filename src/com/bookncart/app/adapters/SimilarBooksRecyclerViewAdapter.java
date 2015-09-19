package com.bookncart.app.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.activities.BookDetailActivity;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.BookObject;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.serverApi.ImageRequestManager;
import com.bookncart.app.widgets.RoundedImageView;

public class SimilarBooksRecyclerViewAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	List<BookObject> mData;
	Context context;
	int imageHeightGrid;
	MyOnClickListener onClickListener;

	public SimilarBooksRecyclerViewAdapter(List<BookObject> mData,
			Context context) {
		super();
		this.mData = mData;
		this.context = context;
		imageHeightGrid = (int) (context.getResources().getDisplayMetrics().widthPixels / 2.5f);
		onClickListener = new MyOnClickListener();
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

		holder.bookName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

		holder.containerLayout.setTag(R.integer.bnc_shop_tag_bookid,
				mData.get(pos).getId());
		holder.containerLayout.setOnClickListener(onClickListener);

		holder.bookName.setText(mData.get(pos).getName());
		holder.bookPrice.setText("₹ " + mData.get(pos).getPrice());
		ImageRequestManager.get(context).requestImage(
				context,
				holder.bookImage,
				ZApplication.getInstance().getImageUrl(
						mData.get(pos).getImage_url()), -1);
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
		TextView bookName, bookPrice;
		FrameLayout containerLayout;

		public ShopItemHolder(View v) {
			super(v);
			bookImage = (RoundedImageView) v
					.findViewById(R.id.shop_layout_image_grid);
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
