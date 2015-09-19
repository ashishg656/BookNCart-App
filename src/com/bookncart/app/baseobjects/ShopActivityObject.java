package com.bookncart.app.baseobjects;

import java.util.List;

public class ShopActivityObject {

	List<BookObject> books;
	Integer next_page;

	public ShopActivityObject(List<BookObject> books, Integer next_page) {
		super();
		this.books = books;
		this.next_page = next_page;
	}

	public List<BookObject> getBooks() {
		return books;
	}

	public void setBooks(List<BookObject> books) {
		this.books = books;
	}

	public Integer getNext_page() {
		return next_page;
	}

	public void setNext_page(Integer next_page) {
		this.next_page = next_page;
	}
}
