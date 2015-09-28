package com.bookncart.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bookncart.app.application.ZApplication;

/**
 * Utility class.
 */
public class CommonLib {

	public static final boolean isTestBuild = false;
	public static final String BaseUrl = "base url here";
	/**
	 * Preference names
	 */
	public final static String APP_SETTINGS = "application_settings";
	public static final String preferenceName = "preferences";
	public static final String login = "login";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";

	/**
	 * API key
	 */
	public static final String APIKEY = "";
	public static final String CLIENT_KEY = "mwoa6g876bxgd8qc3z98vvqlm9j6ixom";

	/**
	 * GCM Sender ID
	 */
	public static final String GCM_SENDER_ID = "509487250429";
	public static final String GCM_TAG = "GCM Message Tag";
	/**
	 * Types
	 */
	public static final String Z_FEED_TYPE_ARTICLE_STRING = "article";
	public static final String Z_FEED_TYPE_PHOTO_STRING = "expert";
	public static final int Z_FEED_TYPE_ARTICLE = 0;
	public static final int Z_FEED_TYPE_PHOTO = 1;
	public static final int CHAT_ALIGNMENT_LEFT = 2;
	public static final int CHAT_ALIGNMENT_RIGHT = 3;
	public static final int Z_EXPERT_DETAIL_LIST_TYPE_HEADER = 4;
	public static final int Z_EXPERT_DETAIL_LIST_TYPE_PROJECT = 5;
	public static final int Z_USER_PROFILE_LIST_TYPE_HEADER = 6;
	public static final int Z_USER_PROFILE_LIST_TYPE_ARTICLE = 7;
	public static final int Z_USER_PROFILE_LIST_TYPE_PHOTO = 8;
	public static final int Z_USER_PROFILE_LIST_TYPE_FOOTER = 9;

	/**
	 * Object types
	 */
	public static final int OBJECT_TYPE_HOME_OBJECT = 1;
	public static final int OBJECT_TYPE_ARTICLE_LIST_OBJECT = 2;
	public static final int OBJECT_TYPE_PHOTO_LIST_OBJECT = 3;
	public static final int OBJECT_TYPE_PRODUCT_LIST_OBJECT = 4;
	public static final int OBJECT_TYPE_SHOP_CATEGORY_OBJECT = 5;
	public static final int OBJECT_TYPE_SHOP_SUB_CATEGORY_OBJECT = 6;
	public static final int OBJECT_TYPE_EXPERT_LIST_OBJECT = 7;
	public static final int OBJECT_TYPE_COMPLETE_CATEGORY_LIST = 8;

	// improvised photos url suffix for disk cache
	public final static String PHOTO_TYPE_SNIPPET = "PHOTO_TYPE_SNIPPET";
	public final static String PHOTO_TYPE_LARGE = "PHOTO_TYPE_LARGE";
	public final static String PHOTO_TYPE_DOWNLOADED = "PHOTO_TYPE_DOWNLOADED";

	public final static String CRASHLYTICS_VERSION_STRING = "Version 1.0 Dev";

