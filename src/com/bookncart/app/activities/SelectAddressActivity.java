package com.bookncart.app.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.bookncart.app.adapters.AddressListAdapter;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.AddressObject;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;

public class SelectAddressActivity extends BaseActivity implements
		UploadManagerCallback, OnClickListener {

	CoordinatorLayout coordinatorLayout;
	Snackbar snackbar;
	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;

	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout addAddressLayout;
	private AddressObject mData;
	AddressListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_address_activity_layout);

		setConnectionErrorVariables();

		progressDarkCircle = (View) findViewById(R.id.dark_circle);
		progressLightCircle = (View) findViewById(R.id.light_circle);
		progressImage = (ImageView) findViewById(R.id.image_progress);
		progressLayoutContainer = (FrameLayout) findViewById(R.id.progresslayutcontainre);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
		addAddressLayout = (FrameLayout) findViewById(R.id.newaddress);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinatorhome);
		recyclerView = (RecyclerView) findViewById(R.id.confrmorder_recycler_view);

		setSupportActionBar(toolbar);
		toolbarTitle.setText("Select Or Add Address");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("");
		toolbar.setBackgroundColor(getResources()
				.getColor(R.color.PrimaryColor));

		addAddressLayout.setOnClickListener(this);

		retryDataConnectionLayout.setOnClickListener(this);

		layoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);

		snackbar = Snackbar
				.make(coordinatorLayout,
						"Currently only CASH ON DELIVERY payment mode is available",
						Snackbar.LENGTH_INDEFINITE)
				.setActionTextColor(
						getResources()
								.getColor(R.color.bnc_green_color_primary))
				.setAction("Okay", new OnClickListener() {

					@Override
					public void onClick(View v) {
						snackbar.dismiss();
					}
				});
		snackbar.show();
	}

	@Override
	protected void onResume() {
		UploadManager.getInstance().addCallback(this, this);
		loadData();
		super.onResume();
	}

	@Override
	protected void onPause() {
		UploadManager.getInstance().removeCallback(this);
		super.onPause();
	}

	void loadData() {
		String url = ZApplication.getInstance().getBaseUrl()
				+ "view_addresses/";
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_VIEW_ALL_ADDRESSES_REQUEST_TAG,
				BNC_VIEW_ALL_ADDRESSES_REQUEST_TAG,
				BNC_VIEW_ALL_ADDRESSES_REQUEST_TAG, null, null, null);
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_VIEW_ALL_ADDRESSES_REQUEST_TAG) {
			hideMainContentLoadingAnimations();
			if (status) {
				hideConnectionErrorLayout();
				mData = (AddressObject) data;
				setAdapterData();
			} else {
				showConnectionErrorLayout();
			}
		}
	}

	private void setAdapterData() {
		adapter = new AddressListAdapter(this, mData);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_VIEW_ALL_ADDRESSES_REQUEST_TAG) {
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
		case R.id.newaddress:
			// Intent i = new Intent(this, SelectAddressActivity.class);
			// startActivity(i);
			break;

		default:
			break;
		}
	}
}
