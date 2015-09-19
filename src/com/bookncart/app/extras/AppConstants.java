package com.bookncart.app.extras;

public interface AppConstants {

	// HOME ACTIVITY
	public static final int BNC_HOME_LIST_TYPE_HEADER = 1;
	public static final int BNC_HOME_LIST_TYPE_CATEGORY = 2;
	public static final int BNC_HOME_LIST_TYPE_TOP_RATED = 3;
	public static final int BNC_HOME_LIST_TYPE_RECENTLY_VIEWED = 4;
	public static final int BNC_HOME_LIST_TYPE_CURRENTLY_ACTIVE = 5;
	public static final int BNC_HOME_LIST_TYPE_FEATURED = 6;
	public static final int BNC_HOME_LIST_TYPE_BEST_SELLING = 7;
	public static final int BNC_HOME_LIST_TYPE_NEW_ADDED = 8;
	public static final int BNC_HOME_LIST_TYPE_TAGS = 9;
	public static final int BNC_HOME_LIST_TYPE_LOADING_FOOTER = 10;

	// SHOP BY CATEGORIES ACTIVITY
	public static final int BNC_SHOP_BY_CATEGORIES_TYPE_HEADER = 1;
	public static final int BNC_SHOP_BY_CATEGORIES_TYPE_NORMAL = 2;

	// SHOP ACTIVITY
	public static final int BNC_SHOP_DISPLAY_TYPE_GRID = 1;
	public static final int BNC_SHOP_DISPLAY_TYPE_LIST = 2;
	public static final int BNC_SHOP_DISPLAY_TYPE_LONG_LIST = 3;

	// SHOP ACTIVITY FILTERS
	public static final int BNC_SHOP_FILTER_CONDITION_OLD = 0;
	public static final int BNC_SHOP_FILTER_CONDITION_NEW = 1;
	public static final int BNC_SHOP_FILTER_CONDITION_BOTH = 2;
	public static final int BNC_SHOP_FILTER_FEATURED_FEATURED = 0;
	public static final int BNC_SHOP_FILTER_FEATURED_NONFEATURED = 1;
	public static final int BNC_SHOP_FILTER_FEATURED_BOTH = 2;

	// CART FRAGMENT
	public static final int BNC_CART_LIST_TYPE_HEADER = 0;
	public static final int BNC_CART_LIST_TYPE_FOOTER = 1;
	public static final int BNC_CART_LIST_TYPE_CART_ITEM = 2;

	// mode for best selling or latest or top rated or latest or featured book
	// in shop activity
	public static final int BNC_MODE_SHOP_FEATURED = 0;
	public static final int BNC_MODE_SHOP_BEST_SELLING = 1;
	public static final int BNC_MODE_SHOP_LATEST = 2;
	public static final int BNC_MODE_SHOP_TOP_RATED = 3;
	public static final int BNC_MODE_SHOP_CURRENTLY_ACTIVE = 4;
	public static final int BNC_MODE_SHOP_CATEGORY_WISE = 6;
	public static final int BNC_MODE_SHOP_TAG_WISE = 7;

	// load more shop activity
	public static final String loadMoreString = "loadMore";
	public static final int BNC_SHOP_TYPE_NORMAL = 0;
	public static final int BNC_SHOP_TYPE_LOADING = 1;
}
