package com.bookncart.app.baseobjects;

import java.util.List;

public class ReviewObject {

	List<reviewDEtail> reviews;

	public class reviewDEtail {
		String image;
		String name;
		String review;
		String timestamp;
		int rating;

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getReview() {
			return review;
		}

		public void setReview(String review) {
			this.review = review;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public int getRating() {
			return rating;
		}

		public void setRating(int rating) {
			this.rating = rating;
		}

	}

	public List<reviewDEtail> getReviews() {
		return reviews;
	}

	public void setReviews(List<reviewDEtail> reviews) {
		this.reviews = reviews;
	}
}
