package com.bookncart.app.gcm;

import com.bookncart.app.preferences.ZPreferences;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityWatcherService extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("Network", "Network connectivity change");
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

			if (ni != null && ni.isConnectedOrConnecting()) {
				Log.i("Network", "Network " + ni.getTypeName() + " connected");

				if (ZPreferences.isGcmRegistered(context)) {
					ComponentName receiver = new ComponentName(context,
							ConnectivityWatcherService.class);
					context.getPackageManager().setComponentEnabledSetting(
							receiver,
							PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
							PackageManager.DONT_KILL_APP);
				} else {
					Intent intentService = new Intent(context,
							RegistrationIntentService.class);
					context.startService(intentService);
				}
			} else {
				Log.d("Network", "There's no network connectivity");
			}
		} catch (Exception e) {
		}
	}
}
