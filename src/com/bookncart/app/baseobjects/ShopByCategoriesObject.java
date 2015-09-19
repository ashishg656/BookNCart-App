package com.bookncart.app.baseobjects;

import java.util.List;

public class ShopByCategoriesObject {

	List<CategoriesObject> main_categories;

	public class CategoriesObject {
		String name;
		int id;
		String image_url;
		String image_url_2;
		List<ShopByCategoriesSingleCategoryObject> sub_categories;

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

		public List<ShopByCategoriesSingleCategoryObject> getSub_categories() {
			return sub_categories;
		}

		public void setSub_categories(
				List<ShopByCategoriesSingleCategoryObject> sub_categories) {
			this.sub_categories = sub_categories;
		}
	}

	public List<CategoriesObject> getMain_categories() {
		return main_categories;
	}

	public void setMain_categories(List<CategoriesObject> main_categories) {
		this.main_categories = main_categories;
	}

}
