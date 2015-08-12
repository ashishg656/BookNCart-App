package com.bookncart.app.objects;

public class CartObject {

	private int id;
	private String name;
	private int price;
	private boolean condition;
	private int mrp;
	private String imageUrl;
	private int quantity;

	public CartObject(int id, String name, int price, boolean condition,
			int mrp, String imageUrl, int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.condition = condition;
		this.mrp = mrp;
		this.imageUrl = imageUrl;
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public boolean isCondition() {
		return condition;
	}

	public void setCondition(boolean condition) {
		this.condition = condition;
	}

	public int getMrp() {
		return mrp;
	}

	public void setMrp(int mrp) {
		this.mrp = mrp;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}