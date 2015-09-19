package com.bookncart.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.adapters.RecentlyViewedBooksRecyclerAdapter;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.ShopActivityObject;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.extras.RecyclerViewSwipeToRemoveCallback;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;

public class RecentlyViewedBooksActivity extends BaseActivity implements
		AppConstants, OnClickListener, ZRequestTags, UploadManagerCallback {

	RecyclerView recyclerView;
	GridLayoutManager gridLayoutManager;
	RecentlyViewedBooksRecyclerAdapter adapter;

	// request params
	Integer nextPageNumber = 1;
	boolean isMoreAllowed;
	boolean isRequestRunning = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recently_viewed_books_activity);

		recyclerView = (RecyclerView) findViewById(R.id.recent_recycler_view);
		progressDarkCircle = (View) findViewById(R.id.dark_circle);
		progressLightCircle = (View) findViewById(R.id.light_circle);
		progressImage = (ImageView) findViewById(R.id.image_progress);
		progressLayoutContainer = (FrameLayout) findViewById(R.id.progresslayutcontainre);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setBackgroundColor(getResources()
				.getColor(R.color.PrimaryColor));

		UploadManager.getInstance().addCallback(this, this);

		toolbarTitle.setText("Recently Viewed Books");

		gridLayoutManager = new GridLayoutManager(this, 2);
		gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {

			@Override
			public int getSpanSize(int arg0) {
				if (adapter.mData.get(arg0).getImage_url()
						.equals(loadMoreString))
					return 2;
				else
					return 1;
			}
		});
		recyclerView.setLayoutManager(gridLayoutManager);

		recyclerView.addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				// scrollToolbarBy(-dy);
				super.onScrolled(recyclerView, dx, dy);
				int pos = gridLayoutManager.findLastVisibleItemPosition();
				if (pos > recyclerView.getAdapter().getItemCount() - 5) {
					if (isMoreAllowed && !isRequestRunning) {
						loadData();
					}
				}
			}

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					// if (pos == 0) {
					// setToolbarTranslation(recyclerView.getChildAt(0));
					// } else
					// scrollToolbarAfterTouchEnds();
				}
				super.onScrollStateChanged(recyclerView, newState);
			}
		});

		loadData();
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

	@Override
	protected void onDestroy() {
		UploadManager.getInstance().removeCallback(this);
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void loadData() {
		String url = ZApplication.getInstance().getBaseUrl()
				+ "recently_viewed_books/";
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("pagenumber", Integer
				.toString(nextPageNumber)));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_RECENT_VIEWED_BOOKS, BNC_RECENT_VIEWED_BOOKS,
				BNC_RECENT_VIEWED_BOOKS, null, nameValuePairs, null);
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_RECENT_VIEWED_BOOKS) {
			isRequestRunning = false;
			hideMainContentLoadingAnimations();
			if (status) {
				if (adapter == null) {
					ShopActivityObject obj = (ShopActivityObject) data;
					if (obj.getNext_page() == null) {
						nextPageNumber = null;
						isMoreAllowed = false;
					} else {
						nextPageNumber = obj.getNext_page();
						isMoreAllowed = true;
					}
					adapter = new RecentlyViewedBooksRecyclerAdapter(
							obj.getBooks(), this, isMoreAllowed);
					recyclerView.setAdapter(adapter);

					ItemTouchHelper.Callback callback = new RecyclerViewSwipeToRemoveCallback(
							adapter);
					ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
					touchHelper.attachToRecyclerView(recyclerView);
				} else {
					ShopActivityObject obj = (ShopActivityObject) data;
					if (obj.getNext_page() == null) {
						nextPageNumber = null;
						isMoreAllowed = false;
					} else {
						nextPageNumber = obj.getNext_page();
						isMoreAllowed = true;
					}
					adapter.addData(obj.getBooks(), isMoreAllowed);
				}
			} else {
				Log.w("as", "req failed");
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void deleteBookFromRecentList(int id) {
		String url = ZApplication.getInstance().getBaseUrl()
				+ "delete_recent_viewed_book/";
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("book_id", Integer
				.toString(id)));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_DELETE_RECENT_VIEWED_BOOK_TAG,
				BNC_DELETE_RECENT_VIEWED_BOOK_TAG,
				BNC_DELETE_RECENT_VIEWED_BOOK_TAG, null, nameValuePairs, null);
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_RECENT_VIEWED_BOOKS) {
			isRequestRunning = true;
			if (adapter == null) {
				showMainContentLoadingAnimations();
			}
		}
	}

	@Override
	public void onClick(View v) {

	}
}
