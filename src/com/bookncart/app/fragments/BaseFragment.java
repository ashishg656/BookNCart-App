package com.bookncart.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bookncart.app.R;
import com.bookncart.app.animations.ProgressLayoutAnimation;

public class BaseFragment extends Fragment {

	View progressDarkCircle, progressLightCircle;
	ImageView progressImage;
	FrameLayout progressLayoutContainer;
	ProgressLayoutAnimation animForProgressLayoutAnimated;

	LinearLayout connectionErrorLayout;
	LinearLayout retryDataConnectionLayout;
	LinearLayout connectionFailedCloudImage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	void setProgressLayoutVariables(View v) {
		progressDarkCircle = (View) v.findViewById(R.id.dark_circle);
		progressLightCircle = (View) v.findViewById(R.id.light_circle);
		progressImage = (ImageView) v.findViewById(R.id.image_progress);
		progressLayoutContainer = (FrameLayout) v
				.findViewById(R.id.progresslayutcontainre);
	}

	public void showMainContentLoadingAnimations(Context context) {
		animForProgressLayoutAnimated = new ProgressLayoutAnimation(
				progressDarkCircle, progressLightCircle, progressImage,
				progressLayoutContainer, context);
		animForProgressLayoutAnimated.startAnimations();
	}

	public void hideMainContentLoadingAnimations() {
		animForProgressLayoutAnimated.stopAnimations();
	}

	public void setConnectionErrorVariables(View v) {
		connectionErrorLayout = (LinearLayout) v
				.findViewById(R.id.connection_error_layout);
		retryDataConnectionLayout = (LinearLayout) v
				.findViewById(R.id.retrylayoutconnectionerror);
		connectionFailedCloudImage = (LinearLayout) v
				.findViewById(R.id.connectionfailedimagelayout);
	}

	public void showConnectionErrorLayout(View v) {
		connectionErrorLayout.setVisibility(View.VISIBLE);

		connectionFailedCloudImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				retryDataConnectionLayout.performClick();
			}
		});
		v.findViewById(R.id.opensettingslayout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								Settings.ACTION_WIFI_SETTINGS);
						if (intent.resolveActivity(getActivity()
								.getPackageManager()) != null) {
							startActivity(intent);
						}
					}
				});
	}

	public void hideConnectionErrorLayout() {
		if (connectionErrorLayout != null)
			connectionErrorLayout.setVisibility(View.GONE);
	}
}
