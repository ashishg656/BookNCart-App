package com.bookncart.app.objects;

import java.util.ArrayList;

public class HomeActivityListObject {

	private ArrayList<String> headerImages;
	private ArrayList<String> categoriesImages;
	private ArrayList<Integer> categoriesImagesId;
	private ArrayList<HomeTopRatedBookObject> topRatedBooks;
	private ArrayList<HomeTopRatedBookObject> bestSellingBooks;
	private ArrayList<HomeTopRatedBookObject> newAddedBooks;
	private ArrayList<HomeTopRatedBookObject> featuredBooks;
	private ArrayList<RecentlyViewedBooksObject> recentlyViewedBooks;
	private ArrayList<HomeTopRatedBookObject> currentlyViewedBooks;
	private ArrayList<TagObject> tags;

	public HomeActivityListObject(ArrayList<String> headerImages,
			ArrayList<String> categoriesImages,
			ArrayList<Integer> categoriesImagesId,
			ArrayList<HomeTopRatedBookObject> topRatedBooks,
			ArrayList<HomeTopRatedBookObject> bestSellingBooks,
			ArrayList<HomeTopRatedBookObject> newAddedBooks,
			ArrayList<HomeTopRatedBookObject> featuredBooks,
			ArrayList<RecentlyViewedBooksObject> recentlyViewedBooks,
			ArrayList<HomeTopRatedBookObject> currentlyViewedBooks,
			ArrayList<TagObject> tags) {
		super();
		this.headerImages = headerImages;
		this.categoriesImages = categoriesImages;
		this.categoriesImagesId = categoriesImagesId;
		this.topRatedBooks = topRatedBooks;
		this.bestSellingBooks = bestSellingBooks;
		this.newAddedBooks = newAddedBooks;
		this.featuredBooks = featuredBooks;
		this.recentlyViewedBooks = recentlyViewedBooks;
		this.currentlyViewedBooks = currentlyViewedBooks;
		this.tags = tags;
	}

	public ArrayList<TagObject> getTags() {
		return tags;
	}

	public void setTags(ArrayList<TagObject> tags) {
		this.tags = tags;
	}

	public ArrayList<String> getHeaderImages() {
		return headerImages;
	}

	public void setHeaderImages(ArrayList<String> headerImages) {
		this.headerImages = headerImages;
	}

	public ArrayList<String> getCategoriesImages() {
		return categoriesImages;
	}

	public void setCategoriesImages(ArrayList<String> categoriesImages) {
		this.categoriesImages = categoriesImages;
	}

	public ArrayList<Integer> getCategoriesImagesId() {
		return categoriesImagesId;
	}

	public void setCategoriesImagesId(ArrayList<Integer> categoriesImagesId) {
		this.categoriesImagesId = categoriesImagesId;
	}

	public ArrayList<HomeTopRatedBookObject> getTopRatedBooks() {
		return topRatedBooks;
	}

	public void setTopRatedBooks(ArrayList<HomeTopRatedBookObject> topRatedBooks) {
		this.topRatedBooks = topRatedBooks;
	}

	public ArrayList<HomeTopRatedBookObject> getBestSellingBooks() {
		return bestSellingBooks;
	}

	public void setBestSellingBooks(
			ArrayList<HomeTopRatedBookObject> bestSellingBooks) {
		this.bestSellingBooks = bestSellingBooks;
	}

	public ArrayList<HomeTopRatedBookObject> getNewAddedBooks() {
		return newAddedBooks;
	}

	public void setNewAddedBooks(ArrayList<HomeTopRatedBookObject> newAddedBooks) {
		this.newAddedBooks = newAddedBooks;
	}

	public ArrayList<HomeTopRatedBookObject> getFeaturedBooks() {
		return featuredBooks;
	}

	public void setFeaturedBooks(ArrayList<HomeTopRatedBookObject> featuredBooks) {
		this.featuredBooks = featuredBooks;
	}

	public ArrayList<RecentlyViewedBooksObject> getRecentlyViewedBooks() {
		return recentlyViewedBooks;
	}

	public void setRecentlyViewedBooks(
			ArrayList<RecentlyViewedBooksObject> recentlyViewedBooks) {
		this.recentlyViewedBooks = recentlyViewedBooks;
	}

	public ArrayList<HomeTopRatedBookObject> getCurrentlyViewedBooks() {
		return currentlyViewedBooks;
	}

	public void setCurrentlyViewedBooks(
			ArrayList<HomeTopRatedBookObject> currentlyViewedBooks) {
		this.currentlyViewedBooks = currentlyViewedBooks;
	}
}
