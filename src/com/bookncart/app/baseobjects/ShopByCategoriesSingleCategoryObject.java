package com.bookncart.app.baseobjects;

public class ShopByCategoriesSingleCategoryObject {

	int id;
	String image_url;
	String image_url_2;
	String name;

	public ShopByCategoriesSingleCategoryObject() {

	}

	public ShopByCategoriesSingleCategoryObject(int id, String image_url,
			String image_url_2, String name) {
		super();
		this.id = id;
		this.image_url = image_url;
		this.image_url_2 = image_url_2;
		this.name = name;
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

	public String getImage_url_2() {
		return image_url_2;
	}

	public void setImage_url_2(String image_url_2) {
		this.image_url_2 = image_url_2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
