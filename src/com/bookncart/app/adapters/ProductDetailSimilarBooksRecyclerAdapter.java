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
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.activities.BookDetailActivity;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.BookObject;
import com.bookncart.app.serverApi.ImageRequestManager;

public class ProductDetailSimilarBooksRecyclerAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	List<BookObject> mData;
	Context context;
	MyOnClickListener onClickListener;

	public ProductDetailSimilarBooksRecyclerAdapter(List<BookObject> mData,
			Context context) {
		super();
		this.mData = mData;
		this.context = context;
		onClickListener = new MyOnClickListener();
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCommon, int pos) {
		TopRatedBooksRecyclerHolder holder = (TopRatedBooksRecyclerHolder) holderCommon;
		holder.bookName.setText(mData.get(pos).getName());
		holder.bookPrice.setText("₹ " + mData.get(pos).getPrice());

		holder.mainContainer.setTag(R.integer.bnc_shop_tag_bookid,
				mData.get(pos).getId());
		holder.mainContainer.setOnClickListener(onClickListener);

		ImageRequestManager.get(context).requestImage(
				context,
				holder.bookImage,
				ZApplication.getInstance().getImageUrl(
						mData.get(pos).getImage_url()), -1);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.home_activity_list_item_top_rated_book_item_layout,
				parent, false);
		TopRatedBooksRecyclerHolder holder = new TopRatedBooksRecyclerHolder(v);
		return holder;
	}

	class TopRatedBooksRecyclerHolder extends RecyclerView.ViewHolder {

		FrameLayout mainContainer;
		ImageView bookImage;
		TextView bookName, bookPrice;

		public TopRatedBooksRecyclerHolder(View v) {
			super(v);
			mainContainer = (FrameLayout) v
					.findViewById(R.id.main_layout_top_rated);
			bookImage = (ImageView) v.findViewById(R.id.top_rated_book_image);
			bookName = (TextView) v.findViewById(R.id.top_rated_book_name);
			bookPrice = (TextView) v.findViewById(R.id.top_rated_book_price);
		}
	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.main_layout_top_rated:
				int bookid = (int) v.getTag(R.integer.bnc_shop_tag_bookid);
				Intent intent = new Intent(context, BookDetailActivity.class);
				intent.putExtra("bookid", bookid);
				context.startActivity(intent);
				break;
			}
		}
	}
}