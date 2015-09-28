package com.bookncart.app.adapters;

import java.util.List;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
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
import com.bookncart.app.activities.BookDetailActivity;
import com.bookncart.app.activities.HomeActivity;
import com.bookncart.app.activities.RecentlyViewedBooksActivity;
import com.bookncart.app.activities.ShopActivity;
import com.bookncart.app.activities.ShopByCategoriesActivity;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.BookObject;
import com.bookncart.app.baseobjects.HomeActivityRequest1Object;
import com.bookncart.app.baseobjects.HomeActivityRequest2Object;
import com.bookncart.app.baseobjects.TagObject;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.serverApi.ImageRequestManager;
import com.bookncart.app.widgets.CirclePageIndicator;
import com.bookncart.app.widgets.CustomFlowLayout;
import com.bookncart.app.widgets.RecyclerViewHorizontalScrolling;
import com.bookncart.app.widgets.RoundedImageView;
import com.bookncart.app.widgets.ViewPagerHorizontalScrolling;

public class HomeActivityRecyclerViewAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	Context context;
	MyOnClickListener onClickListener;
	MyDragListener myDragListener;
	MyLongClickListener myLongClickListener;
	HomeActivityRequest1Object request1Object;
	HomeActivityRequest2Object request2Object;

	public HomeActivityRecyclerViewAdapter(Context context,
			HomeActivityRequest1Object obj) {
		super();
		this.context = context;
		this.request1Object = obj;
		this.request2Object = null;
		onClickListener = new MyOnClickListener();
		myLongClickListener = new MyLongClickListener();
		myDragListener = new MyDragListener();
	}

	@Override
	public int getItemViewType(int position) {
		if (request2Object == null) {
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
				return BNC_HOME_LIST_TYPE_LOADING_FOOTER;
			}
		} else {
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
		}
		return 0;
	}

	@Override
	public int getItemCount() {
		if (request2Object == null)
			return 5;
		else
			return 9;
	}

	public void addDataObject2(HomeActivityRequest2Object obj) {
		this.request2Object = obj;
		notifyDataSetChanged();
	}

	@Override
	public void onBindViewHolder(ViewHolder commonHolder, int pos) {
		switch (getItemViewType(pos)) {
		case BNC_HOME_LIST_TYPE_HEADER: {
			HomeActivityListHeaderViewHolder holder = (HomeActivityListHeaderViewHolder) commonHolder;
			HeaderPagerAdapter adapter = new HeaderPagerAdapter();
			holder.viewPagerHeader.setAdapter(adapter);
			holder.pageIndicator.setViewPager(holder.viewPagerHeader);

			holder.viewPagerHeader
					.addOnPageChangeListener(new OnPageChangeListener() {

						@Override
						public void onPageSelected(int arg0) {
						}

						@Override
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {
						}

						@Override
						public void onPageScrollStateChanged(int state) {
							((HomeActivity) context)
									.enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
						}
					});
		}
			break;
		case BNC_HOME_LIST_TYPE_CATEGORY: {
			HomeActivityListCategoryViewHolder holder = (HomeActivityListCategoryViewHolder) commonHolder;
			holder.categoryName1.setText(request1Object.getCategories().get(0)
					.getName());
			holder.categoryName2.setText(request1Object.getCategories().get(1)
					.getName());
			holder.categoryName3.setText(request1Object.getCategories().get(2)
					.getName());
			ImageRequestManager.get(context).requestImage(
					context,
					holder.categoryImage1,
					ZApplication.getInstance().getImageUrl(
							request1Object.getCategories().get(0)
									.getImage_url()), -1);
			ImageRequestManager.get(context).requestImage(
					context,
					holder.categoryImage2,
					ZApplication.getInstance().getImageUrl(
							request1Object.getCategories().get(1)
									.getImage_url()), -1);
			ImageRequestManager.get(context).requestImage(
					context,
					holder.categoryImage3,
					ZApplication.getInstance().getImageUrl(
							request1Object.getCategories().get(2)
									.getImage_url()), -1);
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
					request1Object.getFeatured_books(),
					BNC_HOME_LIST_TYPE_FEATURED);

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
					request1Object.getBest_selling_books(),
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
					request2Object.getLatest_books(),
					BNC_HOME_LIST_TYPE_NEW_ADDED);
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
					request2Object.getTop_rated_books(),
					BNC_HOME_LIST_TYPE_TOP_RATED);
			holder.recyclerView.setAdapter(adapter);
			holder.showMoreBooksLayout.setOnClickListener(onClickListener);
			holder.showMoreBooksLayout.setTag(BNC_HOME_LIST_TYPE_TOP_RATED);
		}
			break;
		case BNC_HOME_LIST_TYPE_TAGS: {
			HomeActivityListTagsViewHolder holder = (HomeActivityListTagsViewHolder) commonHolder;
			holder.customFlowLayout.removeAllViews();
			for (int i = 0; i < request2Object.getTags().size(); i++) {
				View view = LayoutInflater.from(context).inflate(
						R.layout.tag_layout_blue, holder.customFlowLayout,
						false);

				TagObject tagObject = (TagObject) request2Object.getTags().get(
						i);

				TextView tagName = (TextView) view.findViewById(R.id.tag_name);
				tagName.setText(tagObject.getTag_name());

				LinearLayout containerTag = (LinearLayout) view
						.findViewById(R.id.taglayoutbluecontainer);
				containerTag.setTag(R.integer.bnc_shop_tag_bookid,
						tagObject.getId());
				containerTag.setTag(R.integer.bnc_shop_tag_bookname,
						tagObject.getTag_name());
				containerTag.setOnClickListener(onClickListener);

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
					request2Object.getCurrently_active_books(),
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
					request2Object.getRecently_viewed_books(),
					BNC_HOME_LIST_TYPE_RECENTLY_VIEWED);
			holder.recyclerView.setAdapter(adapter);
			holder.showMoreBooksLayout.setOnClickListener(onClickListener);
			holder.showMoreBooksLayout
					.setTag(BNC_HOME_LIST_TYPE_RECENTLY_VIEWED);
			if (request2Object.getRecently_viewed_books().size() == 0) {
				holder.recyclerView.setVisibility(View.GONE);
				holder.nullCaseText.setVisibility(View.VISIBLE);
			} else {
				holder.recyclerView.setVisibility(View.VISIBLE);
				holder.nullCaseText.setVisibility(View.GONE);
			}
		}
			break;
		case BNC_HOME_LIST_TYPE_LOADING_FOOTER: {

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
		case BNC_HOME_LIST_TYPE_LOADING_FOOTER:
			v = LayoutInflater.from(context).inflate(
					R.layout.loading_more_content_progress, parent, false);
			HomeActivityListFooterLoadingHolder holderLoading = new HomeActivityListFooterLoadingHolder(
					v);
			return holderLoading;
		}
		return null;
	}

	class HomeActivityListFooterLoadingHolder extends RecyclerView.ViewHolder {

		public HomeActivityListFooterLoadingHolder(View v) {
			super(v);
		}
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

		TextView headingText, nullCaseText;
		RecyclerViewHorizontalScrolling recyclerView;
		LinearLayout showMoreBooksLayout;

		public HomeActivityListTopRatedViewHolder(View v) {
			super(v);
			headingText = (TextView) v.findViewById(R.id.home_heading_text);
			recyclerView = (RecyclerViewHorizontalScrolling) v
					.findViewById(R.id.home_recycler_view_toprated);
			showMoreBooksLayout = (LinearLayout) v
					.findViewById(R.id.topratedmorebookslayout);
			nullCaseText = (TextView) v
					.findViewById(R.id.nullcasetextrecentlyviewed);
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
		ImageView categoryImage1, categoryImage2, categoryImage3;
		TextView categoryName1, categoryName2, categoryName3;

		public HomeActivityListCategoryViewHolder(View v) {
			super(v);
			mainLayout1 = (FrameLayout) v
					.findViewById(R.id.main_category_holder1);
			mainLayout2 = (FrameLayout) v
					.findViewById(R.id.main_category_holder2);
			mainLayout3 = (FrameLayout) v
					.findViewById(R.id.main_category_holder3);
			categoryImage1 = (ImageView) v
					.findViewById(R.id.main_category_holder1_image);
			categoryName1 = (TextView) v
					.findViewById(R.id.main_category_holder1_text);
			categoryImage2 = (ImageView) v
					.findViewById(R.id.main_category_holder2_image);
			categoryName2 = (TextView) v
					.findViewById(R.id.main_category_holder2_text);
			categoryImage3 = (ImageView) v
					.findViewById(R.id.main_category_holder3_image);
			categoryName3 = (TextView) v
					.findViewById(R.id.main_category_holder3_text);

			mainLayout1.setOnClickListener(onClickListener);
			mainLayout2.setOnClickListener(onClickListener);
			mainLayout3.setOnClickListener(onClickListener);
		}
	}

	class HeaderPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return request1Object.getBanners().size();
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

			ImageRequestManager.get(context).requestImage(
					context,
					image,
					ZApplication.getInstance().getImageUrl(
							request1Object.getBanners().get(position)
									.getBanner_image()), -1);

			container.addView(imageLayout);
			return imageLayout;
		}
	}

	class TopRatedBooksRecyclerAdapter extends
			RecyclerView.Adapter<RecyclerView.ViewHolder> {

		List<BookObject> mData;
		int bookType;

		public TopRatedBooksRecyclerAdapter(List<BookObject> list, int type) {
			super();
			this.mData = list;
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
			holder.mainContainer.setTag(R.integer.bnc_home_tag_bookname, mData
					.get(pos).getName());
			holder.mainContainer.setTag(R.integer.bnc_home_tag_bookid, mData
					.get(pos).getId());
			holder.mainContainer.setTag(R.integer.bnc_home_tag_isfavourite,
					mData.get(pos).isIs_favourite());
			holder.mainContainer.setOnDragListener(myDragListener);
			holder.mainContainer.setOnLongClickListener(myLongClickListener);
			holder.mainContainer.setOnClickListener(onClickListener);

			holder.bookName.setText(mData.get(pos).getName());
			holder.bookPrice.setText("₹ " + mData.get(pos).getPrice());
			ImageRequestManager.get(context).requestImage(
					context,
					holder.bookImage,
					ZApplication.getInstance().getImageUrl(
							mData.get(pos).getImage_url()), -1);
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

			FrameLayout mainContainer;
			ImageView bookImage;
			TextView bookName, bookPrice;

			public TopRatedBooksRecyclerHolder(View v) {
				super(v);
				mainContainer = (FrameLayout) v
						.findViewById(R.id.main_layout_top_rated);
				bookImage = (ImageView) v
						.findViewById(R.id.top_rated_book_image);
				bookName = (TextView) v.findViewById(R.id.top_rated_book_name);
				bookPrice = (TextView) v
						.findViewById(R.id.top_rated_book_price);
			}
		}
	}

	class RecentlyViewedBooksRecyclerAdapter extends
			RecyclerView.Adapter<RecyclerView.ViewHolder> {

		List<BookObject> mData;
		int bookType;

		public RecentlyViewedBooksRecyclerAdapter(
				List<BookObject> recently_viewed_books,
				int bncHomeListTypeRecentlyViewed) {
			this.mData = recently_viewed_books;
			this.bookType = bncHomeListTypeRecentlyViewed;
		}

		@Override
		public int getItemCount() {
			return mData.size();
		}

		@Override
		public void onBindViewHolder(ViewHolder holderCom, int pos) {
			RecentlyViewedBooksRecyclerHolder holder = (RecentlyViewedBooksRecyclerHolder) holderCom;

			holder.mainContainer.setTag(R.integer.bnc_home_tag_position, pos);
			holder.mainContainer.setTag(R.integer.bnc_home_tag_type, bookType);
			holder.mainContainer.setTag(R.integer.bnc_home_tag_bookname, mData
					.get(pos).getName());
			holder.mainContainer.setTag(R.integer.bnc_home_tag_bookid, mData
					.get(pos).getId());
			holder.mainContainer.setTag(R.integer.bnc_home_tag_isfavourite,
					mData.get(pos).isIs_favourite());
			holder.mainContainer.setOnDragListener(myDragListener);
			holder.mainContainer.setOnLongClickListener(myLongClickListener);
			holder.mainContainer.setOnClickListener(onClickListener);

			holder.bookName.setText(mData.get(pos).getName());
			holder.bookPrice.setText("₹ " + mData.get(pos).getPrice());
			ImageRequestManager.get(context).requestImage(
					context,
					holder.bookImage,
					ZApplication.getInstance().getImageUrl(
							mData.get(pos).getImage_url()), -1);
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

			RoundedImageView bookImage;
			TextView bookName, bookPrice;
			FrameLayout mainContainer;

			public RecentlyViewedBooksRecyclerHolder(View v) {
				super(v);
				mainContainer = (FrameLayout) v
						.findViewById(R.id.main_layout_top_rated);
				bookImage = (RoundedImageView) v
						.findViewById(R.id.top_rated_book_image);
				bookName = (TextView) v
						.findViewById(R.id.recent_viewed_book_name);
				bookPrice = (TextView) v.findViewById(R.id.recent_view_price);
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
					intent = new Intent(context,
							RecentlyViewedBooksActivity.class);
					break;
				}
				intent.putExtra("actionbarname", actionBarTitle);
				context.startActivity(intent);
				break;
			case R.id.main_layout_top_rated:
				intent = new Intent(context, BookDetailActivity.class);
				int bookpos = (int) v.getTag(R.integer.bnc_home_tag_position);
				int booktype = (int) v.getTag(R.integer.bnc_home_tag_type);
				int bookid = 0;
				switch (booktype) {
				case BNC_HOME_LIST_TYPE_FEATURED:
					bookid = request1Object.getFeatured_books().get(bookpos)
							.getId();
					break;
				case BNC_HOME_LIST_TYPE_BEST_SELLING:
					bookid = request1Object.getBest_selling_books()
							.get(bookpos).getId();
					break;
				case BNC_HOME_LIST_TYPE_NEW_ADDED:
					bookid = request2Object.getLatest_books().get(bookpos)
							.getId();
					break;
				case BNC_HOME_LIST_TYPE_TOP_RATED:
					bookid = request2Object.getTop_rated_books().get(bookpos)
							.getId();
					break;
				case BNC_HOME_LIST_TYPE_CURRENTLY_ACTIVE:
					bookid = request2Object.getCurrently_active_books()
							.get(bookpos).getId();
					break;
				case BNC_HOME_LIST_TYPE_RECENTLY_VIEWED:
					bookid = request2Object.getRecently_viewed_books()
							.get(bookpos).getId();
					break;

				default:
					break;
				}
				intent.putExtra("bookid", bookid);
				context.startActivity(intent);
				break;
			case R.id.taglayoutbluecontainer:
				int tagId = (int) v.getTag(R.integer.bnc_shop_tag_bookid);
				String name = (String) v
						.getTag(R.integer.bnc_shop_tag_bookname);
				intent = new Intent(context, ShopActivity.class);
				intent.putExtra("tag_wise", tagId);
				intent.putExtra("actionbarname", name);
				context.startActivity(intent);
			default:
				break;
			}
		}
	}

	class MyLongClickListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			int pos = (int) v.getTag(R.integer.bnc_home_tag_position);
			int type = (int) v.getTag(R.integer.bnc_home_tag_type);
			String bookName = (String) v
					.getTag(R.integer.bnc_home_tag_bookname);
			int bookId = (int) v.getTag(R.integer.bnc_home_tag_bookid);
			boolean isFav = (boolean) v
					.getTag(R.integer.bnc_home_tag_isfavourite);

			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder();
			v.startDrag(data, shadowBuilder, null, 0);
			((HomeActivity) context).showButtonsLayout(bookId, isFav, bookName);

			return true;
		}
	}

	class MyDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				((HomeActivity) context).setButtonsLocation(v, event);
				return true;
			case DragEvent.ACTION_DRAG_ENDED:
				((HomeActivity) context).hideButtonsLayout();
				return true;
			default:
				break;
			}
			return false;
		}
	}
}