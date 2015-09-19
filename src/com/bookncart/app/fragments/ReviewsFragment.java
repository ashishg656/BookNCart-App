package com.bookncart.app.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.activities.BookDetailActivity;
import com.bookncart.app.adapters.ReviewsListAdapter;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.ReviewObject;
import com.bookncart.app.baseobjects.ReviewObject.reviewDEtail;
import com.bookncart.app.extras.MyAnimatorListener;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;

public class ReviewsFragment extends BaseFragment implements OnClickListener,
		ZRequestTags, UploadManagerCallback {

	View mainContentView;
	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	ReviewObject mData;
	ReviewsListAdapter adapter;
	MyTouchListener touchListener;
	FrameLayout containerLayout;
	LinearLayout recyclerViewContainerLayout;
	float downY;
	int maxTranslationUpDuration = 600;
	LinearLayout toolbarLayout, writeReviewBar;
	float maxRecyclerViewHeight;
	LinearLayout backButton;
	TextView toolbarTitle;
	ProgressBar progressBar;
	LinearLayout loadingLayout;

	private VelocityTracker mVelocityTracker = null;
	private float minFlingVelocity;

	double minimumRecyclerViewHeight;
	double minumumTranslationForRecyclerContainer;

	int bookId;

	boolean isEndAnimationRunning = false;

	public static ReviewsFragment newInstance(Bundle b) {
		ReviewsFragment frg = new ReviewsFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainContentView = inflater.inflate(R.layout.review_fragment_layout,
				container, false);

		recyclerView = (RecyclerView) mainContentView
				.findViewById(R.id.revirerecyclerF);
		recyclerViewContainerLayout = (LinearLayout) mainContentView
				.findViewById(R.id.ecetcelrviewcontainer);
		containerLayout = (FrameLayout) mainContentView
				.findViewById(R.id.contane);
		toolbarLayout = (LinearLayout) mainContentView
				.findViewById(R.id.toolbarfakereviewfrag);
		writeReviewBar = (LinearLayout) mainContentView
				.findViewById(R.id.writereviewbar);
		backButton = (LinearLayout) mainContentView
				.findViewById(R.id.backbuttonfake2);
		toolbarTitle = (TextView) mainContentView
				.findViewById(R.id.numberoferviews);
		progressBar = (ProgressBar) mainContentView
				.findViewById(R.id.progressbar);
		loadingLayout = (LinearLayout) mainContentView
				.findViewById(R.id.loadinglayout);
		setConnectionErrorVariables(mainContentView);

		return mainContentView;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		bookId = getArguments().getInt("bookid");

		retryDataConnectionLayout.setOnClickListener(this);

		touchListener = new MyTouchListener();
		minimumRecyclerViewHeight = getActivity().getResources()
				.getDisplayMetrics().heightPixels * 0.4;
		minumumTranslationForRecyclerContainer = getActivity().getResources()
				.getDisplayMetrics().heightPixels * 0.4;

		ViewConfiguration viewConfiguration = ViewConfiguration
				.get(getActivity());
		minFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity() * 10;

		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		containerLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							containerLayout.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						} else {
							containerLayout.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
							maxRecyclerViewHeight = containerLayout.getHeight()
									- 2
									* getActivity().getResources()
											.getDimensionPixelSize(
													R.dimen.bnc_button_height);
						}
					}
				});

		writeReviewBar.setOnClickListener(this);

		recyclerView.setOnTouchListener(touchListener);

		int color = ((BookDetailActivity) getActivity()).darkColor;
		writeReviewBar.setBackgroundColor(color);
		toolbarLayout.setBackgroundColor(color);

		loadData();
	}

	private void loadData() {
		String url = ZApplication.getInstance().getBaseUrl() + "view_reviews/";
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("bookid", bookId + ""));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_VIEW_REVIEWS_REQUEST_TAG, BNC_VIEW_REVIEWS_REQUEST_TAG,
				BNC_VIEW_REVIEWS_REQUEST_TAG, null, nameValuePairs, null);
	}

	class MyTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int index = event.getActionIndex();
			int pointerId = event.getPointerId(index);

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				downY = event.getRawY();

				if (mVelocityTracker == null)
					mVelocityTracker = VelocityTracker.obtain();
				else
					mVelocityTracker.clear();
				mVelocityTracker.addMovement(event);
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				mVelocityTracker.addMovement(event);

				float deltaY = downY - event.getRawY();
				if (layoutManager.findLastCompletelyVisibleItemPosition() == (adapter
						.getItemCount() - 1)) {
					if (deltaY > 0 || containerLayout.getTranslationY() != 0) {
						float translationY = -deltaY;
						if (translationY > 0)
							translationY = 0;
						float temp = Math.abs(translationY);
						if (temp > minumumTranslationForRecyclerContainer) {
							dismissContainerViewUpwards();
							return false;
						}
						containerLayout.setTranslationY(translationY);
						return true;
					}
				}
				if (layoutManager.findFirstCompletelyVisibleItemPosition() <= 0) {
					if (deltaY < 0
							|| recyclerView.getLayoutParams().height < maxRecyclerViewHeight) {
						reduceSizeOfRecyclerView(-deltaY);
						return true;
					}
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);

				float yVelocity = VelocityTrackerCompat.getYVelocity(
						mVelocityTracker, pointerId);
				int firstVisibleItem = layoutManager
						.findFirstCompletelyVisibleItemPosition();
				int lastVisibleItem = layoutManager
						.findLastCompletelyVisibleItemPosition();
				int itemCount = adapter.getItemCount() - 1;

				if (Math.abs(yVelocity) < minFlingVelocity) {
					if (firstVisibleItem <= 0 && lastVisibleItem == itemCount) {
						if (containerLayout.getTranslationY() != 0) {
							float temp = Math.abs(containerLayout
									.getTranslationY());
							double diff = minumumTranslationForRecyclerContainer;
							if (temp < diff / 2) {
								setRecyclerViewBackToBotton();
							} else {
								dismissContainerViewUpwards();
							}
						} else {
							double diff = containerLayout.getHeight()
									- minimumRecyclerViewHeight;
							if (recyclerView.getLayoutParams().height > minimumRecyclerViewHeight
									+ diff / 2) {
								makeRecyclerViewHeightDeafult();
							} else {
								swipeDownAndDismissContainer();
							}
						}
					} else if (lastVisibleItem == itemCount) {
						float temp = Math
								.abs(containerLayout.getTranslationY());
						double diff = minumumTranslationForRecyclerContainer;
						if (temp < diff / 2) {
							setRecyclerViewBackToBotton();
						} else {
							dismissContainerViewUpwards();
						}
					} else if (firstVisibleItem <= 0) {
						double diff = containerLayout.getHeight()
								- minimumRecyclerViewHeight;
						if (recyclerView.getLayoutParams().height > minimumRecyclerViewHeight
								+ diff / 2) {
							makeRecyclerViewHeightDeafult();
						} else {
							swipeDownAndDismissContainer();
						}
					}
				} else {
					if (firstVisibleItem <= 0 && lastVisibleItem == itemCount) {
						if (yVelocity < 0) {
							dismissContainerViewUpwards();
						} else if (yVelocity > 0) {
							swipeDownAndDismissContainer();
						}
					} else if (lastVisibleItem == itemCount) {
						dismissContainerViewUpwards();
					} else if (firstVisibleItem <= 0) {
						swipeDownAndDismissContainer();
					}
				}
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				mVelocityTracker.recycle();
			}
			return false;
		}
	}

	public void swipeDownAndDismissContainer() {
		if (!isEndAnimationRunning) {
			isEndAnimationRunning = true;
			recyclerView.setOnTouchListener(null);
			ObjectAnimator anim = ObjectAnimator.ofFloat(
					recyclerViewContainerLayout, "translationY",
					recyclerViewContainerLayout.getHeight());
			anim.setDuration(400);
			anim.setInterpolator(new AccelerateDecelerateInterpolator());
			anim.addListener(new MyAnimatorListener() {
				@Override
				public void onAnimationEnd(Animator animation) {
					isEndAnimationRunning = false;
					destroyFragment();
				}
			});
			anim.start();
		}
	}

	@SuppressLint("NewApi")
	public void dismissContainerViewUpwards() {
		if (!isEndAnimationRunning) {
			isEndAnimationRunning = true;
			recyclerView.setOnTouchListener(null);
			float ratio = 1 - Math.abs(containerLayout.getTranslationY()
					/ containerLayout.getHeight());
			float time = ratio * maxTranslationUpDuration;
			ObjectAnimator anim = ObjectAnimator.ofFloat(containerLayout,
					"translationY", -containerLayout.getHeight());
			anim.setDuration((long) time);
			anim.setInterpolator(new AccelerateDecelerateInterpolator());
			anim.addListener(new MyAnimatorListener() {
				@Override
				public void onAnimationEnd(Animator animation) {
					isEndAnimationRunning = false;
					destroyFragment();
				}
			});
			anim.start();
		}
	}

	public void reduceSizeOfRecyclerView(float deltaY) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) recyclerView
				.getLayoutParams();
		params.height = (int) (maxRecyclerViewHeight - deltaY);
		if (params.height < minimumRecyclerViewHeight) {
			swipeDownAndDismissContainer();
		}
		recyclerView.setLayoutParams(params);
	}

	void setRecyclerViewBackToBotton() {
		recyclerView.setOnTouchListener(null);
		float ratio = 1 - Math.abs(containerLayout.getTranslationY()
				/ containerLayout.getHeight());
		float time = ratio * maxTranslationUpDuration;
		ObjectAnimator anim = ObjectAnimator.ofFloat(containerLayout,
				"translationY", 0);
		anim.setDuration((long) time);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		anim.addListener(new MyAnimatorListener() {
			@Override
			public void onAnimationEnd(Animator animation) {
				recyclerView.setOnTouchListener(touchListener);
			}
		});
		anim.start();
	}

	void makeRecyclerViewHeightDeafult() {
		recyclerView.setOnTouchListener(null);
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) recyclerView
				.getLayoutParams();
		params.height = (int) maxRecyclerViewHeight;
		recyclerView.setLayoutParams(params);
		recyclerView.setOnTouchListener(touchListener);
	}

	void destroyFragment() {
		if (getActivity() != null)
			getActivity().onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.writereviewbar:
			((BookDetailActivity) getActivity())
					.openWriteBookReviewFragment(bookId);
			break;
		case R.id.retrylayoutconnectionerror:
			loadData();
			break;

		default:
			break;
		}
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_VIEW_REVIEWS_REQUEST_TAG) {
			loadingLayout.setVisibility(View.GONE);
			progressBar.setVisibility(View.GONE);
			if (status) {
				hideConnectionErrorLayout();
				mData = (ReviewObject) data;
				adapter = new ReviewsListAdapter(getActivity(), mData);
				recyclerView.setAdapter(adapter);
				toolbarTitle.setText(mData.getReviews().size() + " reviews");
			} else {
				showConnectionErrorLayout(mainContentView);
			}
		}
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_VIEW_REVIEWS_REQUEST_TAG) {
			loadingLayout.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.VISIBLE);
		}
	}
}
