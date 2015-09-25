package com.bookncart.app.serverApi;

import android.content.Context;

import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.objects.AllParserObjects;

/**
 * Created by Saurabh on 25-08-2015.
 */
public class ParserClass implements ZRequestTags {

	public static Object parseData(String text, int objType, Context context) {
		if (objType == BNC_LOGIN_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseLoginObject(text);
		} else if (objType == BNC_HOME_REQUEST_TAG_1) {
			return AllParserObjects.getinstance()
					.parseHomeActivityRequestObject1(text, context);
		} else if (objType == BNC_HOME_REQUEST_TAG_2) {
			return AllParserObjects.getinstance()
					.parseHomeActivityRequestObject2(text, context);
		} else if (objType == BNC_SHOP_ACTIVTY_TAG) {
			return AllParserObjects.getinstance().parseShopActivityObject(text);
		} else if (objType == BNC_BOOK_DETAIL_ACTIVITY_TAG) {
			return AllParserObjects.getinstance().parseBookDetailObject(text);
		} else if (objType == BNC_HOME_SIMILAR_BOOKS_REQUEST) {
			return AllParserObjects.getinstance().parseSimilarBooksHomeObject(
					text);
		} else if (objType == BNC_CATEGORIES_REQUEST_TAG) {
			return AllParserObjects.getinstance()
					.parseAllCategoriesObject(text);
		} else if (objType == BNC_USER_PROFILE_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseUserProfileObject(text);
		} else if (objType == BNC_RECENT_VIEWED_BOOKS) {
			return AllParserObjects.getinstance()
					.parseRecentlyViewedBooksActivityObject(text);
		} else if (objType == BNC_ADD_TO_FAVOURITES_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseAddToFavouritesObject(
					text);
		} else if (objType == BNC_VIEW_WISHLIST_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseWishlistRequestObject(
					text);
		} else if (objType == BNC_WISHLIST_REMOVE_FROM_WISHLIST) {
			return AllParserObjects.getinstance().parseAddToFavouritesObject(
					text);
		} else if (objType == BNC_SEARCH_AUTOCOMLETE_REQUEST) {
			return AllParserObjects.getinstance()
					.parseSearchResultsObject(text);
		} else if (objType == BNC_LOGOUT_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseLogoutObject(text);
		} else if (objType == BNC_ADD_REVIEW_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseAddToFavouritesObject(
					text);
		} else if (objType == BNC_VIEW_REVIEWS_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseReviewObject(text);
		} else if (objType == BNC_VIEW_CART_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseViewCartObject(text);
		} else if (objType == BNC_EDIT_CART_ITEM_QUANTITY_TAG) {
			return AllParserObjects.getinstance().parseAddToCartObject(text);
		} else if (objType == BNC_CART_REMOVE_FROM_CART) {
			return AllParserObjects.getinstance().parseAddToCartObject(text);
		} else if (objType == BNC_CART_READD_CART_ITEM) {
			return AllParserObjects.getinstance().parseAddToCartObject(text);
		} else if (objType == BNC_ADD_TO_CART_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseAddToCartObject(text);
		} else if (objType == BNC_BUYNOW_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseAddToCartObject(text);
		} else if (objType == BNC_CONFIRM_ORDER_REQUEST_TAG) {
			return AllParserObjects.getinstance().parseViewCartObject(text);
		}
		return null;
	}

}
