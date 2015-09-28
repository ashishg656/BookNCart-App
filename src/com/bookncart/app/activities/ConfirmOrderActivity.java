package com.bookncart.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.adapters.ConfirmOrderRecyclerAdapter;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.CartObject;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;

public class ConfirmOrderActivity extends BaseActivity implements ZRequestTags,
		UploadManagerCallback, OnClickListener {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	ConfirmOrderRecyclerAdapter adapter;
	CartObject mData;

	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout placeOrderLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_order_activity_layout);

		progressDarkCircle = (View) findViewById(R.id.dark_circle);
		progressLightCircle = (View) findViewById(R.id.light_circle);
		progressImage = (ImageView) findViewById(R.id.image_progress);
		progressLayoutContainer = (FrameLayout) findViewById(R.id.progresslayutcontainre);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
		placeOrderLayout = (FrameLayout) findViewById(R.id.placeorder);

		setSupportActionBar(toolbar);
		toolbarTitle.setText("Please Confirm Order");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("");
		toolbar.setBackgroundColor(getResources()
				.getColor(R.color.PrimaryColor));

		setConnectionErrorVariables();

		placeOrderLayout.setOnClickListener(this);

		recyclerView = (RecyclerView) findViewById(R.id.confrmorder_recycler_view);
		retryDataConnectionLayout.setOnClickListener(this);

		layoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);

		UploadManager.getInstance().addCallback(this, this);
		loadData();
	}

	@Override
	protected void onDestroy() {
		UploadManager.getInstance().removeCallback(this);
		super.onDestroy();
	}

	private void loadData() {
		String url = ZApplication.getInstance().getBaseUrl()
				+ "view_cart_request/";
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_CONFIRM_ORDER_REQUEST_TAG, BNC_CONFIRM_ORDER_REQUEST_TAG,
				BNC_CONFIRM_ORDER_REQUEST_TAG, null, null, null);
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_CONFIRM_ORDER_REQUEST_TAG) {
			hideMainContentLoadingAnimations();
			if (status) {
				hideConnectionErrorLayout();
				mData = (CartObject) data;
				setAdapterData();
			} else {
				showConnectionErrorLayout();
			}
		}
	}

	private void setAdapterData() {
		adapter = new ConfirmOrderRecyclerAdapter(mData, this);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_CONFIRM_ORDER_REQUEST_TAG) {
			showMainContentLoadingAnimations();
			hideConnectionErrorLayout();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.retrylayoutconnectionerror:
			loadData();
			break;
		case R.id.placeorder:
			Intent i = new Intent(this, SelectAddressActivity.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}

}
