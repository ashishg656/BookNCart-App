package com.bookncart.app.serverApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.os.AsyncTask;

import com.bookncart.app.baseobjects.ErrorObject;

public class GetRequestManager {

	private static volatile GetRequestManager sInstance;

	Context mContext;

	ArrayList<GetRequestListener> callbacks = new ArrayList<GetRequestListener>();

	private GetRequestManager() {

	}

	public static GetRequestManager getInstance() {

		if (sInstance == null) {
			synchronized (GetRequestManager.class) {
				if (sInstance == null) {
					sInstance = new GetRequestManager();
				}
			}
		}
		return sInstance;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	public void addCallbacks(GetRequestListener callback, Context context) {
		if (!callbacks.contains(callback))
			callbacks.add(callback);
		if (mContext == null) {
			setContext(mContext.getApplicationContext());
		}
	}

	public void removeCallbacks(GetRequestListener callback) {
		if (callbacks.contains(callback))
			callbacks.remove(callback);
	}

	public void makeAyncRequest(String url, String requestTag, int objType) {
		new GetAsyncTask(url, requestTag, objType).execute();
	}

	public class GetAsyncTask extends AsyncTask<Void, Void, Object> {

		String url;

		String requestTag;

		int objType;

		HttpResponse response;

		public GetAsyncTask(String url, String requestTag, int objType) {
			this.url = url;
			this.requestTag = requestTag;
			this.objType = objType;
		}

		@Override
		protected void onPreExecute() {
			for (GetRequestListener callback : callbacks) {
				callback.onRequestStarted(requestTag);
			}
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Void... params) {
			try {
				// URL url = new URL(this.url);
				HttpGet get = new HttpGet(this.url);
				// get.addHeader("client-key","mwoa6g876bxgd8qc3z98vvqlm9j6ixom");
				response = HttpManager.execute(get);
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

					return ParserClass.parseData(text, this.objType, mContext);

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
					if (response.getStatusLine().getStatusCode() == HttpsURLConnection.HTTP_OK) {

						for (GetRequestListener callback : callbacks) {
							callback.onRequestCompleted(requestTag, result);
						}

						return;
					} else {
						for (GetRequestListener callback : callbacks) {
							callback.onRequestFailed(requestTag, result);
						}
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					for (GetRequestListener callback : callbacks) {
						callback.onRequestFailed(requestTag, new ErrorObject(
								"Network Error", 500, 1));

					}
					return;
				}

			} else {
				for (GetRequestListener callback : callbacks) {
					callback.onRequestFailed(requestTag, new ErrorObject(
							"Network Error", 500, 1));

				}
				return;
			}
		}
	}
}
