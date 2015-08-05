package com.bookncart.app.objects;

public class RecentlyViewedBooksObject {

	private int id;
	private String name;
	private int price;
	private boolean isFavourite;
	private String imageUrl;

	public RecentlyViewedBooksObject(int id, String name, int price,
			boolean isFavourite, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.isFavourite = isFavourite;
		this.imageUrl = imageUrl;
	}

	public boolean isFavourite() {
		return isFavourite;
	}

	public void setFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
