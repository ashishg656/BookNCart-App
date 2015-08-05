package com.bookncart.app.objects;

public class HomeTopRatedBookObject {

	private int id;
	private String name;
	private int price;
	private boolean isfavourite;
	private String imageUrl;

	public HomeTopRatedBookObject(int id, String name, int price,
			boolean isfavourite, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.isfavourite = isfavourite;
		this.imageUrl = imageUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isIsfavourite() {
		return isfavourite;
	}

	public void setIsfavourite(boolean isfavourite) {
		this.isfavourite = isfavourite;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
