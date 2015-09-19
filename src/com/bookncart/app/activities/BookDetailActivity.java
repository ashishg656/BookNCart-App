package com.bookncart.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.adapters.ProductDetailSimilarBooksRecyclerAdapter;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.AddToCartObject;
import com.bookncart.app.baseobjects.AddToFavouritesObject;
import com.bookncart.app.baseobjects.BookDetailObject;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.fragments.BookDescriptionFragment;
import com.bookncart.app.fragments.ReviewsFragment;
import com.bookncart.app.fragments.WriteReviewLoggedInUserDialog;
import com.bookncart.app.fragments.WriteReviewNonLoggedInUserDialog;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.serverApi.ImageRequestManager;
import com.bookncart.app.serverApi.ImageRequestManager.RequestBitmap;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;
import com.bookncart.app.widgets.ObservableScrollView;
import com.bookncart.app.widgets.ObservableScrollViewListener;
import com.bookncart.app.widgets.RecyclerViewHorizontalScrolling;

@SuppressLint("NewApi")
public class BookDetailActivity extends BaseActivity implements
		ObservableScrollViewListener, UploadManagerCallback, ZRequestTags,
		OnClickListener {

	Toolbar toolbarWhiteIcon;
	int bookId;
	ObservableScrollView scrollView;
	ImageView bookImage;
	int deviceWidth;
	LinearLayout buyNowContainerLayoutFake, buyNowContainerLayoutOriginal;
	RecyclerViewHorizontalScrolling recyclerView;
	LinearLayoutManager layoutManager;
	ProductDetailSimilarBooksRecyclerAdapter adapter;
	View bookImageDividerView;
	int statusBarHeight, toolbarHeight;
	LinearLayout transparentToolbar;
	FrameLayout appbarContainer;

	public BookDetailObject bookDetailObject;

	TextView bookName, bookAuthor, conditionOldOrNew, bookPrice, bookMRP,
			bookDiscount, numberOfLikes, numberOfReviews, bookDescription,
			noSimilarBooksText;
	private CoordinatorLayout coordinatorLayout;

	ImageView likeButtonImage;

	boolean isFavouritesRequestRunning = false;
	boolean isAddToCartRequestRunning = false;
	public int darkColor;

	ReviewsFragment reviewsFragment;
	WriteReviewLoggedInUserDialog writeReviewLoggedInUserDialogFragment;
	WriteReviewNonLoggedInUserDialog writeReviewNonLoggedInUserDialogFragment;

	LinearLayout buynowButton, addtocartButton, gotocartButton,
			buynowButtonFake, addtocartButtonFake, gotocartButtonFake;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_detail_activity_layout);

		UploadManager.getInstance().addCallback(this, this);

		toolbarWhiteIcon = (Toolbar) findViewById(R.id.toolbar);
		darkColor = getResources().getColor(R.color.PrimaryDarkColor);
		scrollView = (ObservableScrollView) findViewById(R.id.bookdetailscrollview);
		bookImage = (ImageView) findViewById(R.id.book_image_detail_activity);
		toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
		buyNowContainerLayoutFake = (LinearLayout) findViewById(R.id.buynowaddtocartfake);
		buyNowContainerLayoutOriginal = (LinearLayout) findViewById(R.id.buynowaddtocartorig);
		recyclerView = (RecyclerViewHorizontalScrolling) findViewById(R.id.home_recycler_view_toprated_similar_Details);
		bookImageDividerView = (View) findViewById(R.id.image_divider_view);
		transparentToolbar = (LinearLayout) findViewById(R.id.faketoolbarshopdetail);
		appbarContainer = (FrameLayout) findViewById(R.id.appbarcontainer);
		progressDarkCircle = (View) findViewById(R.id.dark_circle);
		progressLightCircle = (View) findViewById(R.id.light_circle);
		progressImage = (ImageView) findViewById(R.id.image_progress);
		progressLayoutContainer = (FrameLayout) findViewById(R.id.progresslayutcontainre);
		likeButtonImage = (ImageView) findViewById(R.id.like_selecot_detail);
		buynowButton = (LinearLayout) findViewById(R.id.buynowbutton);
		addtocartButton = (LinearLayout) findViewById(R.id.addtocartbutton);
		gotocartButton = (LinearLayout) findViewById(R.id.gotocartbutton);
		buynowButtonFake = (LinearLayout) findViewById(R.id.buynowbuttonfake);
		addtocartButtonFake = (LinearLayout) findViewById(R.id.addtocartbuttonfake);
		gotocartButtonFake = (LinearLayout) findViewById(R.id.gotocartbuttonfake);

		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinatorhome);

		bookName = (TextView) findViewById(R.id.booknamebookdetail);
		bookAuthor = (TextView) findViewById(R.id.authorbookdetail);
		conditionOldOrNew = (TextView) findViewById(R.id.conditionbookdetail);
		bookPrice = (TextView) findViewById(R.id.price_text_bokdetail);
		bookMRP = (TextView) findViewById(R.id.mrp_book_detail);
		noSimilarBooksText = (TextView) findViewById(R.id.nullcasetextbookdeatail);
		bookDiscount = (TextView) findViewById(R.id.discount_book_detail);
		numberOfLikes = (TextView) findViewById(R.id.book_no_of_likes);
		numberOfReviews = (TextView) findViewById(R.id.no_of_reviews_book_Detail);
		bookDescription = (TextView) findViewById(R.id.description_book_detail);

		findViewById(R.id.backbuttonfake).setOnClickListener(this);
		findViewById(R.id.readmorebutton).setOnClickListener(this);
		findViewById(R.id.likebuttonlayout).setOnClickListener(this);
		findViewById(R.id.reviewfragmenopn).setOnClickListener(this);
		findViewById(R.id.searchbuttonfake).setOnClickListener(this);
		findViewById(R.id.wishlistcounttext).setVisibility(View.GONE);
		findViewById(R.id.cartcounttxt).setVisibility(View.GONE);
		findViewById(R.id.sharebutton).setOnClickListener(this);
		buynowButton.setOnClickListener(this);
		addtocartButton.setOnClickListener(this);
		gotocartButton.setOnClickListener(this);

		buynowButtonFake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buynowButton.performClick();
			}
		});
		addtocartButtonFake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addtocartButton.performClick();
			}
		});
		gotocartButtonFake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gotocartButton.performClick();
			}
		});

		setConnectionErrorVariables();

		retryDataConnectionLayout.setOnClickListener(this);

		deviceWidth = getResources().getDisplayMetrics().widthPixels;
		toolbarHeight = getResources().getDimensionPixelSize(
				R.dimen.bnc_toolbar_height);

		scrollView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						scrollView.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						statusBarHeight = getResources().getDisplayMetrics().heightPixels
								- scrollView.getHeight();
					}
				});

		setSupportActionBar(toolbarWhiteIcon);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("");

		appbarContainer.setTranslationY(-toolbarHeight);

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bookImage
				.getLayoutParams();
		params.height = deviceWidth
				- getResources().getDimensionPixelSize(
						R.dimen.bnc_toolbar_height);
		bookImage.setLayoutParams(params);

		scrollView.setScrollListnerer(this);

		bookId = getIntent().getExtras().getInt("bookid");

		loadData();
	}

	@SuppressWarnings("deprecation")
	private void loadData() {
		String url = ZApplication.getInstance().getBaseUrl() + "book_detail/";
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("book_id", Integer
				.toString(bookId)));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_BOOK_DETAIL_ACTIVITY_TAG, BNC_BOOK_DETAIL_ACTIVITY_TAG,
				BNC_BOOK_DETAIL_ACTIVITY_TAG, null, nameValuePairs, null);
	}

	@Override
	protected void onDestroy() {
		UploadManager.getInstance().removeCallback(this);
		super.onDestroy();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	void setStatusBarColor(int color, int fallbackColor) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(color);
		} else {
			toolbarWhiteIcon.setBackgroundColor(fallbackColor);
		}
	}

	@Override
	public void onScrollStopped() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_search) {
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			return true;
		} else if (id == android.R.id.home) {
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onScroll(int x, int y, int oldx, int oldy) {
		float trans = y / 3;
		bookImage.setTranslationY(trans);

		int[] locationFake = new int[5];
		int[] locationOrg = new int[5];
		buyNowContainerLayoutFake.getLocationOnScreen(locationFake);
		buyNowContainerLayoutOriginal.getLocationOnScreen(locationOrg);
		if (locationFake[1] < locationOrg[1]) {
			buyNowContainerLayoutOriginal.setVisibility(View.GONE);
		} else
			buyNowContainerLayoutOriginal.setVisibility(View.VISIBLE);

		toolbarScrollChanges(y, oldy);
	}

	private void toolbarScrollChanges(int y, int oldy) {
		int dy = -1 * (y - oldy);
		int location[] = new int[5];
		bookImageDividerView.getLocationOnScreen(location);
		int actualLocation = location[1] - statusBarHeight;
		if (actualLocation < 0) {
			float trans = appbarContainer.getTranslationY() + dy;
			if (trans > 0)
				trans = 0;
			else if (trans < -toolbarHeight)
				trans = -toolbarHeight;
			appbarContainer.setTranslationY(trans);
		} else {
			if (actualLocation < toolbarHeight) {
				float trans = transparentToolbar.getTranslationY() + dy;
				if (trans > 0)
					trans = 0;
				else if (trans < -toolbarHeight)
					trans = -toolbarHeight;
				transparentToolbar.setTranslationY(trans);
				if (appbarContainer.getTranslationY() > -toolbarHeight) {
					float transAppbar = appbarContainer.getTranslationY() + dy;
					if (transAppbar > 0)
						transAppbar = 0;
					else if (transAppbar < -toolbarHeight)
						transAppbar = -toolbarHeight;
					appbarContainer.setTranslationY(transAppbar);
				}
			} else {
				transparentToolbar.setTranslationY(0);
				appbarContainer.animate().translationY(-toolbarHeight)
						.setDuration(100).start();
			}
		}
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_BOOK_DETAIL_ACTIVITY_TAG) {
			hideMainContentLoadingAnimations();
			if (status) {
				hideConnectionErrorLayout();
				this.bookDetailObject = (BookDetailObject) data;
				fillDataInScrollView();
			} else {
				showConnectionErrorLayout();
			}
		} else if (requestType == BNC_ADD_TO_FAVOURITES_REQUEST_TAG) {
			isFavouritesRequestRunning = false;
			if (status) {
				AddToFavouritesObject obj = (AddToFavouritesObject) data;
				if (obj.isError())
					makeToast("Server error");
				else {
					if (obj.isRemovedFromFavourites()) {
						makeToast("Removed from wishlist");
						likeButtonImage.setSelected(false);
					} else {
						makeToast("Added to wishlist");
						likeButtonImage.setSelected(true);
					}
					ZPreferences
							.setWishlistCount(this, obj.getWishlist_count());
					numberOfLikes.setText(obj.getNumberOfLikesOnBook()
							+ " likes");
				}
			} else {
				makeToast("Unable to add to wishlist. Please check your network connection.");
			}
		} else if (requestType == BNC_ADD_TO_CART_REQUEST_TAG) {
			isAddToCartRequestRunning = false;
			if (status) {
				AddToCartObject obj = (AddToCartObject) data;
				if (obj.isError())
					makeToastCart(obj.getErrorMessage());
				else {
					makeToastCart("Added to cart");
				}
				addtocartButton.setVisibility(View.GONE);
				gotocartButton.setVisibility(View.VISIBLE);
				addtocartButtonFake.setVisibility(View.GONE);
				gotocartButtonFake.setVisibility(View.VISIBLE);
				ZPreferences.setCartCount(this, obj.getCart_count());
			} else {
				makeToastCart("Unable to add to cart. Please check your network connection.");
			}
		} else if (requestType == BNC_BUYNOW_REQUEST_TAG) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (status) {
				AddToCartObject obj = (AddToCartObject) data;
				if (obj.isError())
					makeToastCart(obj.getErrorMessage());
				else {
					makeToastCart("Added to cart");
				}
				addtocartButton.setVisibility(View.GONE);
				gotocartButton.setVisibility(View.VISIBLE);
				addtocartButtonFake.setVisibility(View.GONE);
				gotocartButtonFake.setVisibility(View.VISIBLE);
				ZPreferences.setCartCount(this, obj.getCart_count());

				openCartActivity();
			} else {
				makeToastCart("Unable to add to cart. Please check your network connection.");
			}
		} else {
			if (reviewsFragment != null) {
				reviewsFragment.uploadFinished(requestType, objectId, data,
						uploadId, status, parserId);
			}
			if (writeReviewLoggedInUserDialogFragment != null) {
				writeReviewLoggedInUserDialogFragment
						.uploadFinished(requestType, objectId, data, uploadId,
								status, parserId);
			}
			if (writeReviewNonLoggedInUserDialogFragment != null) {
				writeReviewNonLoggedInUserDialogFragment
						.uploadFinished(requestType, objectId, data, uploadId,
								status, parserId);
			}
		}
	}

	private void makeToast(String string) {
		Snackbar.make(coordinatorLayout, string, Snackbar.LENGTH_SHORT)
				.setActionTextColor(
						getResources().getColor(R.color.bnc_yellow_color))
				.setAction("WiSHLIST", new OnClickListener() {

					@Override
					public void onClick(View v) {
						openWishlistActivity();
					}
				}).show();
	}

	private void makeToastCart(String string) {
		Snackbar.make(coordinatorLayout, string, Snackbar.LENGTH_SHORT)
				.setActionTextColor(
						getResources().getColor(R.color.bnc_yellow_color))
				.setAction("CART", new OnClickListener() {

					@Override
					public void onClick(View v) {
						openCartActivity();
					}
				}).show();
	}

	private void fillDataInScrollView() {
		float discount = 100 - (float) bookDetailObject.getPrice()
				/ (float) bookDetailObject.getMrp() * 100.0f;
		toolbarTitle.setText(bookDetailObject.getName());
		bookAuthor.setText("By " + bookDetailObject.getAuthor());
		bookName.setText(bookDetailObject.getName());
		if (bookDetailObject.isCondition()) {
			conditionOldOrNew.setText("OLD");
		} else {
			conditionOldOrNew.setText("NEW");
		}
		bookPrice.setText("₹ " + bookDetailObject.getPrice());

		if (bookDetailObject.isIs_favourite())
			likeButtonImage.setSelected(true);
		else
			likeButtonImage.setSelected(false);

		StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
		bookMRP.setText("₹ " + bookDetailObject.getMrp(),
				TextView.BufferType.SPANNABLE);
		Spannable spannable = (Spannable) bookMRP.getText();
		spannable.setSpan(STRIKE_THROUGH_SPAN, 0,
				("₹ " + bookDetailObject.getMrp()).length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		bookDiscount.setText("You save : " + (int) discount + "%");
		numberOfLikes.setText(bookDetailObject.getNumber_of_likes() + " likes");
		numberOfReviews.setText(bookDetailObject.getNumber_of_reviews()
				+ " reviews");
		bookDescription.setText(bookDetailObject.getDescription());

		ImageRequestManager.get(this).requestImage2(
				this,
				bookImage,
				ZApplication.getInstance().getImageUrl(
						bookDetailObject.getImage()), new RequestBitmap() {

					@Override
					public void onRequestCompleted(Bitmap bitmap) {
						bookImage.setImageBitmap(bitmap);
						Palette.from(bitmap).generate(
								new PaletteAsyncListener() {

									@Override
									public void onGenerated(Palette p) {
										int colorLight = p
												.getDarkMutedColor(0x000000);
										int colorDark = p
												.getDarkVibrantColor(0x000000);
										darkColor = colorDark;
										toolbarWhiteIcon
												.setBackgroundColor(colorLight);
										setStatusBarColor(colorDark, colorLight);
									}
								});
					}
				}, -1);

		fillSimilarBooksDataInRecyclerView();
	}

	private void fillSimilarBooksDataInRecyclerView() {
		layoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.HORIZONTAL, false);
		recyclerView.setLayoutManager(layoutManager);

		adapter = new ProductDetailSimilarBooksRecyclerAdapter(
				bookDetailObject.getRelated_books(), this);
		recyclerView.setAdapter(adapter);

		if (bookDetailObject.getRelated_books().size() == 0) {
			recyclerView.setVisibility(View.GONE);
			noSimilarBooksText.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_BOOK_DETAIL_ACTIVITY_TAG) {
			showMainContentLoadingAnimations();
		} else if (requestType == BNC_ADD_TO_FAVOURITES_REQUEST_TAG) {
			isFavouritesRequestRunning = true;
		} else if (requestType == BNC_ADD_TO_CART_REQUEST_TAG) {
			isAddToCartRequestRunning = true;
		} else if (requestType == BNC_BUYNOW_REQUEST_TAG) {
			progressDialog = ProgressDialog.show(this, null,
					"Adding to cart..", true, false);
		} else {
			if (reviewsFragment != null) {
				reviewsFragment.uploadStarted(requestType, objectId, parserId,
						object);
			}
			if (writeReviewLoggedInUserDialogFragment != null) {
				writeReviewLoggedInUserDialogFragment.uploadStarted(
						requestType, objectId, parserId, object);
			}
			if (writeReviewNonLoggedInUserDialogFragment != null) {
				writeReviewNonLoggedInUserDialogFragment.uploadStarted(
						requestType, objectId, parserId, object);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.retrylayoutconnectionerror:
			loadData();
			break;
		case R.id.searchbuttonfake:
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.backbuttonfake:
			this.finish();
			break;
		case R.id.sharebutton:
			showShareingOptionsLayout();
			break;
		case R.id.likebuttonlayout:
			if (!isFavouritesRequestRunning)
				addToFavouritesRequest(bookId, true);
			break;
		case R.id.reviewfragmenopn:
			openReviewsFragment();
			break;
		case R.id.addtocartbutton:
			if (!isAddToCartRequestRunning)
				addToCartRequest();
			break;
		case R.id.gotocartbutton:
			openCartActivity();
			break;
		case R.id.buynowbutton:
			if (!isAddToCartRequestRunning)
				buyNowButtonClicked();
			break;
		case R.id.readmorebutton:
			openBookDescriptionFragment();
			break;
		default:
			break;
		}
	}

	private void buyNowButtonClicked() {
		String url = ZApplication.getInstance().getBaseUrl() + "add_to_cart/";
		List<NameValuePair> nameValuePair = new ArrayList<>();
		nameValuePair.add(new BasicNameValuePair("book_id", Integer
				.toString(bookId)));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_BUYNOW_REQUEST_TAG, BNC_BUYNOW_REQUEST_TAG,
				BNC_BUYNOW_REQUEST_TAG, null, nameValuePair, null);
	}

	private void addToCartRequest() {
		String url = ZApplication.getInstance().getBaseUrl() + "add_to_cart/";
		List<NameValuePair> nameValuePair = new ArrayList<>();
		nameValuePair.add(new BasicNameValuePair("book_id", Integer
				.toString(bookId)));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_ADD_TO_CART_REQUEST_TAG, BNC_ADD_TO_CART_REQUEST_TAG,
				BNC_ADD_TO_CART_REQUEST_TAG, null, nameValuePair, null);
	}

	void openBookDescriptionFragment() {
		Bundle b = new Bundle();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container,
						BookDescriptionFragment.newInstance(b))
				.addToBackStack("").commit();
	}

	private void openReviewsFragment() {
		Bundle b = new Bundle();
		b.putInt("bookid", bookId);
		reviewsFragment = ReviewsFragment.newInstance(b);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, reviewsFragment)
				.addToBackStack("").commit();
	}

	public void openWriteBookReviewFragment(int bookId) {
		Bundle b = new Bundle();
		b.putInt("bookid", bookId);

		if (ZPreferences.isUserLogIn(this)) {
			writeReviewLoggedInUserDialogFragment = WriteReviewLoggedInUserDialog
					.newInstance(b);
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.fragment_container,
							writeReviewLoggedInUserDialogFragment)
					.addToBackStack("").commit();
		} else {
			writeReviewNonLoggedInUserDialogFragment = WriteReviewNonLoggedInUserDialog
					.newInstance(b);
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.fragment_container,
							writeReviewNonLoggedInUserDialogFragment)
					.addToBackStack("").commit();
		}
	}
}
