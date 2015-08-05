package com.bookncart.app.objects;

import java.util.ArrayList;

public class ShopByCategoriesObject {

	private ArrayList<ShopByCategoriesSingleCategoryObject> subCategories;
	private String categoryName;
	private int categoryId;
	private String categoryImage;

	public ShopByCategoriesObject(
			ArrayList<ShopByCategoriesSingleCategoryObject> subCategories,
			String categoryName, int categoryId, String categoryImage) {
		super();
		this.subCategories = subCategories;
		this.categoryName = categoryName;
		this.categoryId = categoryId;
		this.categoryImage = categoryImage;
	}

	public ArrayList<ShopByCategoriesSingleCategoryObject> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(
			ArrayList<ShopByCategoriesSingleCategoryObject> subCategories) {
		this.subCategories = subCategories;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(String categoryImage) {
		this.categoryImage = categoryImage;
	}

}
