package com.bookncart.app.baseobjects;

public class BookObject {

	int id;
	String image_url;
	boolean is_favourite;
	int price;
	String name;
	String author;

	public BookObject() {

	}

	public BookObject(int id, String image_url, boolean is_favourite,
			int price, String name) {
		super();
		this.id = id;
		this.image_url = image_url;
		this.is_favourite = is_favourite;
		this.price = price;
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public boolean isIs_favourite() {
		return is_favourite;
	}

	public void setIs_favourite(boolean is_favourite) {
		this.is_favourite = is_favourite;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
