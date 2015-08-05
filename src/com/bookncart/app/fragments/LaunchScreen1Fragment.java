package com.bookncart.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookncart.app.R;

public class LaunchScreen1Fragment extends Fragment {

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

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
