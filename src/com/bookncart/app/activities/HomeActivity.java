package com.bookncart.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bookncart.app.R;
import com.bookncart.app.adapters.HomeActivityRecyclerViewAdapter;
import com.bookncart.app.adapters.SimilarBooksRecyclerViewAdapter;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.AddToFavouritesObject;
import com.bookncart.app.baseobjects.BookObject;
import com.bookncart.app.baseobjects.HomeActivityRequest1Object;
import com.bookncart.app.baseobjects.HomeActivityRequest2Object;
import com.bookncart.app.baseobjects.SimilarBooksObject;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.extras.MyAnimatorListener;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.objects.AllParserObjects;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.serverApi.ImageRequestManager;
import com.bookncart.app.serverApi.ImageRequestManager.RequestBitmap;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;
import com.bookncart.app.widgets.AnimatorUtils;
import com.bookncart.app.widgets.CircularImageView;
import com.bookncart.app.widgets.TouchEventInterceptLinearLayout;

@SuppressLint("NewApi")
public class HomeActivity extends BaseActivity implements OnDragListener,
		OnClickListener, UploadManagerCallback, ZRequestTags, AppConstants,
		SwipeRefreshLayout.OnRefreshListener {

	LinearLayoutManager layoutManager;
	NavigationView navigationView;
	TextView navigationDrawerUserName, navigationDrawerEmail;
	CircularImageView navigationDrawerImageUser;
	ImageView navigationDrawerImageDefault;
	DrawerLayout drawerLayout;
	LinearLayout navigationDrawerHeaderLayout;
	HomeActivityRecyclerViewAdapter adapter;
	RecyclerView recyclerView;
	private SwipeRefreshLayout swipeRefreshLayout;

	FrameLayout buttonsLayout, mainContentFrame;
	ImageView favouriteButton, thumbImageView, shareButton, similarButton;
	int radius, deviceWidth, thumbImageWidth, cornerAllowance, deviceHeight;
	float angle, angleCorrection, x, y, leftx, lefty, rightx, righty, touchx,
			touchy;
	public static final int TRANSLATION_DURATION = 200;
	public static final int ANIMATION_DURATION_SIMILAR_BOOKS = 400;
	public static final int ANIMATION_DURATION_SIMILAR_BOOKS_LONG = 700;
	final float scaleFactorForSimilarBooksLayout = 0.6f;
	AppBarLayout appBarLayout;
	ImageView similarBooksBackgroundView;
	TouchEventInterceptLinearLayout similarBooksLayout;
	FrameLayout similarBooksContainerLayout;
	public RecyclerView similarBooksRecyclerView;
	boolean isSimilarBooksLayoutCompletetlyVisible = false;
	ProgressBar similarBooksLoadingProgressBar;
	LinearLayout similarBooksLoadingLinearLayout, similarBooksNullCaseText;

	int similarBookShowBookid;
	String similarBookShowBookName;
	boolean similarBookShowIsfavourite;

	CoordinatorLayout coordinatorLayout;
	Snackbar cachedDataSnackbar;

	TextView wishlistCount, cartCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);

		UploadManager.getInstance().addCallback(this, this);

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		buttonsLayout = (FrameLayout) findViewById(R.id.buttons_layout);
		navigationDrawerEmail = (TextView) findViewById(R.id.navdraweremail);
		navigationDrawerImageUser = (CircularImageView) findViewById(R.id.avatar);
		navigationDrawerImageDefault = (ImageView) findViewById(R.id.avatardefault);
		navigationDrawerUserName = (TextView) findViewById(R.id.navdrawerusername);
		navigationDrawerHeaderLayout = (LinearLayout) findViewById(R.id.navigationdrawerheader);
		favouriteButton = (ImageView) findViewById(R.id.favouritebutton);
		thumbImageView = (ImageView) findViewById(R.id.thumb_ind_image);
		shareButton = (ImageView) findViewById(R.id.sharebutton);
		similarButton = (ImageView) findViewById(R.id.similarbutton);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
		mainContentFrame = (FrameLayout) findViewById(R.id.content_frame);
		similarBooksLayout = (TouchEventInterceptLinearLayout) findViewById(R.id.linearlayoutsimilarbookslineartouch);
		similarBooksBackgroundView = (ImageView) findViewById(R.id.similarbookbgview);
		similarBooksContainerLayout = (FrameLayout) findViewById(R.id.similarbooklayout);
		similarBooksRecyclerView = (RecyclerView) findViewById(R.id.similarbooks_recycler_view);
		similarBooksLoadingProgressBar = (ProgressBar) findViewById(R.id.similarbooksloadingprogressbar);
		similarBooksLoadingLinearLayout = (LinearLayout) findViewById(R.id.similarbooksloadingprogeress);
		similarBooksNullCaseText = (LinearLayout) findViewById(R.id.similarbooksnullcasetext);
		wishlistCount = (TextView) findViewById(R.id.wishlistcounttext);
		cartCount = (TextView) findViewById(R.id.cartcounttxt);

		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinatorhome);

		progressDarkCircle = (View) findViewById(R.id.dark_circle);
		progressLightCircle = (View) findViewById(R.id.light_circle);
		progressImage = (ImageView) findViewById(R.id.image_progress);
		progressLayoutContainer = (FrameLayout) findViewById(R.id.progresslayutcontainre);

		setConnectionErrorVariables();

		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources(
				R.color.bnc_red_color_primary, R.color.bnc_green_color_primary,
				R.color.PrimaryColor);
		swipeRefreshLayout.setProgressViewOffset(
				false,
				0,
				getResources().getDimensionPixelSize(
						R.dimen.bnc_home_swipe_refersh_padding_top));

		retryDataConnectionLayout.setOnClickListener(this);

		navigationDrawerHeaderLayout.setOnClickListener(this);

		if (ZPreferences.isUserLogIn(this)) {
			navigationDrawerEmail.setText(ZPreferences.getUserEmail(this));
			navigationDrawerUserName.setText(ZPreferences.getUserName(this));
			ImageRequestManager.get(this).requestImage2(this,
					navigationDrawerImageUser,
					ZPreferences.getUserImageURL(this), new RequestBitmap() {

						@Override
						public void onRequestCompleted(Bitmap bitmap) {
							navigationDrawerImageUser.setImageBitmap(bitmap);
							navigationDrawerImageDefault
									.setVisibility(View.GONE);
						}
					}, -1);
		} else {
			navigationDrawerEmail.setText("Please Login");
			navigationDrawerUserName.setText("BookNCart");
			navigationDrawerImageUser.setVisibility(View.GONE);
		}

		thumbImageView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						thumbImageView.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						thumbImageWidth = thumbImageView.getWidth();
						buttonsLayout.setVisibility(View.GONE);
					}
				});

		radius = getResources().getDimensionPixelSize(
				R.dimen.bnc_radius_home_activity);
		deviceWidth = getResources().getDisplayMetrics().widthPixels;

		appBarLayout.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));

		mainContentFrame.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						mainContentFrame.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						deviceHeight = mainContentFrame.getHeight();
					}
				});

		cornerAllowance = getResources().getDimensionPixelSize(
				R.dimen.bnc_corner_allowance_due_to_padding);

		layoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setBackgroundColor(getResources()
				.getColor(R.color.PrimaryColor));
		getSupportActionBar().setTitle("");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		toolbar.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						toolbar.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						toolbarHeight = toolbar.getHeight();
					}
				});

		setDrawerItemClickListener();
		setDrawerActionBarToggle();

		similarBooksContainerLayout.setVisibility(View.GONE);

		favouriteButton.setOnDragListener(this);
		similarButton.setOnDragListener(this);
		shareButton.setOnDragListener(this);
		recyclerView.addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				scrollToolbarBy(-dy);
				super.onScrolled(recyclerView, dx, dy);
			}

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					int pos = layoutManager.findFirstVisibleItemPosition();
					if (pos == 0) {
						setToolbarTranslation(recyclerView.getChildAt(0));
					} else
						scrollToolbarAfterTouchEnds();
				}
				super.onScrollStateChanged(recyclerView, newState);
			}
		});

		loadFirstDataRequest();
	}

	private void loadFirstDataRequest() {
		String url = ZApplication.getInstance().getBaseUrl()
				+ "home_request_1/";
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_HOME_REQUEST_TAG_1, BNC_HOME_REQUEST_TAG_1,
				BNC_HOME_REQUEST_TAG_1, null, null, null);
	}

	private void loadSecondDataRequest() {
		String url = ZApplication.getInstance().getBaseUrl()
				+ "home_request_2/";
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_HOME_REQUEST_TAG_2, BNC_HOME_REQUEST_TAG_2,
				BNC_HOME_REQUEST_TAG_2, null, null, null);
	}

	@Override
	protected void onDestroy() {
		UploadManager.getInstance().removeCallback(this);
		super.onDestroy();
	}

	private void setDrawerActionBarToggle() {
		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
				this, drawerLayout, toolbar, R.string.bnc_open_drawer,
				R.string.bnc_close_drawer) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();
		navigationView.setItemIconTintList(null);
	}

	private void setDrawerItemClickListener() {
		navigationView
				.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {

					@Override
					public boolean onNavigationItemSelected(MenuItem item) {
						item.setChecked(true);
						drawerLayout.closeDrawers();
						switch (item.getItemId()) {
						case R.id.bnc_home:
							return true;
						case R.id.bnc_recently_viewed_books:
							openRecentlyViewedBooksActivity();
							return true;
						case R.id.bnc_shop_by_categories:
							openShopByCategoriesActivity();
							return true;

						default:
							return true;
						}
					}
				});
	}

	public void scrollToolbarBy(int dy) {
		float requestedTranslation = appBarLayout.getTranslationY() + dy;
		if (requestedTranslation < -toolbarHeight) {
			requestedTranslation = -toolbarHeight;
			appBarLayout.setTranslationY(requestedTranslation);
		} else if (requestedTranslation > 0) {
			requestedTranslation = 0;
			appBarLayout.setTranslationY(requestedTranslation);
		} else if (requestedTranslation >= -toolbarHeight
				&& requestedTranslation <= 0) {
			appBarLayout.setTranslationY(requestedTranslation);
		}
	}

	public void scrollToolbarAfterTouchEnds() {
		float currentTranslation = -appBarLayout.getTranslationY();
		if (currentTranslation > toolbarHeight / 2) {
			appBarLayout.animate().translationY(-toolbarHeight)
					.setDuration(TRANSLATION_DURATION)
					.setInterpolator(new DecelerateInterpolator());
		} else {
			appBarLayout.animate().translationY(0)
					.setDuration(TRANSLATION_DURATION)
					.setInterpolator(new DecelerateInterpolator());
		}
	}

	public void setToolbarTranslation(View firstChild) {
		if (firstChild.getTop() < toolbarHeight && firstChild.getTop() >= 0) {
			appBarLayout.animate().translationY(0)
					.setDuration(TRANSLATION_DURATION)
					.setInterpolator(new DecelerateInterpolator());
		} else {
			scrollToolbarAfterTouchEnds();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_cart) {
			Intent intent = new Intent(this, WishlistAndCartActivity.class);
			startActivity(intent);
			return true;
		} else if (id == R.id.action_search) {
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			return true;
		} else if (id == R.id.action_wishlist) {
			openWishlistActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (similarBooksContainerLayout.getVisibility() == View.VISIBLE) {
			toolbar.getBackground().setAlpha(0);
			if (similarBooksLayout.getTranslationY() < deviceHeight
					* scaleFactorForSimilarBooksLayout) {
				hideSimilarButtonsLayout(ANIMATION_DURATION_SIMILAR_BOOKS_LONG);
			} else {
				hideSimilarButtonsLayout(ANIMATION_DURATION_SIMILAR_BOOKS);
			}
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.navigationdrawerheader:
			if (ZPreferences.isUserLogIn(this)) {
				Intent intent = new Intent(this, UserProfileActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(this, LaunchActivity.class);
				startActivity(i);
			}
			break;
		case R.id.retrylayoutconnectionerror:
			loadFirstDataRequest();
			break;

		default:
			break;
		}
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_HOME_REQUEST_TAG_1) {
			swipeRefreshLayout.setRefreshing(false);
			hideMainContentLoadingAnimations();
			if (status) {
				hideConnectionErrorLayout();
				if (cachedDataSnackbar != null)
					cachedDataSnackbar.dismiss();
				HomeActivityRequest1Object obj = (HomeActivityRequest1Object) data;
				setFirstAdapterData(obj);
				loadSecondDataRequest();

				ZPreferences.setWishlistCount(this, obj.getWishlist_count());
				performWishlistCountModification();
				ZPreferences.setCartCount(this, obj.getCart_count());
				performCartCountModification();
			} else {
				if (ZPreferences.getHomeRequest1Object(this) != null
						&& ZPreferences.getHomeRequest2Object(this) != null) {
					HomeActivityRequest1Object obj = (HomeActivityRequest1Object) AllParserObjects
							.getinstance().parseHomeActivityRequestObject1(
									ZPreferences.getHomeRequest1Object(this),
									this);
					HomeActivityRequest2Object obj2 = (HomeActivityRequest2Object) AllParserObjects
							.getinstance().parseHomeActivityRequestObject2(
									ZPreferences.getHomeRequest2Object(this),
									this);
					setFirstAdapterData(obj);
					setSecondAdapterdata(obj2);
					hideConnectionErrorLayout();
					cachedDataSnackbar = Snackbar
							.make(coordinatorLayout, "Connection error.",
									Snackbar.LENGTH_INDEFINITE)
							.setAction("RETRY", new OnClickListener() {

								@Override
								public void onClick(View v) {
									loadFirstDataRequest();
								}
							})
							.setActionTextColor(
									getResources().getColor(
											R.color.bnc_yellow_color));
					cachedDataSnackbar.show();
					appBarLayout.setTranslationY(0);
					performWishlistCountModification();
					performCartCountModification();
				} else {
					showConnectionErrorLayout();
				}
			}
		} else if (requestType == BNC_HOME_REQUEST_TAG_2) {
			swipeRefreshLayout.setRefreshing(false);
			if (status) {
				HomeActivityRequest2Object obj = (HomeActivityRequest2Object) data;
				setSecondAdapterdata(obj);
			} else {
				loadSecondDataRequest();
			}
		} else if (requestType == BNC_HOME_SIMILAR_BOOKS_REQUEST) {
			similarBooksLoadingLinearLayout.setVisibility(View.GONE);
			if (status) {
				SimilarBooksObject obj = (SimilarBooksObject) data;
				addDataInSimilarBooksRecyclerView(obj.getSimilar_books());
				if (obj.getSimilar_books().size() == 0) {
					similarBooksNullCaseText.setVisibility(View.VISIBLE);
				} else {
					similarBooksNullCaseText.setVisibility(View.GONE);
				}
			} else {
				similarBooksNullCaseText.setVisibility(View.GONE);
			}
		} else if (requestType == BNC_ADD_TO_FAVOURITES_REQUEST_TAG) {
			if (status) {
				AddToFavouritesObject obj = (AddToFavouritesObject) data;
				if (obj.isError())
					makeToast("Server error");
				else {
					if (obj.isRemovedFromFavourites()) {
						Snackbar.make(coordinatorLayout,
								"Removed from wishlist", Snackbar.LENGTH_SHORT)
								.setActionTextColor(
										getResources().getColor(
												R.color.bnc_yellow_color))
								.setAction("WiSHLIST", new OnClickListener() {

									@Override
									public void onClick(View v) {
										openWishlistActivity();
									}
								}).show();
					} else {
						Snackbar.make(coordinatorLayout, "Added to wishlist",
								Snackbar.LENGTH_SHORT)
								.setActionTextColor(
										getResources().getColor(
												R.color.bnc_yellow_color))
								.setAction("WiSHLIST", new OnClickListener() {

									@Override
									public void onClick(View v) {
										openWishlistActivity();
									}
								}).show();
					}
					ZPreferences
							.setWishlistCount(this, obj.getWishlist_count());
					performWishlistCountModification();
				}
			} else {
				makeToast("Unable to add to wishlist. Please check your network connection.");
			}
		}
	}

	private void performWishlistCountModification() {
		int count = ZPreferences.getWishlistCount(this);
		if (count == 0) {
			wishlistCount.setVisibility(View.GONE);
		} else {
			wishlistCount.setVisibility(View.VISIBLE);
			wishlistCount.setText("" + count);
		}
	}

	void performCartCountModification() {
		int count = ZPreferences.getCartCount(this);
		if (count == 0) {
			cartCount.setVisibility(View.GONE);
		} else {
			cartCount.setVisibility(View.VISIBLE);
			cartCount.setText("" + count);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		performWishlistCountModification();
		performCartCountModification();
	}

	void makeToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	private void setFirstAdapterData(HomeActivityRequest1Object obj) {
		adapter = new HomeActivityRecyclerViewAdapter(this, obj);
		recyclerView.setAdapter(adapter);
		appBarLayout.setTranslationY(0);
	}

	private void setSecondAdapterdata(HomeActivityRequest2Object obj) {
		adapter.addDataObject2(obj);
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_HOME_REQUEST_TAG_1) {
			if (!swipeRefreshLayout.isRefreshing())
				showMainContentLoadingAnimations();
		} else if (requestType == BNC_HOME_REQUEST_TAG_2) {

		} else if (requestType == BNC_HOME_SIMILAR_BOOKS_REQUEST) {
			similarBooksLoadingLinearLayout.setVisibility(View.VISIBLE);
		} else if (requestType == BNC_ADD_TO_FAVOURITES_REQUEST_TAG) {

		}
	}

	public void showButtonsLayout(int bookId, boolean isFavourite,
			String bookName) {
		buttonsLayout.setVisibility(View.VISIBLE);
		similarButton.setSelected(false);
		shareButton.setSelected(false);
		if (isFavourite) {
			favouriteButton.setSelected(true);
		} else {
			favouriteButton.setSelected(false);
		}

		this.similarBookShowBookid = bookId;
		this.similarBookShowIsfavourite = isFavourite;
		this.similarBookShowBookName = bookName;
	}

	public void setButtonsLocation(View v, final DragEvent event) {
		angle = 270;
		angleCorrection = 0;

		touchx = event.getX() - thumbImageWidth / 1.5f;
		touchy = event.getY() - thumbImageWidth / 1.5f;

		thumbImageView.setTranslationX(touchx);
		thumbImageView.setTranslationY(touchy);

		if (touchx < deviceWidth / 2)
			angle = angle + angleCorrection;
		else
			angle = angle - angleCorrection;

		checkClippings();

		List<Animator> animList = new ArrayList<>();

		animList.add(makeEnteranimation(favouriteButton, touchx, touchy, x, y));
		animList.add(makeEnteranimation(shareButton, touchx, touchy, leftx,
				lefty));
		animList.add(makeEnteranimation(similarButton, touchx, touchy, rightx,
				righty));

		AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(400);
		animSet.setInterpolator(new OvershootInterpolator());
		animSet.playTogether(animList);
		animSet.start();
	}

	private void checkClippings() {
		int anglecorrectionscale = 10;
		x = (float) (touchx + radius * Math.cos(Math.toRadians(angle)));
		y = (float) (touchy + radius * Math.sin(Math.toRadians(angle)));
		leftx = (float) (touchx + radius * Math.cos(Math.toRadians(angle - 45)));
		lefty = (float) (touchy + radius * Math.sin(Math.toRadians(angle - 45)));
		rightx = (float) (touchx + radius
				* Math.cos(Math.toRadians(angle + 45)));
		righty = (float) (touchy + radius
				* Math.sin(Math.toRadians(angle + 45)));

		if (righty < -cornerAllowance || lefty < -cornerAllowance
				|| y < -cornerAllowance) {
			if (touchx < deviceWidth / 2)
				angle = angle + anglecorrectionscale;
			else
				angle = angle - anglecorrectionscale;
			checkClippings();
		}
		if (leftx < -cornerAllowance) {
			angle = angle + anglecorrectionscale;
			checkClippings();
		}
		if (rightx > deviceWidth - thumbImageWidth + cornerAllowance) {
			angle = angle - anglecorrectionscale;
			checkClippings();
		}
	}

	private Animator makeEnteranimation(final View item, float startx,
			float starty, float endx, float endy) {
		Animator anim = ObjectAnimator.ofPropertyValuesHolder(item,
				AnimatorUtils.rotation(0f, 720f),
				AnimatorUtils.translationX(startx, endx),
				AnimatorUtils.translationY(starty, endy));

		return anim;
	}

	public void hideButtonsLayout() {
		List<Animator> animList = new ArrayList<>();

		animList.add(makeExitanimation(shareButton, touchx, touchy, leftx,
				lefty));
		animList.add(makeExitanimation(favouriteButton, touchx, touchy, x, y));
		animList.add(makeExitanimation(similarButton, touchx, touchy, rightx,
				righty));

		AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(100);
		animSet.playSequentially(animList);
		animSet.addListener(new MyAnimatorListener() {

			@Override
			public void onAnimationEnd(Animator animation) {
				buttonsLayout.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				buttonsLayout.setVisibility(View.GONE);
			}
		});
		animSet.start();
	}

	private Animator makeExitanimation(ImageView item, float touchx,
			float touchy, float x, float y) {
		Animator anim = ObjectAnimator.ofPropertyValuesHolder(item,
				AnimatorUtils.rotation(720f, 0f),
				AnimatorUtils.translationX(x, touchx),
				AnimatorUtils.translationY(y, touchy));
		return anim;
	}

	public boolean onDrag(View v, DragEvent event) {
		switch (v.getId()) {
		case R.id.favouritebutton:
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_LOCATION:
				v.setSelected(true);
				v.invalidate();
				break;
			case DragEvent.ACTION_DROP:
				if (buttonsLayout.getVisibility() != View.GONE)
					hideButtonsLayout();
				addToFavouritesRequest(similarBookShowBookid, false);
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				v.setSelected(false);
				v.invalidate();

			default:
				break;
			}
			break;
		case R.id.similarbutton:
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_LOCATION:
				v.setSelected(true);
				v.invalidate();
				break;
			case DragEvent.ACTION_DROP:
				if (buttonsLayout.getVisibility() != View.GONE)
					hideButtonsLayout();
				showSimilarBooksLayout();
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				v.setSelected(false);
				v.invalidate();
			}
			break;
		case R.id.sharebutton:
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_LOCATION:
				v.setSelected(true);
				v.invalidate();
				break;
			case DragEvent.ACTION_DROP:
				showShareingOptionsLayout();
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				v.setSelected(false);
				v.invalidate();
			}
			break;

		default:
			break;
		}
		return true;
	}

	public void hideSimilarButtonsLayout(int duration) {
		isSimilarBooksLayoutCompletetlyVisible = false;
		appBarLayout.setAlpha(255);
		similarBooksBackgroundView.setImageAlpha(0);
		ObjectAnimator recyclerAnimator1 = ObjectAnimator.ofFloat(recyclerView,
				"scaleX", 1);
		ObjectAnimator recyclerAnimator2 = ObjectAnimator.ofFloat(recyclerView,
				"scaleY", 1);
		ObjectAnimator animTrans = ObjectAnimator.ofFloat(similarBooksLayout,
				"translationY", deviceHeight);

		animTrans.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float alphaFactor = (float) similarBooksLayout
						.getTranslationY() / (float) deviceHeight;
				int alpha = (int) (alphaFactor * 255);
				toolbar.getBackground().setAlpha(Math.abs(alpha));
			}
		});

		List<Animator> animSetList = new ArrayList<>();
		animSetList.add(animTrans);
		animSetList.add(recyclerAnimator1);
		animSetList.add(recyclerAnimator2);

		AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(duration);
		animSet.setInterpolator(new AccelerateDecelerateInterpolator());
		animSet.addListener(new MyAnimatorListener() {

			@Override
			public void onAnimationEnd(Animator animation) {
				similarBooksContainerLayout.setVisibility(View.GONE);
				toolbar.getBackground().setAlpha(255);
			}
		});
		animSet.playTogether(animSetList);
		animSet.start();
	}

	private void showSimilarBooksLayout() {
		requestForSimilarBooks(similarBookShowBookid);

		isSimilarBooksLayoutCompletetlyVisible = false;
		appBarLayout.setTranslationY(0);
		appBarLayout.setAlpha(0);
		similarBooksContainerLayout.setVisibility(View.VISIBLE);
		similarBooksBackgroundView.setImageAlpha(0);
		recyclerView.setPivotY(0);
		recyclerView.setPivotX(deviceWidth / 2);

		similarBooksRecyclerView.setAdapter(null);

		((TextView) findViewById(R.id.similarbooktext))
				.setText("Books similar to " + similarBookShowBookName);

		ObjectAnimator recyclerAnimator1 = ObjectAnimator.ofFloat(recyclerView,
				"scaleX", 1, scaleFactorForSimilarBooksLayout);
		ObjectAnimator recyclerAnimator2 = ObjectAnimator.ofFloat(recyclerView,
				"scaleY", 1, scaleFactorForSimilarBooksLayout);
		ObjectAnimator animTrans = ObjectAnimator.ofFloat(similarBooksLayout,
				"translationY", deviceHeight, deviceHeight
						* scaleFactorForSimilarBooksLayout);

		List<Animator> animSetList = new ArrayList<>();
		animSetList.add(animTrans);
		animSetList.add(recyclerAnimator1);
		animSetList.add(recyclerAnimator2);

		final AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(ANIMATION_DURATION_SIMILAR_BOOKS);
		animSet.setInterpolator(new AccelerateDecelerateInterpolator());
		animSet.playTogether(animSetList);
		similarBooksContainerLayout.getViewTreeObserver()
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						similarBooksContainerLayout.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						animSet.start();
					}
				});
	}

	@SuppressWarnings("deprecation")
	public void requestForSimilarBooks(int bookid) {
		String url = ZApplication.getInstance().getBaseUrl()
				+ "related_books_request/";
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("book_id", Integer
				.toString(bookid)));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_HOME_SIMILAR_BOOKS_REQUEST, BNC_HOME_SIMILAR_BOOKS_REQUEST,
				BNC_HOME_SIMILAR_BOOKS_REQUEST, null, nameValuePairs, null);
	}

	private void addDataInSimilarBooksRecyclerView(
			List<BookObject> similarBooksData) {
		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
		similarBooksRecyclerView.setLayoutManager(gridLayoutManager);

		SimilarBooksRecyclerViewAdapter adapterSimilarBooks = new SimilarBooksRecyclerViewAdapter(
				similarBooksData, this);
		similarBooksRecyclerView.setAdapter(adapterSimilarBooks);

		similarBooksBackgroundView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toolbar.getBackground().setAlpha(0);
				hideSimilarButtonsLayout(ANIMATION_DURATION_SIMILAR_BOOKS);
			}
		});

		similarBooksLayout.setOnTouchListener(new MyTouchListener());
		similarBooksNullCaseText.setOnTouchListener(new MyTouchListener());
	}

	class MyTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				if (similarBooksLayout.getTranslationY() > deviceHeight
						* scaleFactorForSimilarBooksLayout) {
					hideSimilarButtonsLayout(ANIMATION_DURATION_SIMILAR_BOOKS);
				} else {
					if (isSimilarBooksLayoutCompletetlyVisible) {
						isSimilarBooksLayoutCompletetlyVisible = false;
						ObjectAnimator animTrans = ObjectAnimator
								.ofFloat(
										similarBooksLayout,
										"translationY",
										deviceHeight
												* scaleFactorForSimilarBooksLayout);
						animTrans.setDuration(400);
						animTrans
								.addUpdateListener(new AnimatorUpdateListener() {

									@Override
									public void onAnimationUpdate(
											ValueAnimator animation) {
										float alphaFactor = (float) animation
												.getAnimatedValue()
												/ (deviceHeight * scaleFactorForSimilarBooksLayout);
										int alpha = 255 - (int) (alphaFactor * 255);
										similarBooksBackgroundView
												.setImageAlpha(alpha);
									}
								});
						animTrans.start();
					} else {
						isSimilarBooksLayoutCompletetlyVisible = true;
						ObjectAnimator animTrans = ObjectAnimator.ofFloat(
								similarBooksLayout, "translationY", 0);
						animTrans.setDuration(400);
						animTrans
								.addUpdateListener(new AnimatorUpdateListener() {

									@Override
									public void onAnimationUpdate(
											ValueAnimator animation) {
										float alphaFactor = (float) animation
												.getAnimatedValue()
												/ (deviceHeight * scaleFactorForSimilarBooksLayout);
										int alpha = 255 - (int) (alphaFactor * 255);
										similarBooksBackgroundView
												.setImageAlpha(alpha);
									}
								});
						animTrans.start();
					}

				}
				return true;
			case MotionEvent.ACTION_MOVE:
				if (event.getHistorySize() > 0) {
					int dy = (int) (event.getY() - event.getHistoricalY(0)) * 2;
					float requestedTrans = similarBooksLayout.getTranslationY()
							+ dy;
					if (requestedTrans > deviceHeight)
						requestedTrans = deviceHeight;
					else if (requestedTrans < 0) {
						requestedTrans = 0;
						// isSimilarBooksLayoutCompletetlyVisible = true;
					}

					if (requestedTrans > deviceHeight
							* scaleFactorForSimilarBooksLayout) {
						similarBooksBackgroundView.setImageAlpha(0);
						float factor = requestedTrans / deviceHeight;
						recyclerView.setScaleX(factor);
						recyclerView.setScaleY(factor);

						float rationalisedfactor = (((factor - scaleFactorForSimilarBooksLayout) * (1 - 0)) / (1 - scaleFactorForSimilarBooksLayout)) + 0;
						int alpha = (int) (rationalisedfactor * 255);
						appBarLayout.setAlpha(255);
						toolbar.getBackground().setAlpha(alpha);
					} else {
						toolbar.getBackground().setAlpha(0);
						appBarLayout.setAlpha(0);
						float alphaFactor = requestedTrans
								/ (deviceHeight * scaleFactorForSimilarBooksLayout);
						int alpha = 255 - (int) (alphaFactor * 255);
						similarBooksBackgroundView.setImageAlpha(alpha);
					}
					similarBooksLayout.setTranslationY(requestedTrans);
				}
				break;

			default:
				break;
			}
			return true;
		}
	}

	@Override
	public void onRefresh() {
		swipeRefreshLayout.setRefreshing(true);
		loadFirstDataRequest();
	}

	public void enableDisableSwipeRefresh(boolean status) {
		swipeRefreshLayout.setEnabled(status);
	}
}