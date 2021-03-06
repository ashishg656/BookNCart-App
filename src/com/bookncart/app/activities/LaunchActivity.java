package com.bookncart.app.activities;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.accounts.Account;
import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.bookncart.app.R;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.LoginObject;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.fragments.LaunchScreen1Fragment;
import com.bookncart.app.fragments.LaunchScreen4Fragment;
import com.bookncart.app.gcm.RegistrationIntentService;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;
import com.bookncart.app.utils.CommonLib;
import com.bookncart.app.utils.JSONUtils;
import com.bookncart.app.widgets.CirclePageIndicator;
import com.bookncart.app.widgets.ParallaxViewPagerTransformer;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

@SuppressLint("NewApi")
public class LaunchActivity extends BaseActivity implements
		OnPageChangeListener, OnClickListener,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		ResultCallback<LoadPeopleResult>, UploadManagerCallback, ZRequestTags {

	ViewPager viewPager;
	ArgbEvaluator argbEvaluator;
	FrameLayout mainContainerLayout;
	ArrayList<Integer> colors = new ArrayList<Integer>();
	MyPagerAdapter adapter;
	ImageView launchIcon;
	int deviceHeight;
	CirclePageIndicator pageIndicator;
	ImageView gradientBg, skipButtonBg;
	LinearLayout loginButtonsContainerLayout, loginButtonsLayout;
	int loginButtonsLayoutHeight, skipButtonHeight;
	RelativeLayout skipButtonLayout;
	TextView skipButton;
	Button googleLoginButton, facebookLoginButton;
	ProgressDialog progressDialog;

	// GOOGLE API
	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;
	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;
	/* Is there a ConnectionResult resolution in progress? */
	private boolean mIsResolving = false;
	/* Should we automatically resolve ConnectionResults when possible? */
	private boolean mShouldResolve = false;
	private static final int PROFILE_PIC_SIZE = 400;

	// FACEBOOK
	CallbackManager callbackManager;

	// DATA TO SEND
	String emailToSend, idToSend, imageUrlToSend, nameToSend,
			accessTokenToSend, additionalDataToSend;
	boolean isGoogleAccountToSend;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private BroadcastReceiver mRegistrationBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());

		setContentView(R.layout.launch_activity_layout);

		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (!ZPreferences.isGcmRegistered(LaunchActivity.this)) {
					Toast.makeText(LaunchActivity.this,
							"Please enable internet connection",
							Toast.LENGTH_SHORT).show();
				}
			}
		};

		if (!checkPlayServices())
			return;

		Intent intent = new Intent(this, RegistrationIntentService.class);
		startService(intent);

		viewPager = (ViewPager) findViewById(R.id.pager_launch);
		skipButton = (TextView) findViewById(R.id.launch_skip_text);
		mainContainerLayout = (FrameLayout) findViewById(R.id.launch_activity_main);
		launchIcon = (ImageView) findViewById(R.id.ic_app_icon_launch);
		pageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator);
		gradientBg = (ImageView) findViewById(R.id.gradient_bg_launch);
		skipButtonBg = (ImageView) findViewById(R.id.skip_button_bg);
		loginButtonsContainerLayout = (LinearLayout) findViewById(R.id.indicatorandbuttonslayout);
		loginButtonsLayout = (LinearLayout) findViewById(R.id.login_buttons);
		skipButtonLayout = (RelativeLayout) findViewById(R.id.skipbuttonlayout);
		googleLoginButton = (Button) findViewById(R.id.google_sign_in_button);
		facebookLoginButton = (Button) findViewById(R.id.facebook_login_button);

		facebookLoginButton.setOnClickListener(this);

		progressDialog = new ProgressDialog(this);
		progressDialog.dismiss();

		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			Scroller scroller = new Scroller(this,
					new DecelerateInterpolator(), true);
			mScroller.set(viewPager, scroller);
		} catch (Exception e) {
		}

		viewPager.setPageTransformer(true, new ParallaxViewPagerTransformer(
				R.id.imagelaunch));

		loginButtonsLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							mainContainerLayout.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						} else {
							mainContainerLayout.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						}
						loginButtonsLayoutHeight = loginButtonsLayout
								.getHeight();
						skipButtonHeight = skipButtonLayout.getHeight();
					}
				});

		mainContainerLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							mainContainerLayout.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						} else {
							mainContainerLayout.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						}
						deviceHeight = mainContainerLayout.getHeight();
					}
				});

		argbEvaluator = new ArgbEvaluator();
		colors.add(getResources().getColor(R.color.home_viewpager_color_3));
		colors.add(getResources().getColor(R.color.home_viewpager_color_2));
		colors.add(getResources().getColor(R.color.home_viewpager_color_1));
		colors.add(getResources().getColor(R.color.home_viewpager_color_4));

		pageIndicator.setOnPageChangeListener(this);
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		pageIndicator.setViewPager(viewPager);

		skipButton.setOnClickListener(this);
		googleLoginButton.setOnClickListener(this);

		ZPreferences.setIsUserLogin(this, false);

		initialiseGoogleApiClient();

		callbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {

					@Override
					public void onSuccess(LoginResult result) {
						if (!ZPreferences.isGcmRegistered(LaunchActivity.this)) {
							Intent intent = new Intent(LaunchActivity.this,
									RegistrationIntentService.class);
							startService(intent);
						}

						if (progressDialog != null)
							progressDialog.dismiss();
						if (AccessToken.getCurrentAccessToken()
								.getPermissions().contains("email")) {
							getFacebookUserDetails(result.getAccessToken());
						} else {
							Toast.makeText(LaunchActivity.this,
									"Please allow email permission",
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onError(FacebookException error) {
						if (progressDialog != null)
							progressDialog.dismiss();
						Toast.makeText(LaunchActivity.this, error.getMessage(),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel() {
						if (progressDialog != null)
							progressDialog.dismiss();
					}
				});

		UploadManager.getInstance().addCallback(this, this);
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(
						this,
						"This device doesn't support Play services, App will not work normally",
						Toast.LENGTH_LONG).show();
				finish();
			}
			return false;
		}
		return true;
	}

	private void getFacebookUserDetails(final AccessToken accessToken) {
		GraphRequest request = GraphRequest.newMeRequest(accessToken,
				new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object,
							GraphResponse response) {
						System.out.println(object);
						if (progressDialog != null)
							progressDialog.dismiss();
						idToSend = JSONUtils.getStringfromJSON(object, "id");
						nameToSend = JSONUtils
								.getStringfromJSON(object, "name");
						emailToSend = JSONUtils.getStringfromJSON(object,
								"email");

						JSONObject temp1 = JSONUtils.getJSONObject(object,
								"picture");
						JSONObject temp2 = JSONUtils.getJSONObject(temp1,
								"data");
						imageUrlToSend = JSONUtils.getStringfromJSON(temp2,
								"url");

						accessTokenToSend = accessToken.getToken();

						additionalDataToSend = object.toString()
								+ " ---  and  --  " + response.toString();

						isGoogleAccountToSend = false;

						makeLoginRequestToServer();
					}
				});

		progressDialog = ProgressDialog.show(this, null,
				"Verifying info. Please wait..");
		Bundle parameters = new Bundle();
		parameters.putString("fields",
				"id,name,link,email,friends,picture.height(721)");
		request.setParameters(parameters);
		request.executeAsync();
	}

	@SuppressWarnings("deprecation")
	protected void makeLoginRequestToServer() {
		if (ZPreferences.isGcmRegistered(this)) {
			String url = ZApplication.getInstance().getBaseUrl()
					+ "login_request/";
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("access_token",
					accessTokenToSend));
			nameValuePairs.add(new BasicNameValuePair("user_id", idToSend));
			nameValuePairs.add(new BasicNameValuePair("additional_data",
					additionalDataToSend));
			nameValuePairs.add(new BasicNameValuePair("email", emailToSend));
			nameValuePairs.add(new BasicNameValuePair("name", nameToSend));
			nameValuePairs.add(new BasicNameValuePair("image_url",
					imageUrlToSend));
			nameValuePairs.add(new BasicNameValuePair("is_google_login",
					Boolean.toString(isGoogleAccountToSend)));
			nameValuePairs.add(new BasicNameValuePair("device_id", ZPreferences
					.getDeviceID(this)));
			nameValuePairs.add(new BasicNameValuePair("gcm_token", ZPreferences
					.getGcmToken(this)));
			UploadManager.getInstance().makeAyncRequest(url,
					BNC_LOGIN_REQUEST_TAG, BNC_LOGIN_REQUEST_TAG,
					BNC_LOGIN_REQUEST_TAG, null, nameValuePairs, null);
		} else {
			Intent intent = new Intent(this, RegistrationIntentService.class);
			startService(intent);
			Toast.makeText(
					this,
					"GCM Registration not available.Check internet connection and login again",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onStart() {
		mGoogleApiClient.connect();
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(this);
		checkPlayServices();
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mRegistrationBroadcastReceiver,
				new IntentFilter(CommonLib.GCM_TAG));
	}

	@Override
	protected void onDestroy() {
		UploadManager.getInstance().removeCallback(this);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mRegistrationBroadcastReceiver);
	}

	@Override
	protected void onStop() {
		if (mGoogleApiClient.isConnected())
			mGoogleApiClient.disconnect();
		super.onStop();
	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			Bundle bundle = new Bundle();
			bundle.putInt("position", pos);
			switch (pos) {
			case 3:
				return LaunchScreen4Fragment.newInstance(bundle);

			default:
				return LaunchScreen1Fragment.newInstance(bundle);
			}
		}

		@Override
		public int getCount() {
			return colors.size();
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (position < (adapter.getCount() - 1)
				&& position < (colors.size() - 1)) {
			mainContainerLayout.setBackgroundColor((Integer) argbEvaluator
					.evaluate(positionOffset, colors.get(position),
							colors.get(position + 1)));
		} else {
			mainContainerLayout
					.setBackgroundColor(colors.get(colors.size() - 1));
		}
		gradientBg.setImageAlpha(0);
		skipButtonBg.setImageAlpha(0);
		if ((viewPager.getCurrentItem() == 0 && position == 0)
				|| (viewPager.getCurrentItem() == 1 && position == 0)) {
			translateLauncherIconUp(positionOffset);
			scaleLauncherIcon(positionOffset);
			fadeLauncherIcon(positionOffset);
			translateLoginButtons(positionOffset);
			translateSkipButton(positionOffset);
		} else if ((viewPager.getCurrentItem() == 2 && position == 2)
				|| (viewPager.getCurrentItem() == 3 && position == 2 && positionOffset != 0)) {
			fadeSkipButtonAndLastFragmentBg(positionOffset);
			translateLauncherIconUp(1 - positionOffset);
			fadeLauncherIcon(1 - positionOffset);
			scaleLauncherIcon(1 - positionOffset);
			translateLoginButtons(1 - positionOffset);
			translateSkipButton(1 - positionOffset);
		} else if ((viewPager.getCurrentItem() == 2 && position == 2)
				|| (viewPager.getCurrentItem() == 3 && position == 3)) {
			gradientBg.setImageAlpha(255);
			skipButtonBg.setImageAlpha(255);
		} else if (viewPager.getCurrentItem() == 2 && position == 3) {
			gradientBg.setImageAlpha(255);
			skipButtonBg.setImageAlpha(255);
		}
	}

	private void translateSkipButton(float positionOffset) {
		float trans = positionOffset * skipButtonHeight * -1;
		skipButtonLayout.setTranslationY(trans);
	}

	private void translateLoginButtons(float positionOffset) {
		float trans = positionOffset * loginButtonsLayoutHeight;
		loginButtonsContainerLayout.setTranslationY(trans);
	}

	private void scaleLauncherIcon(float positionOffset) {
		if (positionOffset <= 0.5) {
			launchIcon.setScaleX(1 - positionOffset);
			launchIcon.setScaleY(1 - positionOffset);
		} else {
			launchIcon.setScaleX(0.5f);
			launchIcon.setScaleY(0.5f);
		}
	}

	private void fadeSkipButtonAndLastFragmentBg(float positionOffset) {
		int fade = (int) (positionOffset * 255);
		gradientBg.setImageAlpha(fade);
		skipButtonBg.setImageAlpha(fade);
	}

	private void fadeLauncherIcon(float positionOffset) {
		if (positionOffset >= 0.5) {
			float fade = (float) ((((positionOffset - 0.5) * (0 - 255)) / (1 - 0.5)) + 255);
			launchIcon.setImageAlpha((int) fade);
		} else {
			launchIcon.setImageAlpha(255);
		}
	}

	private void translateLauncherIconUp(float positionOffset) {
		if (positionOffset >= 0.5)
			positionOffset = 0.5f;
		float trans = (deviceHeight - getResources().getDimensionPixelSize(
				R.dimen.bnc_launch_app_icon_margin))
				* positionOffset * -1;
		launchIcon.setTranslationY(trans);
	}

	@Override
	public void onPageSelected(int pos) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.launch_skip_text:
			Intent i = new Intent(this, HomeActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			break;
		case R.id.google_sign_in_button:
			onGoogleSignInClicked();
			break;
		case R.id.facebook_login_button:
			LoginManager.getInstance().logInWithReadPermissions(this,
					Arrays.asList("public_profile", "email"));
			progressDialog = ProgressDialog.show(this, null,
					"Retrieving facebook info. Please Wait..");
			break;

		default:
			break;
		}
	}

	private void onGoogleSignInClicked() {
		mShouldResolve = true;
		mGoogleApiClient.connect();

		// Show a message to the user that we are signing in.
		// mStatusTextView.setText(R.string.signing_in);
		progressDialog = ProgressDialog.show(this, "Google Login",
				"Getting Google login details. Please wait..", true, false);
	}

	private void initialiseGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(new Scope(Scopes.PROFILE)).build();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mShouldResolve = false;
				if (progressDialog != null)
					progressDialog.dismiss();
			}

			mIsResolving = false;
			mGoogleApiClient.connect();
			if (!progressDialog.isShowing())
				progressDialog = ProgressDialog
						.show(this, "Google Login",
								"Logging in through Google..Please Wait..",
								true, false);
		} else {
			// facebook login
			callbackManager.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (!mIsResolving && mShouldResolve) {
			if (connectionResult.hasResolution()) {
				try {
					if (progressDialog != null)
						progressDialog.dismiss();
					connectionResult.startResolutionForResult(this, RC_SIGN_IN);
					mIsResolving = true;
				} catch (IntentSender.SendIntentException e) {
					progressDialog = ProgressDialog.show(this, null,
							"Getting user account details");
					mIsResolving = false;
					mGoogleApiClient.connect();
				}
			} else {
				// Could not resolve the connection result, show the user an
				// error dialog.
				// showErrorDialog(connectionResult);
				Toast.makeText(
						this,
						"Login error...Please try again "
								+ connectionResult.describeContents(),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			if (progressDialog != null)
				progressDialog.dismiss();
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		mShouldResolve = false;

		// Show the signed-in UI
		// showSignedInUI();

		if (!ZPreferences.isGcmRegistered(LaunchActivity.this)) {
			Intent intent = new Intent(LaunchActivity.this,
					RegistrationIntentService.class);
			startService(intent);
		}

		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		if (progressDialog != null && !progressDialog.isShowing())
			progressDialog = ProgressDialog.show(this, null,
					"Getting user details.Please wait..");

		Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(
				this);

		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Person currentPerson = Plus.PeopleApi
					.getCurrentPerson(mGoogleApiClient);
			String personName = currentPerson.getDisplayName();
			String personPhoto = currentPerson.getImage().getUrl();
			String personGooglePlusProfile = currentPerson.getUrl();
			String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
			String id = currentPerson.getId();

			personPhoto = personPhoto.substring(0, personPhoto.length() - 2)
					+ PROFILE_PIC_SIZE;


			nameToSend = personName;
			imageUrlToSend = personPhoto;
			emailToSend = email;
			idToSend = id;
			additionalDataToSend = currentPerson
					+ "  ---   profile url   --   " + personGooglePlusProfile;
			isGoogleAccountToSend = true;
		} else {
		}

		GetGoogleIdTokenTask task = new GetGoogleIdTokenTask();
		task.execute();
	}

	private void onGoogleSignOutClicked() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
		}
		// showSignedOutUI();
	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}

	@Override
	public void onResult(LoadPeopleResult arg0) {

	}

	private class GetGoogleIdTokenTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			progressDialog = ProgressDialog.show(LaunchActivity.this, null,
					"Getting Google Plus Access Token");
		}

		@Override
		protected String doInBackground(Void... params) {
			String accountName = Plus.AccountApi
					.getAccountName(mGoogleApiClient);
			Account account = new Account(accountName,
					GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
			String scopes = "audience:server:client_id:"
					+ "509487250429-pekgqlshg58mfqm6adctmvm3ul9nlbjr.apps.googleusercontent.com";
			try {
				return GoogleAuthUtil.getToken(getApplicationContext(),
						account, scopes);
			} catch (IOException e) {
				return null;
			} catch (GoogleAuthException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (result != null) {
				accessTokenToSend = result;
				makeLoginRequestToServer();
			} else {
			}
		}
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_LOGIN_REQUEST_TAG) {
			progressDialog.dismiss();
			if (status) {
				LoginObject obj = (LoginObject) data;
				if (obj.isStatus()) {
					Toast.makeText(this, "Logged in successfully ",
							Toast.LENGTH_SHORT).show();

					ZPreferences.setIsUserLogin(this, true);
					ZPreferences.setUserID(this,
							Integer.toString(obj.getUser_id()));
					ZPreferences.setUserProfileID(this,
							Integer.toString(obj.getUser_profile_id()));
					ZPreferences.setUserImageURL(this, imageUrlToSend);
					ZPreferences.setIsTutorialShown(this, false);
					ZPreferences
							.setIsGoogleAccount(this, isGoogleAccountToSend);
					ZPreferences.setUserName(this, nameToSend);
					ZPreferences.setUserEmail(this, emailToSend);

					Intent intent = new Intent(this, HomeActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					this.finish();
				} else {
					Toast.makeText(this, "Some server error occured",
							Toast.LENGTH_SHORT).show();
				}
			} else {
			}
		}
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_LOGIN_REQUEST_TAG) {
			progressDialog.dismiss();
			progressDialog = ProgressDialog.show(this, "Welcome",
					"Just a moment, logging in");
		}
	}
}