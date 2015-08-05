package com.bookncart.app.fragments;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.widgets.CustomFlowLayout;
import com.bookncart.app.widgets.CustomFlowLayout.LayoutParams;

public class LaunchScreen4Fragment extends Fragment {

	CustomFlowLayout backgroundLayout;
	Timer timer;
	Boolean isVisibleToUserFlag;

	public static LaunchScreen4Fragment newInstance(Bundle b) {
		LaunchScreen4Fragment frg = new LaunchScreen4Fragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.launch_screen_4_fragment, container,
				false);

		backgroundLayout = (CustomFlowLayout) v.findViewById(R.id.flow_layout);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		isVisibleToUserFlag = false;

		CustomFlowLayout.LayoutParams params = new CustomFlowLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int pixels8 = getActivity().getResources().getDimensionPixelSize(
				R.dimen.bnc_margin_small);
		int pixels4 = getActivity().getResources().getDimensionPixelSize(
				R.dimen.bnc_margin_mini);
		params.setMargins(pixels8, pixels8, pixels8, pixels8);
		for (int i = 0; i < items.length; i++) {
			TextView textView = new TextView(getActivity());
			textView.setText(items[i]);
			textView.setBackgroundResource(R.drawable.bnc_square_textview_background_without_corner_white);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
			textView.setPadding(pixels8, pixels4, pixels8, pixels4);
			textView.setTextColor(getResources().getColor(R.color.bnc_white));
			textView.setLayoutParams(params);
			backgroundLayout.addView(textView);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (timer != null) {
			timer.cancel();
			backgroundLayout.scrollTo(0, 0);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isVisibleToUserFlag == true) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (getActivity() != null) {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								backgroundLayout.scrollBy(0, 1);
							}
						});
					}
				}
			}, 60, 30);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			isVisibleToUserFlag = true;
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (getActivity() != null) {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								backgroundLayout.scrollBy(0, 1);
							}
						});
					}
				}
			}, 60, 30);
		} else {
			isVisibleToUserFlag = false;
			if (timer != null) {
				timer.cancel();
				backgroundLayout.scrollTo(0, 0);
			}
		}
	}

	String[] items = { "Engineering", "Database", "Digital", "Systems",
			"Computers", "Networks", "Warehouse", "Management", "Physics",
			"Mathematics", "Chemistry", "Signals", "Semester", "Ramesh babu",
			"Sahlivahan", "Data", "Mobile", "Communication", "Bhavya",
			"Manufacturing", "Process", "Skills", "Technology", "Green",
			"Revolution", "Science", "BOOKNCART", "Auto CAD", "Mechanics",
			"Electrical", "Circuits", "Analog", "Foundation", "Object",
			"Oriented", "Programming", "JAVA", "C", "C++", "Structures",
			"Drawing", "Theory", "Automata", "Multimedia", "Applications",
			"Applied", "Modern", "Graphics", "Operating", "Systems",
			"Microprocessor", "Architecture", "Telecommunication", "Algorithm",
			"Analysis", "Design", "Processing", "Interface", "Control",
			"Software", "Compiler", "Construction", "Mobile", "Computing",
			"Cloud", "Reliability", "Advanced", "Optical", "Artificial",
			"Intelligence", "E-commerce", "Testing", "HTML", "Web",
			"Simulation", "Modelling", "Embedded", "Engineering", "Database",
			"Digital", "Systems", "Computers", "Networks", "Warehouse",
			"BOOKNCART", "Management", "Physics", "Mathematics", "Chemistry",
			"Signals", "Semester", "Ramesh babu", "Sahlivahan", "Data",
			"Mobile", "Communication", "Bhavya", "Manufacturing", "Process",
			"Skills", "Technology", "Green", "Revolution", "Science",
			"Auto CAD", "Mechanics", "Electrical", "Circuits", "Analog",
			"Foundation", "Object", "Oriented", "Programming", "JAVA", "C",
			"C++", "Structures", "Drawing", "Theory", "Automata", "Multimedia",
			"Applications", "Applied", "Modern", "Graphics", "Operating",
			"Systems", "Microprocessor", "Architecture", "Telecommunication",
			"Algorithm", "Analysis", "Design", "Processing", "Interface",
			"Control", "Software", "Compiler", "Construction", "Mobile",
			"Computing", "Cloud", "Reliability", "Advanced", "Optical",
			"Artificial", "Intelligence", "E-commerce", "Testing", "HTML",
			"Web", "Simulation", "Modelling", "Embedded", "BOOKNCART", };
}
