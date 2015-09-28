package com.bookncart.app.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookncart.app.R;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.LogoutObject;
import com.bookncart.app.baseobjects.UserProfileObject;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.notboringactionbar.AlphaForegroundColorSpan;
import com.bookncart.app.notboringactionbar.KenBurnsSupportView;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.serverApi.ImageRequestManager;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;
import com.bookncart.app.widgets.CircularImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

public class UserProfileActivity extends BaseActivity implements
		UploadManagerCallback, ZRequestTags, OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener {

	private int mActionBarTitleColor;
	private int mActionBarHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;
	private ListView mListView;
	private KenBurnsSupportView mHeaderPicture;
	private CircularImageView mHeaderLogo;
	private View mHeader;
	private View mPlaceHolderView;
	private AccelerateDecelerateInterpolator mSmoothInterpolator;

	private RectF mRect1 = new RectF();
	private RectF mRect2 = new RectF();

	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private SpannableString mSpannableString;

	LinearLayout toolbar;
	TextView toolbarTitle;
	TextView userNameText;

	UserProfileObject mData;

	ProgressDialog progressDialog;

	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile_activity_layout);

		mSmoothInterpolator = new AccelerateDecelerateInterpolator();
		mHeaderHeight = getResources().getDisplayMetrics().heightPixels / 2;
		mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();

		setConnectionErrorVariables();

		retryDataConnectionLayout.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.listview);
		toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
		toolbar = (LinearLayout) findViewById(R.id.toolbar);
		mHeader = findViewById(R.id.header);
		mHeaderPicture = (KenBurnsSupportView) findViewById(R.id.header_picture);
		mHeaderLogo = (CircularImageView) findViewById(R.id.header_logo);
		progressDarkCircle = (View) findViewById(R.id.dark_circle);
		progressLightCircle = (View) findViewById(R.id.light_circle);
		progressImage = (ImageView) findViewById(R.id.image_progress);
		progressLayoutContainer = (FrameLayout) findViewById(R.id.progresslayutcontainre);
		userNameText = (TextView) findViewById(R.id.usernameuserprofiletext);

		findViewById(R.id.backbuttonprofile).setOnClickListener(this);

		mActionBarTitleColor = getResources().getColor(R.color.bnc_white);

		mSpannableString = new SpannableString("BookNCart");
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(
				mActionBarTitleColor);

		UploadManager.getInstance().addCallback(this, this);

		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mHeader
				.getLayoutParams();
		params.height = getResources().getDisplayMetrics().heightPixels / 2;
		mHeader.setLayoutParams(params);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(new Scope(Scopes.PROFILE)).build();

		loadData();
	}

	private void loadData() {
		String url = ZApplication.getInstance().getBaseUrl() + "user_profile/";
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_USER_PROFILE_REQUEST_TAG, BNC_USER_PROFILE_REQUEST_TAG,
				BNC_USER_PROFILE_REQUEST_TAG, null, null, null);
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

	private void setupListView() {
		String[] userProfileListItems = getResources().getStringArray(
				R.array.user_profile_list_items);
		mPlaceHolderView = getLayoutInflater().inflate(
				R.layout.placeholder_header_user_profile, mListView, false);
		AbsListView.LayoutParams params = (LayoutParams) mPlaceHolderView
				.getLayoutParams();
		params.height = getResources().getDisplayMetrics().heightPixels / 2;
		mPlaceHolderView.setLayoutParams(params);
		mListView.addHeaderView(mPlaceHolderView);

		mListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, userProfileListItems));
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int scrollY = getScrollY();
				mHeader.setTranslationY(Math.max(-scrollY,
						mMinHeaderTranslation));
				float ratio = clamp(mHeader.getTranslationY()
						/ mMinHeaderTranslation, 0.0f, 1.0f);
				interpolate(mHeaderLogo, getActionBarIconView(),
						mSmoothInterpolator.getInterpolation(ratio));
				userNameText.setAlpha(1 - 3 * ratio);
				setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));

				mHeaderPicture.setTranslationY(Math.max(scrollY / 3,
						mMinHeaderTranslation));
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mListView.getItemAtPosition(position).equals(
						getResources().getString(
								R.string.bnc_profile_logout_string))) {
					sendLogOutRequestToServer();
				}
			}

		});
	}

	protected void sendLogOutRequestToServer() {
		String url = ZApplication.getInstance().getBaseUrl() + "logout_view/";
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_LOGOUT_REQUEST_TAG, BNC_LOGOUT_REQUEST_TAG,
				BNC_LOGOUT_REQUEST_TAG, null, null, null);
	}

	private void setTitleAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0,
				mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		toolbarTitle.setText(mSpannableString);
	}

	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(value, max));
	}

	private void interpolate(View view1, View view2, float interpolation) {
		getOnScreenRect(mRect1, view1);
		getOnScreenRect(mRect2, view2);

		float scaleX = 1.0F + interpolation
				* (mRect2.width() / mRect1.width() - 1.0F);
		float scaleY = 1.0F + interpolation
				* (mRect2.height() / mRect1.height() - 1.0F);
		float translationX = 0.5F * (interpolation * (mRect2.left
				+ mRect2.right - mRect1.left - mRect1.right));
		float translationY = 0.5F * (interpolation * (mRect2.top
				+ mRect2.bottom - mRect1.top - mRect1.bottom));

		view1.setTranslationX(translationX);
		view1.setTranslationY(translationY - mHeader.getTranslationY());
		view1.setScaleX(scaleX);
		view1.setScaleY(scaleY);
	}

	private RectF getOnScreenRect(RectF rect, View view) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(),
				view.getBottom());
		return rect;
	}

	public int getScrollY() {
		View c = mListView.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = mListView.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mPlaceHolderView.getHeight();
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	private ImageView getActionBarIconView() {
		return (ImageView) findViewById(R.id.actionbariconview);
	}

	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}
		mActionBarHeight = getResources().getDimensionPixelSize(
				R.dimen.bnc_toolbar_height);
		return mActionBarHeight;
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_USER_PROFILE_REQUEST_TAG) {
			hideMainContentLoadingAnimations();
			if (status) {
				hideConnectionErrorLayout();
				mData = (UserProfileObject) data;
				fillDataForUserProfile();
			} else {
				showConnectionErrorLayout();
			}
		} else if (requestType == BNC_LOGOUT_REQUEST_TAG) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (status) {
				LogoutObject obj = (LogoutObject) data;
				if (obj.isStatus()) {
					logOutUserFromApp();
				} else {
					logOutUserFromApp();
				}
			} else {
				Toast.makeText(this, "Unable to logout..Try again",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		UploadManager.getInstance().removeCallback(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		mGoogleApiClient.connect();
		super.onStart();
	}

	@Override
	protected void onStop() {
		if (mGoogleApiClient.isConnected())
			mGoogleApiClient.disconnect();
		super.onStop();
	}

	private void logOutUserFromApp() {
		ZPreferences.setIsUserLogin(this, false);
		ZPreferences.setIsGcmRegistered(this, false);
		ZPreferences.setWishlistCount(this, 0);
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}
		Intent i = new Intent(this, LaunchActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		UserProfileActivity.this.finish();
	}

	private void fillDataForUserProfile() {
		setupListView();

		userNameText.setText(mData.getFull_name());

		mSpannableString = new SpannableString(mData.getFull_name());
		toolbarTitle.setText(mSpannableString);

		mHeaderPicture.setResourceIds(mData.getBackground_images().get(0)
				.getBackground_image_1(), mData.getBackground_images().get(0)
				.getBackground_image_2());

		ImageRequestManager.get(this).requestImage(this, mHeaderLogo,
				mData.getProfile_image(), -1);
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_USER_PROFILE_REQUEST_TAG) {
			showMainContentLoadingAnimations();
		} else if (requestType == BNC_LOGOUT_REQUEST_TAG) {
			progressDialog = ProgressDialog.show(this, "LogOut",
					"Please wait..logging you out.", true, false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backbuttonprofile:
			finish();
			break;
		case R.id.retrylayoutconnectionerror:
			loadData();
			break;
		default:
			break;
		}
	}

	@Override
	public void onConnected(Bundle arg0) {

	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!ZPreferences.isUserLogIn(this)) {
			Intent i = new Intent(this, LaunchActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			UserProfileActivity.this.finish();
		}
	}
}