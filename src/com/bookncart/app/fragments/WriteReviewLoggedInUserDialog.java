package com.bookncart.app.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bookncart.app.R;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.serverApi.ImageRequestManager;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;
import com.bookncart.app.widgets.CircularImageView;

public class WriteReviewLoggedInUserDialog extends Fragment implements
		ZRequestTags, UploadManagerCallback {

	TextView reviewBy;
	RatingBar ratingBar;
	TextView ratingBarText;
	EditText review;
	TextView submit;
	CircularImageView userImage;

	int bookid;
	ProgressDialog progressDialog;

	public static WriteReviewLoggedInUserDialog newInstance(Bundle bundle) {
		WriteReviewLoggedInUserDialog frg = new WriteReviewLoggedInUserDialog();
		frg.setArguments(bundle);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.write_review_dialog_logged_in_user,
				container, false);

		reviewBy = (TextView) v.findViewById(R.id.reviewby);
		ratingBar = (RatingBar) v.findViewById(R.id.ratingbar);
		ratingBarText = (TextView) v.findViewById(R.id.ratingtext);
		review = (EditText) v.findViewById(R.id.review);
		submit = (TextView) v.findViewById(R.id.submit);
		userImage = (CircularImageView) v.findViewById(R.id.userImage);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		bookid = getArguments().getInt("bookid");

		reviewBy.setText("Review By " + ZPreferences.getUserName(getActivity()));

		ImageRequestManager.get(getActivity()).requestImage(getActivity(),
				userImage, ZPreferences.getUserImageURL(getActivity()), -1);

		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				if (rating == 1)
					ratingBarText.setText("Hated It");
				else if (rating == 2)
					ratingBarText.setText("Disliked It");
				else if (rating == 3)
					ratingBarText.setText("It's Ok");
				else if (rating == 4)
					ratingBarText.setText("Liked It");
				else if (rating == 5)
					ratingBarText.setText("Loved It");
				else
					ratingBarText.setText("");
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkIfFormFill()) {
					sendReviewRequestToServer();
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	protected void sendReviewRequestToServer() {
		String url = ZApplication.getInstance().getBaseUrl() + "add_review/";
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("bookid", Integer
				.toString(bookid)));
		nameValuePairs.add(new BasicNameValuePair("review", review.getText()
				.toString().trim()));
		nameValuePairs.add(new BasicNameValuePair("rating", Integer
				.toString((int) ratingBar.getRating())));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_ADD_REVIEW_REQUEST_TAG, BNC_ADD_REVIEW_REQUEST_TAG,
				BNC_ADD_REVIEW_REQUEST_TAG, null, nameValuePairs, null);
	}

	protected boolean checkIfFormFill() {
		if (ratingBar.getRating() == 0) {
			makeToast("Please provide rating");
			return false;
		} else if (review.getText().toString().trim().length() < 1) {
			makeToast("Please enter review");
			return false;
		}
		return true;
	}

	void makeToast(String s) {
		Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_ADD_REVIEW_REQUEST_TAG) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (status) {
				Toast.makeText(
						getActivity(),
						"Successfuly posted review. Your review will appear here after it gets verified.",
						Toast.LENGTH_SHORT).show();
				getActivity().onBackPressed();
			} else {
				Toast.makeText(
						getActivity(),
						"Unable to add review. Please check internet connection and try again",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_ADD_REVIEW_REQUEST_TAG) {
			progressDialog = ProgressDialog.show(getActivity(), null,
					"Submitting review", true, false);
		}
	}
}
