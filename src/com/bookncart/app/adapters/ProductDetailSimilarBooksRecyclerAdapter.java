package com.bookncart.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bookncart.app.R;
import com.bookncart.app.objects.HomeTopRatedBookObject;

public class ProductDetailSimilarBooksRecyclerAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	ArrayList<HomeTopRatedBookObject> mData;
	Context context;

	public ProductDetailSimilarBooksRecyclerAdapter(
			ArrayList<HomeTopRatedBookObject> mData, Context context) {
		super();
		this.mData = mData;
		this.context = context;
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCommon, int pos) {
		TopRatedBooksRecyclerHolder holder = (TopRatedBooksRecyclerHolder) holderCommon;
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

		LinearLayout mainContainer;

		public TopRatedBooksRecyclerHolder(View v) {
			super(v);
			mainContainer = (LinearLayout) v
					.findViewById(R.id.main_layout_top_rated);
		}
	}
}