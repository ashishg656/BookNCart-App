package com.bookncart.app.baseobjects;

import java.util.List;

public class HomeActivityRequest2Object {

	List<BookObject> top_rated_books;
	List<BookObject> currently_active_books;
	List<BookObject> latest_books;
	List<BookObject> recently_viewed_books;
	List<TagObject> tags;

	public HomeActivityRequest2Object(List<BookObject> top_rated_books,
			List<BookObject> currently_active_books,
			List<BookObject> latest_books,
			List<BookObject> recently_viewed_books, List<TagObject> tags) {
		super();
		this.top_rated_books = top_rated_books;
		this.currently_active_books = currently_active_books;
		this.latest_books = latest_books;
		this.recently_viewed_books = recently_viewed_books;
		this.tags = tags;
	}

	public List<BookObject> getTop_rated_books() {
		return top_rated_books;
	}

	public void setTop_rated_books(List<BookObject> top_rated_books) {
		this.top_rated_books = top_rated_books;
	}

	public List<BookObject> getCurrently_active_books() {
		return currently_active_books;
	}

	public void setCurrently_active_books(
			List<BookObject> currently_active_books) {
		this.currently_active_books = currently_active_books;
	}

	public List<BookObject> getLatest_books() {
		return latest_books;
	}

	public void setLatest_books(List<BookObject> latest_books) {
		this.latest_books = latest_books;
	}

	public List<BookObject> getRecently_viewed_books() {
		return recently_viewed_books;
	}

	public void setRecently_viewed_books(List<BookObject> recently_viewed_books) {
		this.recently_viewed_books = recently_viewed_books;
	}

	public List<TagObject> getTags() {
		return tags;
	}

	public void setTags(List<TagObject> tags) {
		this.tags = tags;
	}

}
