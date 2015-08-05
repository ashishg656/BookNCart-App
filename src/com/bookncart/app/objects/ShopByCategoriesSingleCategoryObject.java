package com.bookncart.app.objects;

public class ShopByCategoriesSingleCategoryObject {

	private String name;
	private int id;
	private Integer parentId;
	private boolean isLast;
	private boolean isRoot;
	private String imageUrl;

	public ShopByCategoriesSingleCategoryObject(String name, int id,
			Integer parentId, boolean isLast, boolean isRoot, String imageUrl) {
		super();
		this.name = name;
		this.id = id;
		this.parentId = parentId;
		this.isLast = isLast;
		this.isRoot = isRoot;
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

}
