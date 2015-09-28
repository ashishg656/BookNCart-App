package com.bookncart.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookncart.app.R;
import com.bookncart.app.adapters.ShopActivityListAdapter;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.ShopActivityObject;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.extras.MyAnimatorListener;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;
import com.bookncart.app.widgets.RangeSeekBar;
import com.bookncart.app.widgets.RangeSeekBar.OnRangeSeekBarChangeListener;

@SuppressLint("NewApi")
public class ShopActivity extends BaseActivity implements AppConstants,
		OnClickListener, ZRequestTags, UploadManagerCallback {

	int bookType;
	String actionBarTitle;
	AppBarLayout appBarLayout;
	RecyclerView recyclerView;
	GridLayoutManager gridLayoutManager;
	LinearLayoutManager linearLayoutManager;
	ShopActivityListAdapter adapter;
	int appBarLayoutHeight, filterLayoutHeight;
	public static final int TRANSLATION_DURATION = 200;
	ImageView changeDisplayTypeButton;
	public int currentDisplayType = BNC_SHOP_DISPLAY_TYPE_GRID;
	LinearLayout sortButtonLayout, filtersButtonLayout;
	String[] sortingOptions = { "Name", "price", "Author name", "Popularity" };
	int currentSelectedSortingMode = 0;
	int minPriceRange = 0, maxPriceRange = 10000;
	int selectedMinPriceRange = minPriceRange,
			selectedMaxPriceRange = maxPriceRange;
	RangeSeekBar<Integer> rangeSeekBar;
	FrameLayout filtersLayoutContainer;
	EditText minPriceRangeEditText, maxPriceRangeEditText;
	Button discardFilterChangesButton, resetFiltersButton;
	int selectedFeaturedFilter = BNC_SHOP_FILTER_FEATURED_BOTH;
	int selectedConditionFilter = BNC_SHOP_FILTER_CONDITION_BOTH;
	CheckBox oldBooksCheckBox, newBooksCheckBox, featuredBooksCheckBox,
			nonFeaturedBooksCheckBox;
	LinearLayout applyFiltersButton;

	// request params
	Integer nextPageNumber = 1;
	boolean isMoreAllowed;
	boolean isRequestRunning = false;
	int modeToSend = -1;
	boolean categoryWise = false;
	int categoryId;
	boolean tagWise = false;
	int tagId;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_layout);

		UploadManager.getInstance().addCallback(this, this);

		setConnectionErrorVariables();

		retryDataConnectionLayout.setOnClickListener(this);

		recyclerView = (RecyclerView) findViewById(R.id.shop_recycler_view);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
		changeDisplayTypeButton = (ImageView) findViewById(R.id.changelistviewtype);
		sortButtonLayout = (LinearLayout) findViewById(R.id.sort_layput_shop);
		filtersButtonLayout = (LinearLayout) findViewById(R.id.filtersbutton);
		rangeSeekBar = (RangeSeekBar<Integer>) findViewById(R.id.rangeseekbar);
		filtersLayoutContainer = (FrameLayout) findViewById(R.id.filterslayoutshop);
		minPriceRangeEditText = (EditText) findViewById(R.id.minpricetext);
		maxPriceRangeEditText = (EditText) findViewById(R.id.maxpricetext);
		discardFilterChangesButton = (Button) findViewById(R.id.discradfilterchanges);
		resetFiltersButton = (Button) findViewById(R.id.resetfilters);
		oldBooksCheckBox = (CheckBox) findViewById(R.id.oldbookscheckbox);
		newBooksCheckBox = (CheckBox) findViewById(R.id.newbookscheckbox);
		featuredBooksCheckBox = (CheckBox) findViewById(R.id.featuredbookscheckbox);
		nonFeaturedBooksCheckBox = (CheckBox) findViewById(R.id.nonfeaturedbookscheckbox);
		applyFiltersButton = (LinearLayout) findViewById(R.id.applyfiltersbutton);
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

		changeDisplayTypeButton.setOnClickListener(this);
		sortButtonLayout.setOnClickListener(this);
		filtersButtonLayout.setOnClickListener(this);
		resetFiltersButton.setOnClickListener(this);
		discardFilterChangesButton.setOnClickListener(this);
		applyFiltersButton.setOnClickListener(this);

		appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						appBarLayout.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						appBarLayoutHeight = appBarLayout.getHeight();
					}
				});
		filterLayoutHeight = getResources().getDimensionPixelSize(
				R.dimen.bnc_shop_filter_layout_height);

		recyclerView.addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				scrollToolbarBy(-dy);
				super.onScrolled(recyclerView, dx, dy);
				int pos = -1;
				if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
					pos = gridLayoutManager.findLastVisibleItemPosition();
				} else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
					pos = linearLayoutManager.findLastVisibleItemPosition();
				}
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
					int pos = -1;
					if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
						int[] temp = new int[2];
						((StaggeredGridLayoutManager) recyclerView
								.getLayoutManager())
								.findFirstVisibleItemPositions(temp);
						pos = temp[0];
					} else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
						pos = ((LinearLayoutManager) recyclerView
								.getLayoutManager())
								.findFirstVisibleItemPosition();
					}
					if (pos == 0) {
						setToolbarTranslation(recyclerView.getChildAt(0));
					} else
						scrollToolbarAfterTouchEnds();
				}
				super.onScrollStateChanged(recyclerView, newState);
			}
		});

		actionBarTitle = getIntent().getExtras().getString("actionbarname");
		toolbarTitle.setText(actionBarTitle);

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
		linearLayoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(gridLayoutManager);

		bookType = getIntent().getExtras().getInt("home_book_type", -1);
		if (bookType == -1) {

		} else {
			if (bookType == BNC_HOME_LIST_TYPE_FEATURED)
				modeToSend = BNC_MODE_SHOP_FEATURED;
			else if (bookType == BNC_HOME_LIST_TYPE_BEST_SELLING) {
				modeToSend = BNC_MODE_SHOP_BEST_SELLING;
			} else if (bookType == BNC_HOME_LIST_TYPE_NEW_ADDED) {
				modeToSend = BNC_MODE_SHOP_LATEST;
			} else if (bookType == BNC_HOME_LIST_TYPE_CURRENTLY_ACTIVE) {
				modeToSend = BNC_MODE_SHOP_CURRENTLY_ACTIVE;
			} else if (bookType == BNC_HOME_LIST_TYPE_TOP_RATED) {
				modeToSend = BNC_MODE_SHOP_TOP_RATED;
			}
		}

		if (getIntent().getExtras().containsKey("category_wise")) {
			modeToSend = BNC_MODE_SHOP_CATEGORY_WISE;
			categoryWise = true;
			categoryId = getIntent().getExtras().getInt("category_wise");
		} else if (getIntent().getExtras().containsKey("tag_wise")) {
			modeToSend = BNC_MODE_SHOP_TAG_WISE;
			tagWise = true;
			tagId = getIntent().getExtras().getInt("tag_wise");
		}

		loadData();
	}

	@Override
	protected void onDestroy() {
		UploadManager.getInstance().removeCallback(this);
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void loadData() {
		if (modeToSend != -1) {
			String url = ZApplication.getInstance().getBaseUrl()
					+ "commonly_popular_books/";
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("mode", Integer
					.toString(modeToSend)));
			nameValuePairs.add(new BasicNameValuePair("pagenumber", Integer
					.toString(nextPageNumber)));
			if (categoryWise == true) {
				nameValuePairs.add(new BasicNameValuePair("category_id",
						Integer.toString(categoryId)));
			} else if (tagWise == true) {
				nameValuePairs.add(new BasicNameValuePair("tag_id", Integer
						.toString(tagId)));
			}
			UploadManager.getInstance().makeAyncRequest(url,
					BNC_SHOP_ACTIVTY_TAG, BNC_SHOP_ACTIVTY_TAG,
					BNC_SHOP_ACTIVTY_TAG, null, nameValuePairs, null);
		}
	}

	public void scrollToolbarBy(int dy) {
		float requestedTranslation = appBarLayout.getTranslationY() + dy;
		if (requestedTranslation < -appBarLayoutHeight) {
			requestedTranslation = -appBarLayoutHeight;
			appBarLayout.setTranslationY(requestedTranslation);
		} else if (requestedTranslation > 0) {
			requestedTranslation = 0;
			appBarLayout.setTranslationY(requestedTranslation);
		} else if (requestedTranslation >= -appBarLayoutHeight
				&& requestedTranslation <= 0) {
			appBarLayout.setTranslationY(requestedTranslation);
		}
	}

	public void scrollToolbarAfterTouchEnds() {
		float currentTranslation = -appBarLayout.getTranslationY();
		if (currentTranslation > (appBarLayoutHeight - filterLayoutHeight)) {
			appBarLayout.animate().translationY(-appBarLayoutHeight)
					.setDuration(TRANSLATION_DURATION)
					.setInterpolator(new DecelerateInterpolator());
		} else {
			appBarLayout.animate().translationY(0)
					.setDuration(TRANSLATION_DURATION)
					.setInterpolator(new DecelerateInterpolator());
		}
	}

	public void setToolbarTranslation(View firstChild) {
		if (firstChild.getTop() < appBarLayoutHeight
				&& firstChild.getTop() >= 0) {
			appBarLayout.animate().translationY(0)
					.setDuration(TRANSLATION_DURATION)
					.setInterpolator(new DecelerateInterpolator());
		} else {
			scrollToolbarAfterTouchEnds();
		}
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

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.changelistviewtype:
			if (currentDisplayType == BNC_SHOP_DISPLAY_TYPE_GRID) {
				try {
					currentDisplayType = BNC_SHOP_DISPLAY_TYPE_LIST;
					changeDisplayTypeButton
							.setImageResource(R.drawable.list_long_icon);
					adapter = new ShopActivityListAdapter(adapter.mData, this,
							false);
					recyclerView.setAdapter(adapter);
					recyclerView.setLayoutManager(linearLayoutManager);
				} catch (Exception ex) {
				}
			} else if (currentDisplayType == BNC_SHOP_DISPLAY_TYPE_LIST) {
				try {
					currentDisplayType = BNC_SHOP_DISPLAY_TYPE_LONG_LIST;
					changeDisplayTypeButton
							.setImageResource(R.drawable.grid_icon);
					adapter = new ShopActivityListAdapter(adapter.mData, this,
							false);
					recyclerView.setAdapter(adapter);
					recyclerView.setLayoutManager(linearLayoutManager);
				} catch (Exception ex) {
				}
			} else if (currentDisplayType == BNC_SHOP_DISPLAY_TYPE_LONG_LIST) {
				try {
					currentDisplayType = BNC_SHOP_DISPLAY_TYPE_GRID;
					changeDisplayTypeButton
							.setImageResource(R.drawable.list_icon);
					adapter = new ShopActivityListAdapter(adapter.mData, this,
							false);
					recyclerView.setAdapter(adapter);
					recyclerView.setLayoutManager(gridLayoutManager);
				} catch (Exception ex) {
				}
			}
			break;
		case R.id.sort_layput_shop:
			showSortingOptionsDialog();
			break;
		case R.id.filtersbutton:
			showFiltersOptionsLayout();
			break;
		case R.id.discradfilterchanges:
			hidefiltersButtonLayout();
			break;
		case R.id.resetfilters:
			resetFiltersContent();
			break;
		case R.id.applyfiltersbutton:
			applyFiltersButtonClicked();
			break;
		case R.id.retrylayoutconnectionerror:
			loadData();
			break;

		default:
			break;
		}
	}

	private void hidefiltersButtonLayout() {
		ObjectAnimator anim = ObjectAnimator
				.ofFloat(filtersLayoutContainer, "translationY", getResources()
						.getDisplayMetrics().heightPixels);
		anim.addListener(new MyAnimatorListener() {

			@Override
			public void onAnimationEnd(Animator animation) {
				filtersLayoutContainer.setVisibility(View.GONE);
			}
		});
		anim.setDuration(400);
		anim.start();
	}

	private void showFiltersOptionsLayout() {
		filtersLayoutContainer.setVisibility(View.VISIBLE);
		filtersLayoutContainer.setTranslationX(getResources()
				.getDisplayMetrics().widthPixels);
		filtersLayoutContainer.setTranslationY(0);
		filtersLayoutContainer.animate().translationX(0).setDuration(400)
				.start();

		fillDataInFiltersLayout();
	}

	private void fillDataInFiltersLayout() {
		rangeSeekBar.setRangeValues(minPriceRange, maxPriceRange);
		rangeSeekBar.setSelectedMaxValue(selectedMaxPriceRange);
		rangeSeekBar.setSelectedMinValue(selectedMinPriceRange);
		minPriceRangeEditText.setText("₹ " + selectedMinPriceRange);
		maxPriceRangeEditText.setText("₹ " + selectedMaxPriceRange);
		rangeSeekBar.setNotifyWhileDragging(true);
		rangeSeekBar
				.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {

					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Integer minValue,
							Integer maxValue) {
						minPriceRangeEditText.setText("₹ " + minValue);
						maxPriceRangeEditText.setText("₹ " + maxValue);
					}
				});
		switch (selectedConditionFilter) {
		case BNC_SHOP_FILTER_CONDITION_BOTH:
			oldBooksCheckBox.setChecked(true);
			newBooksCheckBox.setChecked(true);
			break;
		case BNC_SHOP_FILTER_CONDITION_NEW:
			oldBooksCheckBox.setChecked(false);
			newBooksCheckBox.setChecked(true);
			break;
		case BNC_SHOP_FILTER_CONDITION_OLD:
			oldBooksCheckBox.setChecked(true);
			newBooksCheckBox.setChecked(false);
			break;
		}
		switch (selectedFeaturedFilter) {
		case BNC_SHOP_FILTER_FEATURED_BOTH:
			featuredBooksCheckBox.setChecked(true);
			nonFeaturedBooksCheckBox.setChecked(true);
			break;
		case BNC_SHOP_FILTER_FEATURED_FEATURED:
			featuredBooksCheckBox.setChecked(true);
			nonFeaturedBooksCheckBox.setChecked(false);
			break;
		case BNC_SHOP_FILTER_FEATURED_NONFEATURED:
			featuredBooksCheckBox.setChecked(false);
			nonFeaturedBooksCheckBox.setChecked(true);
			break;
		}
	}

	private void applyFiltersButtonClicked() {
		selectedMaxPriceRange = rangeSeekBar.getSelectedMaxValue();
		selectedMinPriceRange = rangeSeekBar.getSelectedMinValue();
		if (oldBooksCheckBox.isChecked() && !newBooksCheckBox.isChecked()) {
			selectedConditionFilter = BNC_SHOP_FILTER_CONDITION_OLD;
		} else if (!oldBooksCheckBox.isChecked()
				&& newBooksCheckBox.isChecked()) {
			selectedConditionFilter = BNC_SHOP_FILTER_CONDITION_NEW;
		} else {
			selectedConditionFilter = BNC_SHOP_FILTER_CONDITION_BOTH;
		}

		if (featuredBooksCheckBox.isChecked()
				&& !nonFeaturedBooksCheckBox.isChecked()) {
			selectedFeaturedFilter = BNC_SHOP_FILTER_FEATURED_FEATURED;
		} else if (!featuredBooksCheckBox.isChecked()
				&& nonFeaturedBooksCheckBox.isChecked()) {
			selectedFeaturedFilter = BNC_SHOP_FILTER_FEATURED_NONFEATURED;
		} else {
			selectedFeaturedFilter = BNC_SHOP_FILTER_FEATURED_BOTH;
		}

		hidefiltersButtonLayout();
	}

	private void resetFiltersContent() {
		selectedMinPriceRange = minPriceRange;
		selectedMaxPriceRange = maxPriceRange;
		selectedConditionFilter = BNC_SHOP_FILTER_CONDITION_BOTH;
		selectedFeaturedFilter = BNC_SHOP_FILTER_FEATURED_BOTH;
		fillDataInFiltersLayout();
	}

	private void showSortingOptionsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Sort By");
		builder.setSingleChoiceItems(sortingOptions,
				currentSelectedSortingMode,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == currentSelectedSortingMode)
							dialog.cancel();
						else {
							currentSelectedSortingMode = which;
							dialog.cancel();
							Toast.makeText(ShopActivity.this,
									"Load data for current sorting mode",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		builder.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onBackPressed() {
		if (filtersLayoutContainer.getVisibility() == View.VISIBLE)
			hidefiltersButtonLayout();
		else
			super.onBackPressed();
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_SHOP_ACTIVTY_TAG) {
			isRequestRunning = false;
			hideMainContentLoadingAnimations();
			if (status) {
				if (adapter == null) {
					hideConnectionErrorLayout();
					ShopActivityObject obj = (ShopActivityObject) data;
					if (obj.getNext_page() == null) {
						nextPageNumber = null;
						isMoreAllowed = false;
					} else {
						nextPageNumber = obj.getNext_page();
						isMoreAllowed = true;
					}
					adapter = new ShopActivityListAdapter(obj.getBooks(), this,
							isMoreAllowed);
					recyclerView.setAdapter(adapter);
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
				if (adapter == null) {
					showConnectionErrorLayout();
				} else {
					Toast.makeText(
							this,
							"Unable to load more data. Check internet connection",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_SHOP_ACTIVTY_TAG) {
			isRequestRunning = true;
			if (adapter == null) {
				hideConnectionErrorLayout();
				showMainContentLoadingAnimations();
			}
		}
	}
}