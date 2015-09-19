package com.bookncart.app.baseobjects;

import java.util.List;

public class UserProfileObject {

	String first_name;
	String full_name;
	String email;
	String mobile_number;
	String profile_image;
	List<BackgroundImagesArray> background_images;

	public class BackgroundImagesArray {
		String background_image_1;
		String background_image_2;

		public String getBackground_image_1() {
			return background_image_1;
		}

		public void setBackground_image_1(String background_image_1) {
			this.background_image_1 = background_image_1;
		}

		public String getBackground_image_2() {
			return background_image_2;
		}

		public void setBackground_image_2(String background_image_2) {
			this.background_image_2 = background_image_2;
		}

	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getProfile_image() {
		return profile_image;
	}

	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}

	public List<BackgroundImagesArray> getBackground_images() {
		return background_images;
	}

	public void setBackground_images(
			List<BackgroundImagesArray> background_images) {
		this.background_images = background_images;
	}

}
