package com.bookncart.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bookncart.app.R;

public class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_activity_layout);

		openLaunchActivity();
	}

	private void openLaunchActivity() {
		Intent intent = new Intent(this, LaunchActivity.class);
		startActivity(intent);
	}
}
