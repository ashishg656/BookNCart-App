package com.bookncart.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookncart.app.R;
import com.bookncart.app.animations.ProgressLayoutAnimation;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.serverApi.UploadManager;

@SuppressLint("NewApi")
public class BaseActivity extends AppCompatActivity implements AppConstants,
		ZRequestTags {

	Toolbar toolbar;
	TextView toolbarTitle;
	int toolbarHeight;
	View progressDarkCircle, progressLightCircle;
	ImageView progressImage;
	FrameLayout progressLayoutContainer;
	ProgressLayoutAnimation animForProgressLayoutAnimated;

	LinearLayout connectionErrorLayout;
	LinearLayout retryDataConnectionLayout;
	LinearLayout connectionFailedCloudImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void showMainContentLoadingAnimations() {
		animForProgressLayoutAnimated = new ProgressLayoutAnimation(
				progressDarkCircle, progressLightCircle, progressImage,
				progressLayoutContainer, this);
		animForProgressLayoutAnimated.startAnimations();
	}

	public void hideMainContentLoadingAnimations() {
		animForProgressLayoutAnimated.stopAnimations();
	}

	public void setConnectionErrorVariables() {
		connectionErrorLayout = (LinearLayout) findViewById(R.id.connection_error_layout);
		retryDataConnectionLayout = (LinearLayout) findViewById(R.id.retrylayoutconnectionerror);
		connectionFailedCloudImage = (LinearLayout) findViewById(R.id.connectionfailedimagelayout);
	}

	public void showConnectionErrorLayout() {
		connectionErrorLayout.setVisibility(View.VISIBLE);

		connectionFailedCloudImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				retryDataConnectionLayout.performClick();
			}
		});
		findViewById(R.id.opensettingslayout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								Settings.ACTION_WIFI_SETTINGS);
						if (intent.resolveActivity(getPackageManager()) != null) {
							startActivity(intent);
						}
					}
				});
	}

	void showShareingOptionsLayout() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
		sendIntent.setType("text/plain");
		if (sendIntent.resolveActivity(getPackageManager()) != null) {
			startActivity(Intent.createChooser(sendIntent, "Share this book"));
		}
	}

	void openWishlistActivity() {
		Intent intent = new Intent(this, WishlistAndCartActivity.class);
		intent.putExtra("wishlist", true);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	void openCartActivity() {
		Intent intent = new Intent(this, WishlistAndCartActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@SuppressWarnings("deprecation")
	void addToFavouritesRequest(int bookId, boolean askForNumberOfLikesOnBook) {
		if (ZPreferences.isUserLogIn(this)) {
			String url = ZApplication.getInstance().getBaseUrl()
					+ "add_to_favourite/";
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("book_id", Integer
					.toString(bookId)));
			if (askForNumberOfLikesOnBook)
				nameValuePairs.add(new BasicNameValuePair(
						"askForNumberOfLikesOnBook", Boolean
								.toString(askForNumberOfLikesOnBook)));
			UploadManager.getInstance().makeAyncRequest(url,
					BNC_ADD_TO_FAVOURITES_REQUEST_TAG,
					BNC_ADD_TO_FAVOURITES_REQUEST_TAG,
					BNC_ADD_TO_FAVOURITES_REQUEST_TAG, null, nameValuePairs,
					null);
		} else {
			Toast.makeText(this, "You need to login to use Wishlist feature",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LaunchActivity.class);
			startActivity(intent);
		}
	}

	public void hideConnectionErrorLayout() {
		if (connectionErrorLayout != null)
			connectionErrorLayout.setVisibility(View.GONE);
	}
}