	/**
	 * Executors
	 */
	private static final int mImageAsyncsMaxSize = 4;
	public static final BlockingQueue<Runnable> sPoolWorkQueueImage = new LinkedBlockingQueue<Runnable>(
			128);
	private static ThreadFactory sThreadFactoryImage = new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r);
		}
	};
	public static final Executor THREAD_POOL_EXECUTOR_IMAGE = new ThreadPoolExecutor(
			mImageAsyncsMaxSize, mImageAsyncsMaxSize, 1, TimeUnit.SECONDS,
			sPoolWorkQueueImage, sThreadFactoryImage);

	/**
	 * Log control
	 */
	public final static boolean ZimplyLog = false;

	/**
	 * Check if the device if Android L and perform necessary operations
	 */
	public static boolean isAndroidL() {
		return android.os.Build.VERSION.SDK_INT >= 21;
	}

	/**
	 * Used for Bitmap sampling. Simply returns the sample size by which the
	 * image from disk is to be scaled down to load in memory.
	 * 
	 * @param options
	 *            Options containing origin height and width of image
	 * @param reqWidth
	 *            required width
	 * @param reqHeight
	 *            required hegiht
	 * @return Returns sample size
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	/**
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return distance in km
	 */

	public static double distFrom(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 6371;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(lat1)
				* Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}
	
	/**
	 * {@see {@link Log#println(int, String, String)}
	 * 
	 * @param logLevel
	 *            The priority/type of this log message. This can be any one of
	 *            the log level available in {@link Log} e.g.
	 *            {@link Log#VERBOSE}, {@link Log#DEBUG}, {@link Log#INFO},
	 *            {@link Log#WARN}, {@link Log#ERROR}
	 * @param tag
	 *            Preferably the name of the class like
	 *            CommonLib.class.getSimpleName {@see
	 *            {@link Class#getSimpleName()}
	 * @param message
	 *            A message in two parts - method/location in class, followed by
	 *            the message/information
	 */
	public static void ZLog(int logLevel, String tag, @NonNull String message) {
		if (ZimplyLog && message != null) {
			Log.println(logLevel, tag, message);
		}
	}

	public static void ZLog(String Tag, String Message) {
		if (ZimplyLog && Message != null)
			Log.i(Tag, Message);
	}

	public static void ZLog(String Tag, float Message) {
		if (ZimplyLog)
			Log.i(Tag, Message + "");
	}

	public static void ZLog(String Tag, boolean Message) {
		if (ZimplyLog)
			Log.i(Tag, Message + "");
	}

	public static void ZLog(String Tag, int Message) {
		if (ZimplyLog)
			Log.i(Tag, Message + "");
	}

	/**
	 * Get a rounded cornered bitmap
	 * 
	 * @param bitmap
	 *            Bitmap which is to be rounded
	 * @param roundPx
	 *            Circular radius
	 */
	public static Bitmap getRoundedCornerBitmap(final Bitmap bitmap,
			final float roundPx) {

		if (bitmap != null) {
			try {
				final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
						bitmap.getHeight(), Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(output);

				final Paint paint = new Paint();
				final Rect rect = new Rect(0, 0, bitmap.getWidth(),
						bitmap.getHeight());
				final RectF rectF = new RectF(rect);

				paint.setAntiAlias(true);
				canvas.drawARGB(0, 0, 0, 0);
				canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

				paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
				canvas.drawBitmap(bitmap, rect, rect, paint);

				return output;

			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/**
	 * Check if network is available.
	 */
	public static boolean isNetworkAvailable(Context c) {
		ConnectivityManager connectivityManager = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * Convert the response to HTTPResponse.
	 */
	public static InputStream getStream(HttpResponse response)
			throws IllegalStateException, IOException {

		InputStream instream = response.getEntity().getContent();
		Header contentEncoding = response.getFirstHeader("Content-Encoding");
		if (contentEncoding != null
				&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
			instream = new GZIPInputStream(instream);
		}
		return instream;
	}

	/**
	 * Check for low memory and clear the Application's bitmap cache.
	 */
	public static void clearCache(Activity context) {

		ZApplication zapp = (ZApplication) context.getApplication();

		if (zapp != null) {
			if (zapp.cache != null)
				zapp.cache.clear();

		}
	}

	/**
	 * When storing images in memory, the images should be scaled down. Use this
	 * utility to check whether to scale down images or not.
	 */
	public static boolean shouldScaleDownBitmap(Context context, Bitmap bitmap) {
		if (context != null && bitmap != null && bitmap.getWidth() > 0
				&& bitmap.getHeight() > 0) {
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			int width = metrics.widthPixels;
			int height = metrics.heightPixels;
			return ((width != 0 && width / bitmap.getWidth() < 1) || (height != 0 && height
					/ bitmap.getHeight() < 1));
		}
		return false;
	}

	/**
	 * Returns the bitmap associated
	 */
	public static Bitmap getBitmap(Context mContext, int resId, int width,
			int height) throws OutOfMemoryError {
		if (mContext == null)
			return null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(mContext.getResources(), resId, options);
		options.inSampleSize = CommonLib.calculateInSampleSize(options, width,
				height);
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;

		if (!CommonLib.isAndroidL())
			options.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
				resId, options);

		return bitmap;
	}

	/**
	 * Remove the keyboard explicitly.
	 */
	public static void hideKeyBoard(Activity mActivity, View mGetView) {
		try {
			((InputMethodManager) mActivity
					.getSystemService(Activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(mGetView.getRootView()
							.getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getBitmapFromDisk(String url, Context ctx) {

		Bitmap defautBitmap = null;
		try {
			String filename = constructFileName(url);
			File filePath = new File(ctx.getCacheDir(), filename);

			if (filePath.exists() && filePath.isFile()
					&& !filePath.isDirectory()) {
				FileInputStream fi;
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				fi = new FileInputStream(filePath);
				defautBitmap = BitmapFactory.decodeStream(fi, null, opts);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (Exception e) {

		} catch (OutOfMemoryError e) {

		}

		return defautBitmap;
	}

	public static void writeBitmapToDisk(String url, Bitmap bmp, Context ctx,
			Bitmap.CompressFormat format) {
		FileOutputStream fos;
		String fileName = constructFileName(url);
		try {
			if (bmp != null) {
				fos = new FileOutputStream(
						new File(ctx.getCacheDir(), fileName));
				bmp.compress(format, 75, fos);
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String constructFileName(String url) {
		return url.replaceAll("/", "_");
	}

}
