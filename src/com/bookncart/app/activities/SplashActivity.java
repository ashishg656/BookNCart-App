package com.bookncart.app.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bookncart.app.R;
import com.bookncart.app.gcm.ConnectivityWatcherService;
import com.bookncart.app.preferences.ZPreferences;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity_layout);

		if (!ZPreferences.isGcmRegistered(this)) {
			ComponentName receiver = new ComponentName(this,
					ConnectivityWatcherService.class);
			getPackageManager().setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
		}

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
