package com.bookncart.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.bookncart.app.R;
import com.bookncart.app.adapters.SearchListAdapter;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.ShopActivityObject;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SearchActivity extends BaseActivity implements ZRequestTags,
		UploadManagerCallback, TextWatcher, OnClickListener {

	ListView listView;
	EditText searchText;
	SearchListAdapter adapter;
	LinearLayout backButton, clearButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity_layout);

		UploadManager.getInstance().addCallback(this, this);

		searchText = (EditText) findViewById(R.id.searchtxt);
		listView = (ListView) findViewById(R.id.searchlist);
		backButton = (LinearLayout) findViewById(R.id.backbuttonsearchactivity);
		clearButton = (LinearLayout) findViewById(R.id.crossbuttonsearchactivity);

		clearButton.setVisibility(View.GONE);
		backButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);

		searchText.addTextChangedListener(this);
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_SEARCH_AUTOCOMLETE_REQUEST) {
			if (status) {
				ShopActivityObject obj = (ShopActivityObject) data;
				adapter = new SearchListAdapter(obj.getBooks(), this);
				listView.setAdapter(adapter);
			} else {

			}
		}
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_SEARCH_AUTOCOMLETE_REQUEST) {

		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() > 0) {
			sendSearchRequest(s.toString().trim());
			clearButton.setVisibility(View.VISIBLE);
		} else
			clearButton.setVisibility(View.GONE);
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	protected void onDestroy() {
		UploadManager.getInstance().removeCallback(this);
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void sendSearchRequest(String text) {
		Log.w("as", "text " + text);
		String url = ZApplication.getInstance().getBaseUrl()
				+ "autocomplete_search/";
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("search", text));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_SEARCH_AUTOCOMLETE_REQUEST, BNC_SEARCH_AUTOCOMLETE_REQUEST,
				BNC_SEARCH_AUTOCOMLETE_REQUEST, null, nameValuePairs, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backbuttonsearchactivity:
			this.finish();
			break;
		case R.id.crossbuttonsearchactivity:
			searchText.setText("");
			break;
		default:
			break;
		}
	}
}
