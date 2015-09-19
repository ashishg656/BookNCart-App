package com.bookncart.app.baseobjects;

import java.util.List;

public class CartObject {

	List<CartObjectSingleBook> books;
	int total_quantity;
	int cart_total;
	int delivery_charge;
	int total_amount;

	public class CartObjectSingleBook {
		String name;
		int price;
		String image_url;
		int id;
		String author;
		boolean condition;
		int quantity;

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

		public String getImage_url() {
			return image_url;
		}

		public void setImage_url(String image_url) {
			this.image_url = image_url;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public boolean isCondition() {
			return condition;
		}

		public void setCondition(boolean condition) {
			this.condition = condition;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

	}

	public List<CartObjectSingleBook> getBooks() {
		return books;
	}

	public void setBooks(List<CartObjectSingleBook> books) {
		this.books = books;
	}

	public int getTotal_quantity() {
		return total_quantity;
	}

	public void setTotal_quantity(int total_quantity) {
		this.total_quantity = total_quantity;
	}

	public int getCart_total() {
		return cart_total;
	}

	public void setCart_total(int cart_total) {
		this.cart_total = cart_total;
	}

	public int getDelivery_charge() {
		return delivery_charge;
	}

	public void setDelivery_charge(int delivery_charge) {
		this.delivery_charge = delivery_charge;
	}

	public int getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(int total_amount) {
		this.total_amount = total_amount;
	}

}
