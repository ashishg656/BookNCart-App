package com.bookncart.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bookncart.app.R;
import com.bookncart.app.preferences.ZPreferences;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity_layout);

		openLaunchActivityOrHomeActivity();
	}

	private void openLaunchActivityOrHomeActivity() {
		if (ZPreferences.isUserLogIn(this)) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			this.finish();
		} else {
			Intent intent = new Intent(this, LaunchActivity.class);
			startActivity(intent);
			this.finish();
		}
	}
}
