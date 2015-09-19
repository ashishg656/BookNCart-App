package com.bookncart.app.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;

public class WriteReviewNonLoggedInUserDialog extends Fragment implements
		ZRequestTags, UploadManagerCallback {

	RatingBar ratingBar;
	TextView ratingBarText;
	EditText name, email, review;
	TextView submit;

	int bookid;
	ProgressDialog progressDialog;

	public static WriteReviewNonLoggedInUserDialog newInstance(Bundle bundle) {
		WriteReviewNonLoggedInUserDialog frg = new WriteReviewNonLoggedInUserDialog();
		frg.setArguments(bundle);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.write_review_dialog_non_logged_in_user, container,
				false);

		ratingBar = (RatingBar) v.findViewById(R.id.ratingbar);
		ratingBarText = (TextView) v.findViewById(R.id.ratingtext);
		name = (EditText) v.findViewById(R.id.name);
		email = (EditText) v.findViewById(R.id.email);
		review = (EditText) v.findViewById(R.id.review);
		submit = (TextView) v.findViewById(R.id.submit);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		bookid = getArguments().getInt("bookid");

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
		nameValuePairs.add(new BasicNameValuePair("name", name.getText()
				.toString().trim()));
		nameValuePairs.add(new BasicNameValuePair("email", email.getText()
				.toString()));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_ADD_REVIEW_REQUEST_TAG, BNC_ADD_REVIEW_REQUEST_TAG,
				BNC_ADD_REVIEW_REQUEST_TAG, null, nameValuePairs, null);
	}

	protected boolean checkIfFormFill() {
		if (ratingBar.getRating() == 0) {
			makeToast("Please provide rating");
			return false;
		} else if (name.getText().toString().trim().length() < 1) {
			makeToast("Please enter your name");
			return false;
		} else if (email.getText().toString().trim().length() < 1) {
			makeToast("Please enter your email ID");
			return false;
		} else if (!isValidEmail(email.getText().toString())) {
			makeToast("Please enter valid email address");
			return false;
		} else if (review.getText().toString().trim().length() < 1) {
			makeToast("Please enter review");
			return false;
		}
		return true;
	}

	boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(target)
						.matches();
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
