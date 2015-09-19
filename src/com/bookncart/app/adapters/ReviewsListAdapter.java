package com.bookncart.app.adapters;

import com.bookncart.app.R;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.ReviewObject;
import com.bookncart.app.serverApi.ImageRequestManager;
import com.bookncart.app.widgets.CircularImageView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewsListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	Context context;
	ReviewObject mData;

	public ReviewsListAdapter(Context context, ReviewObject mData) {
		super();
		this.context = context;
		this.mData = mData;
	}

	@Override
	public int getItemCount() {
		return mData.getReviews().size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holderc, int pos) {
		ReviewHolder holder = (ReviewHolder) holderc;
		holder.name.setText(mData.getReviews().get(pos).getName());
		holder.review.setText(mData.getReviews().get(pos).getReview());
		holder.time.setText(mData.getReviews().get(pos).getTimestamp());
		holder.rating.setRating(mData.getReviews().get(pos).getRating());
		if (mData.getReviews().get(pos).getImage() != null) {
			ImageRequestManager.get(context).requestImage(context,
					holder.userImage, mData.getReviews().get(pos).getImage(),
					-1);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.review_list_item_layout, arg0, false);
		ReviewHolder holder = new ReviewHolder(v);
		return holder;
	}

	class ReviewHolder extends RecyclerView.ViewHolder {

		CircularImageView userImage;
		TextView name, time, review;
		RatingBar rating;

		public ReviewHolder(View v) {
			super(v);
			userImage = (CircularImageView) v.findViewById(R.id.userImage);
			name = (TextView) v.findViewById(R.id.name);
			time = (TextView) v.findViewById(R.id.time);
			review = (TextView) v.findViewById(R.id.review);
			rating = (RatingBar) v.findViewById(R.id.rating);
		}
	}
}
