package com.bookncart.app.objects;

import android.content.Context;
import android.util.Log;

import com.bookncart.app.baseobjects.AddToCartObject;
import com.bookncart.app.baseobjects.AddToFavouritesObject;
import com.bookncart.app.baseobjects.BookDetailObject;
import com.bookncart.app.baseobjects.CartObject;
import com.bookncart.app.baseobjects.HomeActivityRequest1Object;
import com.bookncart.app.baseobjects.HomeActivityRequest2Object;
import com.bookncart.app.baseobjects.LoginObject;
import com.bookncart.app.baseobjects.LogoutObject;
import com.bookncart.app.baseobjects.ReviewObject;
import com.bookncart.app.baseobjects.ShopActivityObject;
import com.bookncart.app.baseobjects.ShopByCategoriesObject;
import com.bookncart.app.baseobjects.SimilarBooksObject;
import com.bookncart.app.baseobjects.UserProfileObject;
import com.bookncart.app.preferences.ZPreferences;
import com.google.gson.Gson;

public class AllParserObjects {

	static AllParserObjects sInstance;

	public static AllParserObjects getinstance() {
		if (sInstance == null) {
			sInstance = new AllParserObjects();
		}
		return sInstance;
	}

	public LoginObject parseLoginObject(String text) {
		return new Gson().fromJson(text, LoginObject.class);
	}

	public HomeActivityRequest1Object parseHomeActivityRequestObject1(
			String text, Context context) {
		ZPreferences.setHomeRequest1Object(context, text);
		return new Gson().fromJson(text, HomeActivityRequest1Object.class);
	}

	public HomeActivityRequest2Object parseHomeActivityRequestObject2(
			String text, Context context) {
		ZPreferences.setHomeRequest2Object(context, text);
		return new Gson().fromJson(text, HomeActivityRequest2Object.class);
	}

	public ShopActivityObject parseShopActivityObject(String text) {
		return new Gson().fromJson(text, ShopActivityObject.class);
	}

	public BookDetailObject parseBookDetailObject(String text) {
		return new Gson().fromJson(text, BookDetailObject.class);
	}

	public SimilarBooksObject parseSimilarBooksHomeObject(String text) {
		return new Gson().fromJson(text, SimilarBooksObject.class);
	}

	public ShopByCategoriesObject parseAllCategoriesObject(String text) {
		return new Gson().fromJson(text, ShopByCategoriesObject.class);
	}

	public UserProfileObject parseUserProfileObject(String text) {
		return new Gson().fromJson(text, UserProfileObject.class);
	}

	public ShopActivityObject parseRecentlyViewedBooksActivityObject(String text) {
		return new Gson().fromJson(text, ShopActivityObject.class);
	}

	public AddToFavouritesObject parseAddToFavouritesObject(String text) {
		return new Gson().fromJson(text, AddToFavouritesObject.class);
	}

	public ShopActivityObject parseWishlistRequestObject(String text) {
		return new Gson().fromJson(text, ShopActivityObject.class);
	}

	public ShopActivityObject parseSearchResultsObject(String text) {
		return new Gson().fromJson(text, ShopActivityObject.class);
	}

	public LogoutObject parseLogoutObject(String string) {
		return new Gson().fromJson(string, LogoutObject.class);
	}

	public ReviewObject parseReviewObject(String s) {
		return new Gson().fromJson(s, ReviewObject.class);
	}

	public CartObject parseViewCartObject(String s) {
		return new Gson().fromJson(s, CartObject.class);
	}
	
	public AddToCartObject parseAddToCartObject(String s){
		return new Gson().fromJson(s, AddToCartObject.class);
	}
}
