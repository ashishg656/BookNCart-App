package com.bookncart.app.application;

import io.fabric.sdk.android.Fabric;

import java.io.File;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.telephony.TelephonyManager;

import com.bookncart.app.R;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.utils.AnalyticsTrackers;
import com.bookncart.app.utils.FontsOverride;
import com.bookncart.app.utils.LruCache;
import com.bookncart.app.utils.NutraBaseImageDecoder;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ZApplication extends Application implements AppConstants {

	public static final String TAG = ZApplication.class.getSimpleName();

	public static ZApplication sInstance;

	public static File cacheDir;
	public LruCache<String, Bitmap> cache;

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());

		sInstance = new ZApplication();

		setFonts("Hind-Light.ttf");

		cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
				"Bookncart");

		initImageLoader(getApplicationContext());

		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		ZPreferences.setDeviceID(this, tManager.getDeviceId());
		ZPreferences.setVersionNo(this, getCurrentVersionNo());

		cache = new LruCache<String, Bitmap>(30);
		UploadManager.setContext(getApplicationContext());

		AnalyticsTrackers.initialize(this);
		AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
	}

	private int getCurrentVersionNo() {
		int v = 0;
		try {
			v = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {

		}
		return v;
	}

	public static ZApplication getInstance() {
		if (sInstance == null) {
			sInstance = new ZApplication();
		}
		return sInstance;
	}

	public String getBaseUrl() {
		return "http://django-bookncart.rhcloud.com/app/";
	}

	public String getImageUrl(String imgUrl) {
		StringBuilder builder = new StringBuilder(getBaseUrl());
		builder.delete(builder.length() - 5, builder.length() - 1);
		builder.append(imgUrl);
		return builder.toString();
	}

	public static void initImageLoader(Context context) {
		Options decodingOptions = new Options();

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.showImageOnLoading(R.drawable.ic_placeholder)
				.decodingOptions(decodingOptions)

				.bitmapConfig(Bitmap.Config.ARGB_8888).build();

		final int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		final int cacheSize = 1024 * 1024 * memClass / 8;

		System.out.println("Memory cache size" + cacheSize);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(5).memoryCacheSize(cacheSize)
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(300)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.imageDecoder(new NutraBaseImageDecoder(true))
				.denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(options)
				.tasksProcessingOrder(QueueProcessingType.FIFO).build();

		ImageLoader.getInstance().init(config);
	}

	void setFonts(String font) {
		String[] types = { "DEFAULT", "MONOSPACE", "SERIF", "SANS_SERIF" };
		for (int i = 0; i < types.length; i++) {
			FontsOverride.setDefaultFont(getApplicationContext(), types[i],
					"fonts/" + font);
		}
	}

	public synchronized Tracker getGoogleAnalyticsTracker() {
		AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
		return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
	}

	public void trackScreenView(String screenName) {
		Tracker t = getGoogleAnalyticsTracker();
		// Set screen name.
		t.setScreenName(screenName);
		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		GoogleAnalytics.getInstance(this).dispatchLocalHits();
	}

	public void trackException(Exception e) {
		if (e != null) {
			Tracker t = getGoogleAnalyticsTracker();

			t.send(new HitBuilders.ExceptionBuilder()
					.setDescription(
							new StandardExceptionParser(this, null)
									.getDescription(Thread.currentThread()
											.getName(), e)).setFatal(false)
					.build());
		}
	}

	public void trackEvent(String category, String action, String label) {
		Tracker t = getGoogleAnalyticsTracker();
		// Build and send an Event.
		t.send(new HitBuilders.EventBuilder().setCategory(category)
				.setAction(action).setLabel(label).build());
	}
}
