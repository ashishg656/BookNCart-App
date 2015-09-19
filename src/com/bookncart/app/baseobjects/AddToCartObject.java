package com.bookncart.app.baseobjects;

public class AddToCartObject {

	boolean error;
	String errorMessage;
	boolean isAlreadyTen;
	int cart_count;
	int total_quantity;
	int cart_total;
	int delivery_charge;
	int total_amount;

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

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isAlreadyTen() {
		return isAlreadyTen;
	}

	public void setAlreadyTen(boolean isAlreadyTen) {
		this.isAlreadyTen = isAlreadyTen;
	}

	public int getCart_count() {
		return cart_count;
	}

	public void setCart_count(int cart_count) {
		this.cart_count = cart_count;
	}

}
