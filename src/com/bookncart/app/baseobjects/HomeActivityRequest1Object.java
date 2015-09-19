package com.bookncart.app.baseobjects;

import java.util.List;

public class HomeActivityRequest1Object {

	private List<BannerObject> banners;
	private List<BookObject> featured_books;
	private List<BookObject> best_selling_books;
	private List<ShopByCategoriesSingleCategoryObject> categories;
	int wishlist_count;
	int cart_count;

	public HomeActivityRequest1Object(List<BannerObject> banners,
			List<BookObject> featured_books,
			List<BookObject> best_selling_books,
			List<ShopByCategoriesSingleCategoryObject> categories) {
		super();
		this.banners = banners;
		this.featured_books = featured_books;
		this.best_selling_books = best_selling_books;
		this.categories = categories;
	}

	public class BannerObject {
		String banner_image;

		public BannerObject(String banner_image) {
			super();
			this.banner_image = banner_image;
		}

		public String getBanner_image() {
			return banner_image;
		}

		public void setBanner_image(String banner_image) {
			this.banner_image = banner_image;
		}

	}

	public int getWishlist_count() {
		return wishlist_count;
	}

	public void setWishlist_count(int wishlist_count) {
		this.wishlist_count = wishlist_count;
	}

	public List<BannerObject> getBanners() {
		return banners;
	}

	public int getCart_count() {
		return cart_count;
	}

	public void setCart_count(int cart_count) {
		this.cart_count = cart_count;
	}

	public void setBanners(List<BannerObject> banners) {
		this.banners = banners;
	}

	public List<BookObject> getFeatured_books() {
		return featured_books;
	}

	public void setFeatured_books(List<BookObject> featured_books) {
		this.featured_books = featured_books;
	}

	public List<BookObject> getBest_selling_books() {
		return best_selling_books;
	}

	public void setBest_selling_books(List<BookObject> best_selling_books) {
		this.best_selling_books = best_selling_books;
	}

	public List<ShopByCategoriesSingleCategoryObject> getCategories() {
		return categories;
	}

	public void setCategories(
			List<ShopByCategoriesSingleCategoryObject> categories) {
		this.categories = categories;
	}

}
