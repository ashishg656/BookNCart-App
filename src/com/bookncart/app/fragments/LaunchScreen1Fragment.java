package com.bookncart.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookncart.app.R;

public class LaunchScreen1Fragment extends Fragment {

	int position;
	ImageView screenshot;
	TextView description, logo;

	public static LaunchScreen1Fragment newInstance(Bundle b) {
		LaunchScreen1Fragment frg = new LaunchScreen1Fragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.launch_screen_1_fragment, container,
				false);

		screenshot = (ImageView) v.findViewById(R.id.imagelaunch);
		description = (TextView) v.findViewById(R.id.desc);
		logo = (TextView) v.findViewById(R.id.logo);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		position = getArguments().getInt("position");

		if (position == 0) {
			screenshot.setVisibility(View.GONE);
			description.setVisibility(View.GONE);
			logo.setVisibility(View.VISIBLE);
		} else if (position == 1) {
			screenshot.setImageResource(R.drawable.app_screenshot_home);
			description.setText(getActivity().getResources().getString(
					R.string.bnc_home_description));
			logo.setVisibility(View.GONE);
		} else if (position == 2) {
			screenshot
					.setImageResource(R.drawable.app_screenshot_similar_books);
			description.setText(getActivity().getResources().getString(
					R.string.bnc_similar_description));
			logo.setVisibility(View.GONE);
		}
	}
}
