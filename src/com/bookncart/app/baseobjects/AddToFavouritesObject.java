package com.bookncart.app.baseobjects;

public class AddToFavouritesObject {

	boolean error;
	boolean removedFromFavourites;
	int wishlist_count;
	int numberOfLikesOnBook;

	public int getNumberOfLikesOnBook() {
		return numberOfLikesOnBook;
	}

	public void setNumberOfLikesOnBook(int numberOfLikesOnBook) {
		this.numberOfLikesOnBook = numberOfLikesOnBook;
	}

	public int getWishlist_count() {
		return wishlist_count;
	}

	public void setWishlist_count(int wishlist_count) {
		this.wishlist_count = wishlist_count;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isRemovedFromFavourites() {
		return removedFromFavourites;
	}

	public void setRemovedFromFavourites(boolean removedFromFavourites) {
		this.removedFromFavourites = removedFromFavourites;
	}

}
