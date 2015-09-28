package com.bookncart.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.bookncart.app.R;
import com.bookncart.app.activities.WishlistAndCartActivity;
import com.bookncart.app.adapters.WishlistRecyclerAdapter;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.ShopActivityObject;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;

public class WishlistFragment extends BaseFragment implements
		UploadManagerCallback, ZRequestTags, OnClickListener {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	WishlistRecyclerAdapter adapter;
	View mainContentView;
	ShopActivityObject mData;

	public static WishlistFragment newInstance(Bundle b) {
		WishlistFragment frg = new WishlistFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.wishlist_fragment_layout, container,
				false);
		mainContentView = v;
		recyclerView = (RecyclerView) v
				.findViewById(R.id.wishlist_recycler_view);
		setProgressLayoutVariables(v);
		setConnectionErrorVariables(v);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		retryDataConnectionLayout.setOnClickListener(this);

		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		recyclerView.addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				((WishlistAndCartActivity) getActivity()).scrollToolbarBy(-dy);
				super.onScrolled(recyclerView, dx, dy);
			}

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					int pos = layoutManager.findFirstVisibleItemPosition();
					if (pos == 0) {
						((WishlistAndCartActivity) getActivity())
								.setToolbarTranslation(recyclerView
										.getChildAt(0));
					} else
						((WishlistAndCartActivity) getActivity())
								.scrollToolbarAfterTouchEnds();
				}
				super.onScrollStateChanged(recyclerView, newState);
			}
		});
	}

	@Override
	public void onStart() {
		UploadManager.getInstance().addCallback(this, getActivity());
		loadData();
		super.onStart();
	}

	@Override
	public void onStop() {
		UploadManager.getInstance().removeCallback(this);
		super.onDestroy();
	}

	private void loadData() {
		String url = ZApplication.getInstance().getBaseUrl()
				+ "view_wishlist_request/";
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_VIEW_WISHLIST_REQUEST_TAG, BNC_VIEW_WISHLIST_REQUEST_TAG,
				BNC_VIEW_WISHLIST_REQUEST_TAG, null, null, null);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && isResumed()) {
			if (layoutManager.findFirstVisibleItemPosition() == 0) {
				((WishlistAndCartActivity) getActivity())
						.setToolbarTranslation(recyclerView.getChildAt(0));
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_VIEW_WISHLIST_REQUEST_TAG) {
			hideMainContentLoadingAnimations();
			if (status) {
				hideConnectionErrorLayout();
				mData = (ShopActivityObject) data;
				setAdapterData();
			} else {
				showConnectionErrorLayout(mainContentView);
			}
		} else if (adapter != null) {
			adapter.uploadFinished(requestType, objectId, data, uploadId,
					status, parserId);
		}
	}

	private void setAdapterData() {
		adapter = new WishlistRecyclerAdapter(mData.getBooks(), getActivity());
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_VIEW_WISHLIST_REQUEST_TAG) {
			if (getActivity() != null)
				showMainContentLoadingAnimations(getActivity());
			hideConnectionErrorLayout();
		} else if (adapter != null) {
			adapter.uploadStarted(requestType, objectId, parserId, object);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.retrylayoutconnectionerror:
			loadData();
			break;

		default:
			break;
		}
	}
}
