package com.bookncart.app.serverApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.ErrorObject;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.utils.CommonLib;

public class UploadManager {

	private static SharedPreferences prefs;
	private static ZApplication zapp;
	private static volatile UploadManager sInstance;
	private static Context mContext;
	private ArrayList<UploadManagerCallback> callbacks = new ArrayList<UploadManagerCallback>();

	/**
	 * Empty constructor to prevent multiple objects in memory
	 */
	private UploadManager() {
	}

	/**
	 * Implementation of double check'd locking scheme.
	 */
	public static UploadManager getInstance() {

		if (sInstance == null) {
			synchronized (UploadManager.class) {
				if (sInstance == null) {
					sInstance = new UploadManager();
				}
			}
		}
		return sInstance;
	}

	public static void setContext(Context context) {
		mContext = context;
		prefs = context.getSharedPreferences("application_settings", 0);

		if (context instanceof ZApplication) {
			zapp = (ZApplication) context;
		}
	}

	public void addCallback(UploadManagerCallback callback, Context context) {
		if (!callbacks.contains(callback)) {
			callbacks.add(callback);
		}
		if (mContext == null) {
			setContext(context.getApplicationContext());
		}
	}

	public void removeCallback(UploadManagerCallback callback) {
		if (callbacks.contains(callback)) {
			callbacks.remove(callback);
		}
	}

	@SuppressWarnings("deprecation")
	public void makeAyncRequest(String url, int requestType, int objectId,
			int parserId, Object object, List<NameValuePair> nameValuePairs,
			MultipartEntity entities) {
		if (nameValuePairs == null)
			nameValuePairs = new ArrayList<>();

		if (ZPreferences.isUserLogIn(mContext)) {
			nameValuePairs.add(new BasicNameValuePair("user_id", ZPreferences
					.getUserID(mContext)));
			nameValuePairs.add(new BasicNameValuePair("user_profile_id",
					ZPreferences.getUserProfileID(mContext)));
		}
		nameValuePairs.add(new BasicNameValuePair("device_id", ZPreferences
				.getDeviceID(mContext)));

		new PostAsyncTask(url, requestType, objectId, parserId, object,
				nameValuePairs, entities).execute();
	}

	public class PostAsyncTask extends AsyncTask<Void, Void, Object> {

		String url;
		HttpResponse response;
		List<NameValuePair> nameValuePairs;
		int requestType, objectId, parserId;
		Object object;
		MultipartEntity entities;

		public PostAsyncTask(String url, int requestType, int objectId,
				int parserId, Object object,
				List<NameValuePair> nameValuePairs, MultipartEntity entities) {
			this.url = url;
			this.requestType = requestType;
			this.objectId = objectId;
			this.parserId = parserId;
			this.nameValuePairs = nameValuePairs;
			this.object = object;
			this.entities = entities;
		}

		@Override
		protected void onPreExecute() {
			for (UploadManagerCallback callback : callbacks) {
				callback.uploadStarted(requestType, objectId, parserId, object);
			}
			super.onPreExecute();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Object doInBackground(Void... params) {
			try {
				HttpPost httpPost = new HttpPost(this.url);
				/*
				 * httpPost.addHeader(new BasicHeader("client-key",
				 * CommonLib.CLIENT_KEY));
				 */

				if (nameValuePairs != null)
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
							"UTF-8"));

				if (entities != null)
					httpPost.setEntity(entities);

				response = HttpManager.execute(httpPost);

				if (response.getStatusLine().getStatusCode() == HttpsURLConnection.HTTP_OK) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(
									(response.getEntity().getContent())));

					StringBuilder builder = new StringBuilder();
					String aux = "";

					while ((aux = br.readLine()) != null) {
						builder.append(aux);
					}

					String text = builder.toString();

					CommonLib.ZLog("data received", text);

					return ParserClass.parseData(text, this.parserId, mContext);
				} else {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(
									(response.getEntity().getContent())));
					StringBuilder builder = new StringBuilder();
					String aux = "";

					while ((aux = br.readLine()) != null) {
						builder.append(aux);
					}
					String text = builder.toString();

					CommonLib.ZLog("error", text);

					ErrorObject obj = new ErrorObject(text, response
							.getStatusLine().getStatusCode());

					return obj;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {

			super.onPostExecute(result);
			if (result != null) {
				try {
					boolean status = response.getStatusLine().getStatusCode() == HttpsURLConnection.HTTP_OK;
					for (UploadManagerCallback callback : callbacks) {
						callback.uploadFinished(requestType, objectId, result,
								objectId, status, parserId);
					}
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				for (UploadManagerCallback callback : callbacks) {
					callback.uploadFinished(
							requestType,
							objectId,
							new ErrorObject("Invalid response from server", 500),
							objectId, false, parserId);
				}
				return;
			}
			for (UploadManagerCallback callback : callbacks) {
				callback.uploadFinished(requestType, objectId, new ErrorObject(
						"Something went wrong", 500), objectId, false, parserId);
			}
			return;
		}
	}
}