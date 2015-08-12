package com.bookncart.app.adapters;

import java.util.ArrayList;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.activities.HomeActivity;
import com.bookncart.app.activities.ShopActivity;
import com.bookncart.app.activities.ShopByCategoriesActivity;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.objects.HomeActivityListObject;
import com.bookncart.app.objects.HomeTopRatedBookObject;
import com.bookncart.app.objects.RecentlyViewedBooksObject;
import com.bookncart.app.widgets.CirclePageIndicator;
import com.bookncart.app.widgets.CustomFlowLayout;
import com.bookncart.app.widgets.RecyclerViewHorizontalScrolling;
import com.bookncart.app.widgets.ViewPagerHorizontalScrolling;

public class HomeActivityRecyclerViewAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	Context context;
	HomeActivityListObject mData;
	MyOnClickListener onClickListener;
	MyDragListener myDragListener;
	MyLongClickListener myLongClickListener;

	public HomeActivityRecyclerViewAdapter(Context context,
			HomeActivityListObject mData) {
		super();
		this.context = context;
		this.mData = mData;
		onClickListener = new MyOnClickListener();
		myLongClickListener = new MyLongClickListener();
		myDragListener = new MyDragListener();
	}

	@Override
	public int getItemViewType(int position) {
		switch (position) {
		case 0:
			return BNC_HOME_LIST_TYPE_HEADER;
		case 1:
			return BNC_HOME_LIST_TYPE_CATEGORY;
		case 2:
			return BNC_HOME_LIST_TYPE_FEATURED;
		case 3:
			return BNC_HOME_LIST_TYPE_BEST_SELLING;
		case 4:
			return BNC_HOME_LIST_TYPE_NEW_ADDED;
		case 5:
			return BNC_HOME_LIST_TYPE_TOP_RATED;
		case 6:
			return BNC_HOME_LIST_TYPE_TAGS;
		case 7:
			return BNC_HOME_LIST_TYPE_CURRENTLY_ACTIVE;
		case 8:
			return BNC_HOME_LIST_TYPE_RECENTLY_VIEWED;
		}
		return 0;
	}

	@Override
	public int getItemCount() {
		return 9;
	}

	@Override
	public void onBindViewHolder(ViewHolder commonHolder, int pos) {
		switch (getItemViewType(pos)) {
		case BNC_HOME_LIST_TYPE_HEADER: {
			HomeActivityListHeaderViewHolder holder = (HomeActivityListHeaderViewHolder) commonHolder;
			HeaderPagerAdapter adapter = new HeaderPagerAdapter();
			holder.viewPagerHeader.setAdapter(adapter);
			holder.pageIndicator.setViewPager(holder.viewPagerHeader);
		}
			break;
		case BNC_HOME_LIST_TYPE_CATEGORY: {
			HomeActivityListCategoryViewHolder holder = (HomeActivityListCategoryViewHolder) commonHolder;
		}
			break;
		case BNC_HOME_LIST_TYPE_FEATURED: {
			HomeActivityListTopRatedViewHolder holder = (HomeActivityListTopRatedViewHolder) commonHolder;
			holder.headingText.setText(context.getResources().getString(
					R.string.bnc_featured_books));
			LinearLayoutManager layoutManager = new LinearLayoutManager(
					context, LinearLayoutManager.HORIZONTAL, false);
			holder.recyclerView.setLayoutManager(layoutManager);
			TopRatedBooksRecyclerAdapter adapter = new TopRatedBooksRecyclerAdapter(
					mData.getFeaturedBooks(), BNC_HOME_LIST_TYPE_FEATURED);
			holder.recyclerView.setAdapter(adapter);
			holder.showMoreBooksLayout.setOnClickListener(onClickListener);
			holder.showMoreBooksLayout.setTag(BNC_HOME_LIST_TYPE_FEATURED);
		}
			break;
		case BNC_HOME_LIST_TYPE_BEST_SELLING: {
			HomeActivityListTopRatedViewHolder holder = (HomeActivityListTopRatedViewHolder) commonHolder;
			holder.headingText.setText(context.getResources().getString(
					R.string.bnc_best_selling_books));
			LinearLayoutManager layoutManager = new LinearLayoutManager(
					context, LinearLayoutManager.HORIZONTAL, false);
			holder.recyclerView.setLayoutManager(layoutManager);
			TopRatedBooksRecyclerAdapter adapter = new TopRatedBooksRecyclerAdapter(
					mData.getBestSellingBooks(),
					BNC_HOME_LIST_TYPE_BEST_SELLING);
			holder.recyclerView.setAdapter(adapter);
			holder.showMoreBooksLayout.setOnClickListener(onClickListener);
			holder.showMoreBooksLayout.setTag(BNC_HOME_LIST_TYPE_BEST_SELLING);
		}
			break;
		case BNC_HOME_LIST_TYPE_NEW_ADDED: {
			HomeActivityListTopRatedViewHolder holder = (HomeActivityListTopRatedViewHolder) commonHolder;
			holder.headingText.setText(context.getResources().getString(
					R.string.bnc_new_added_books));
			LinearLayoutManager layoutManager = new LinearLayoutManager(
					context, LinearLayoutManager.HORIZONTAL, false);
			holder.recyclerView.setLayoutManager(layoutManager);
			TopRatedBooksRecyclerAdapter adapter = new TopRatedBooksRecyclerAdapter(
					mData.getNewAddedBooks(), BNC_HOME_LIST_TYPE_NEW_ADDED);
			holder.recyclerView.setAdapter(adapter);
			holder.showMoreBooksLayout.setOnClickListener(onClickListener);
			holder.showMoreBooksLayout.setTag(BNC_HOME_LIST_TYPE_NEW_ADDED);
		}
			break;
		case BNC_HOME_LIST_TYPE_TOP_RATED: {
			HomeActivityListTopRatedViewHolder holder = (HomeActivityListTopRatedViewHolder) commonHolder;
			holder.headingText.setText(context.getResources().getString(
					R.string.bnc_top_rated_books));
			LinearLayoutManager layoutManager = new LinearLayoutManager(
					context, LinearLayoutManager.HORIZONTAL, false);
			holder.recyclerView.setLayoutManager(layoutManager);
			TopRatedBooksRecyclerAdapter adapter = new TopRatedBooksRecyclerAdapter(
					mData.getTopRatedBooks(), BNC_HOME_LIST_TYPE_TOP_RATED);
			holder.recyclerView.setAdapter(adapter);
			holder.showMoreBooksLayout.setOnClickListener(onClickListener);
			holder.showMoreBooksLayout.setTag(BNC_HOME_LIST_TYPE_TOP_RATED);
		}
			break;
		case BNC_HOME_LIST_TYPE_TAGS: {
			HomeActivityListTagsViewHolder holder = (HomeActivityListTagsViewHolder) commonHolder;
			holder.customFlowLayout.removeAllViews();
			for (int i = 0; i < mData.getTags().size(); i++) {
				View view = LayoutInflater.from(context).inflate(
						R.layout.tag_layout_blue, holder.customFlowLayout,
						false);

				TextView tagName = (TextView) view.findViewById(R.id.tag_name);
				tagName.setText(mData.getTags().get(i).getName());

				holder.customFlowLayout.addView(view);
			}
		}
			break;
		case BNC_HOME_LIST_TYPE_CURRENTLY_ACTIVE: {
			HomeActivityListTopRatedViewHolder holder = (HomeActivityListTopRatedViewHolder) commonHolder;
			holder.headingText.setText(context.getResources().getString(
					R.string.bnc_currently_active_books));
			LinearLayoutManager layoutManager = new LinearLayoutManager(
					context, LinearLayoutManager.HORIZONTAL, false);
			holder.recyclerView.setLayoutManager(layoutManager);
			TopRatedBooksRecyclerAdapter adapter = new TopRatedBooksRecyclerAdapter(
					mData.getCurrentlyViewedBooks(),
					BNC_HOME_LIST_TYPE_CURRENTLY_ACTIVE);
			holder.recyclerView.setAdapter(adapter);
			holder.showMoreBooksLayout.setOnClickListener(onClickListener);
			holder.showMoreBooksLayout
					.setTag(BNC_HOME_LIST_TYPE_CURRENTLY_ACTIVE);
		}
			break;
		case BNC_HOME_LIST_TYPE_RECENTLY_VIEWED: {
			HomeActivityListTopRatedViewHolder holder = (HomeActivityListTopRatedViewHolder) commonHolder;
			holder.headingText.setText(context.getResources().getString(
					R.string.bnc_recently_viewed_books));
			LinearLayoutManager layoutManager = new LinearLayoutManager(
					context, LinearLayoutManager.HORIZONTAL, false);
			holder.recyclerView.setLayoutManager(layoutManager);
			RecentlyViewedBooksRecyclerAdapter adapter = new RecentlyViewedBooksRecyclerAdapter(
					mData.getRecentlyViewedBooks());
			holder.recyclerView.setAdapter(adapter);
			holder.showMoreBooksLayout.setOnClickListener(onClickListener);
			holder.showMoreBooksLayout
					.setTag(BNC_HOME_LIST_TYPE_RECENTLY_VIEWED);
		}
			break;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case BNC_HOME_LIST_TYPE_HEADER:
			View v = LayoutInflater.from(context).inflate(
					R.layout.home_activity_list_item_header, parent, false);
			HomeActivityListHeaderViewHolder holderHeader = new HomeActivityListHeaderViewHolder(
					v);
			return holderHeader;
		case BNC_HOME_LIST_TYPE_CATEGORY:
			v = LayoutInflater.from(context).inflate(
					R.layout.home_activity_list_item_categories, parent, false);
			HomeActivityListCategoryViewHolder holderCategory = new HomeActivityListCategoryViewHolder(
					v);
			return holderCategory;
		case BNC_HOME_LIST_TYPE_TAGS:
			v = LayoutInflater.from(context).inflate(
					R.layout.home_activity_list_item_shop_by_tags, parent,
					false);
			HomeActivityListTagsViewHolder holderTags = new HomeActivityListTagsViewHolder(
					v);
			return holderTags;
		case BNC_HOME_LIST_TYPE_FEATURED:
		case BNC_HOME_LIST_TYPE_BEST_SELLING:
		case BNC_HOME_LIST_TYPE_NEW_ADDED:
		case BNC_HOME_LIST_TYPE_TOP_RATED:
		case BNC_HOME_LIST_TYPE_CURRENTLY_ACTIVE:
			v = LayoutInflater.from(context).inflate(
					R.layout.home_activity_list_item_top_rated_books, parent,
					false);
			HomeActivityListTopRatedViewHolder holderTopRated = new HomeActivityListTopRatedViewHolder(
					v);
			return holderTopRated;
		case BNC_HOME_LIST_TYPE_RECENTLY_VIEWED:
			v = LayoutInflater.from(context).inflate(
					R.layout.home_activity_list_item_recently_viewed_books,
					parent, false);
			holderTopRated = new HomeActivityListTopRatedViewHolder(v);
			return holderTopRated;
		}
		return null;
	}

	class HomeActivityListHeaderViewHolder extends RecyclerView.ViewHolder {

		ViewPagerHorizontalScrolling viewPagerHeader;
		CirclePageIndicator pageIndicator;

		public HomeActivityListHeaderViewHolder(View v) {
			super(v);
			viewPagerHeader = (ViewPagerHorizontalScrolling) v
					.findViewById(R.id.pager_home_header);
			pageIndicator = (CirclePageIndicator) v
					.findViewById(R.id.circle_page_indicator);
		}
	}

	class HomeActivityListTopRatedViewHolder extends RecyclerView.ViewHolder {

		TextView headingText;
		RecyclerViewHorizontalScrolling recyclerView;
		LinearLayout showMoreBooksLayout;

		public HomeActivityListTopRatedViewHolder(View v) {
			super(v);
			headingText = (TextView) v.findViewById(R.id.home_heading_text);
			recyclerView = (RecyclerViewHorizontalScrolling) v
					.findViewById(R.id.home_recycler_view_toprated);
			showMoreBooksLayout = (LinearLayout) v
					.findViewById(R.id.topratedmorebookslayout);
		}
	}

	class HomeActivityListTagsViewHolder extends RecyclerView.ViewHolder {

		CustomFlowLayout customFlowLayout;

		public HomeActivityListTagsViewHolder(View v) {
			super(v);
			customFlowLayout = (CustomFlowLayout) v
					.findViewById(R.id.tagcustomflowlayouthome);
		}
	}

	class HomeActivityListCategoryViewHolder extends RecyclerView.ViewHolder {

		FrameLayout mainLayout1, mainLayout2, mainLayout3;

		public HomeActivityListCategoryViewHolder(View v) {
			super(v);
			mainLayout1 = (FrameLayout) v
					.findViewById(R.id.main_category_holder1);
			mainLayout2 = (FrameLayout) v
					.findViewById(R.id.main_category_holder2);
			mainLayout3 = (FrameLayout) v
					.findViewById(R.id.main_category_holder3);
			mainLayout1.setOnClickListener(onClickListener);
			mainLayout2.setOnClickListener(onClickListener);
			mainLayout3.setOnClickListener(onClickListener);
		}
	}

	class HeaderPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mData.getHeaderImages().size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view.equals(obj);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View imageLayout = LayoutInflater
					.from(context)
					.inflate(
							R.layout.home_activity_list_item_header_viewpager_image_layout,
							container, false);
			ImageView image = (ImageView) imageLayout
					.findViewById(R.id.header_images_home_header);

			container.addView(imageLayout);
			return imageLayout;
		}
	}

	class TopRatedBooksRecyclerAdapter extends
			RecyclerView.Adapter<RecyclerView.ViewHolder> {

		ArrayList<HomeTopRatedBookObject> mData;
		int bookType;

		public TopRatedBooksRecyclerAdapter(
				ArrayList<HomeTopRatedBookObject> mData, int type) {
			super();
			this.mData = mData;
			this.bookType = type;
		}

		@Override
		public int getItemCount() {
			return mData.size();
		}

		@Override
		public void onBindViewHolder(ViewHolder holderCommon, int pos) {
			TopRatedBooksRecyclerHolder holder = (TopRatedBooksRecyclerHolder) holderCommon;
			holder.mainContainer.setTag(R.integer.bnc_home_tag_position, pos);
			holder.mainContainer.setTag(R.integer.bnc_home_tag_type, bookType);
			holder.mainContainer.setOnDragListener(myDragListener);
			holder.mainContainer.setOnLongClickListener(myLongClickListener);
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
			View v = LayoutInflater
					.from(context)
					.inflate(
							R.layout.home_activity_list_item_top_rated_book_item_layout,
							parent, false);
			TopRatedBooksRecyclerHolder holder = new TopRatedBooksRecyclerHolder(
					v);
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

	class RecentlyViewedBooksRecyclerAdapter extends
			RecyclerView.Adapter<RecyclerView.ViewHolder> {

		ArrayList<RecentlyViewedBooksObject> mData;

		public RecentlyViewedBooksRecyclerAdapter(
				ArrayList<RecentlyViewedBooksObject> mData) {
			super();
			this.mData = mData;
		}

		@Override
		public int getItemCount() {
			return mData.size();
		}

		@Override
		public void onBindViewHolder(ViewHolder arg0, int arg1) {

		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
			View v = LayoutInflater
					.from(context)
					.inflate(
							R.layout.home_activity_list_item_recently_viewed_book_item_layout,
							parent, false);
			RecentlyViewedBooksRecyclerHolder holder = new RecentlyViewedBooksRecyclerHolder(
					v);
			return holder;
		}

		class RecentlyViewedBooksRecyclerHolder extends RecyclerView.ViewHolder {

			public RecentlyViewedBooksRecyclerHolder(View v) {
				super(v);
			}
		}
	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.main_category_holder1:
				Intent intent = new Intent(context,
						ShopByCategoriesActivity.class);
				intent.putExtra("categorypos", 0);
				context.startActivity(intent);
				break;

			case R.id.main_category_holder2:
				intent = new Intent(context, ShopByCategoriesActivity.class);
				intent.putExtra("categorypos", 1);
				context.startActivity(intent);
				break;
			case R.id.main_category_holder3:
				intent = new Intent(context, ShopByCategoriesActivity.class);
				intent.putExtra("categorypos", 2);
				context.startActivity(intent);
				break;
			case R.id.topratedmorebookslayout:
				int bookType = (int) v.getTag();
				intent = new Intent(context, ShopActivity.class);
				intent.putExtra("home_book_type", bookType);
				String actionBarTitle = "BookNCart";
				switch (bookType) {
				case BNC_HOME_LIST_TYPE_BEST_SELLING:
					actionBarTitle = context.getResources().getString(
							R.string.bnc_best_selling_books);
					break;
				case BNC_HOME_LIST_TYPE_FEATURED:
					actionBarTitle = context.getResources().getString(
							R.string.bnc_featured_books);
					break;
				case BNC_HOME_LIST_TYPE_NEW_ADDED:
					actionBarTitle = context.getResources().getString(
							R.string.bnc_new_added_books);
					break;
				case BNC_HOME_LIST_TYPE_TOP_RATED:
					actionBarTitle = context.getResources().getString(
							R.string.bnc_top_rated_books);
					break;
				case BNC_HOME_LIST_TYPE_CURRENTLY_ACTIVE:
					actionBarTitle = context.getResources().getString(
							R.string.bnc_currently_active_books);
					break;
				case BNC_HOME_LIST_TYPE_RECENTLY_VIEWED:
					actionBarTitle = context.getResources().getString(
							R.string.bnc_recently_viewed_books);
					break;
				}
				intent.putExtra("actionbarname", actionBarTitle);
				context.startActivity(intent);
				break;
			default:
				Log.w("As", "chde");
				break;
			}
		}
	}

	class MyLongClickListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			int pos = (int) v.getTag(R.integer.bnc_home_tag_position);
			int type = (int) v.getTag(R.integer.bnc_home_tag_type);
			Log.w("as", "positoon " + pos + " - " + type);
			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder();
			v.startDrag(data, shadowBuilder, null, 0);
			((HomeActivity) context).showButtonsLayout();

			return true;
		}
	}

	class MyDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				Log.w("as", "drag start");
				((HomeActivity) context).setButtonsLocation(v, event);
				return true;
			case DragEvent.ACTION_DRAG_ENDED:
				((HomeActivity) context).hideButtonsLayout();
				Log.w("as", "drag end");
				return true;
			default:
				Log.w("as", "drag some " + action);
				break;
			}
			return false;
		}
	}
}