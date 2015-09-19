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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.activities.ShopActivity;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.ShopByCategoriesSingleCategoryObject;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.serverApi.ImageRequestManager;

public class ShopByCategoriesFragmentRecyclerViewAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	List<ShopByCategoriesSingleCategoryObject> mData;
	Context context;
	MyClickListener myClickListener;
	String categoryName;

	public ShopByCategoriesFragmentRecyclerViewAdapter(
			List<ShopByCategoriesSingleCategoryObject> mData, Context context,
			String category) {
		super();
		this.mData = mData;
		this.context = context;
		myClickListener = new MyClickListener();
		this.categoryName = category;
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
		return mData.size() == 0 ? 0 : mData.size() + 1;
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCommon, int pos) {
		if (getItemViewType(pos) == BNC_SHOP_BY_CATEGORIES_TYPE_NORMAL) {
			ShopByCategoryGridViewHolder holder = (ShopByCategoryGridViewHolder) holderCommon;
			holder.categoryName.setText(mData.get(pos - 1).getName());

			ImageRequestManager.get(context).requestImage(
					context,
					holder.categoryImage,
					ZApplication.getInstance().getImageUrl(
							mData.get(pos - 1).getImage_url()), -1);

			holder.containerLayout.setTag(mData.get(pos - 1).getId());
			holder.containerLayout.setOnClickListener(myClickListener);
		}
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

		TextView categoryName;
		ImageView categoryImage;
		LinearLayout containerLayout;

		public ShopByCategoryGridViewHolder(View v) {
			super(v);
			categoryName = (TextView) v.findViewById(R.id.name_selct_semester);
			categoryImage = (ImageView) v
					.findViewById(R.id.imgae_selct_semester);
			containerLayout = (LinearLayout) v
					.findViewById(R.id.container_select_semester);
		}
	}

	class ShopByCategoriesHeaderHolder extends RecyclerView.ViewHolder {

		public ShopByCategoriesHeaderHolder(View v) {
			super(v);
		}
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.container_select_semester:
				int id = (int) v.getTag();
				Intent intent = new Intent(context, ShopActivity.class);
				intent.putExtra("actionbarname", categoryName);
				intent.putExtra("category_wise", id);
				context.startActivity(intent);
				break;

			default:
				break;
			}
		}
	}
}
