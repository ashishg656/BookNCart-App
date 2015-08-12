package com.bookncart.app.activities;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
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
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.extras.MyAnimatorListener;
import com.bookncart.app.objects.HomeTopRatedBookObject;
import com.bookncart.app.widgets.RangeSeekBar;
import com.bookncart.app.widgets.RangeSeekBar.OnRangeSeekBarChangeListener;

@SuppressLint("NewApi")
public class ShopActivity extends AppCompatActivity implements AppConstants,
		OnClickListener {

	int bookType;
	String actionBarTitle;
	Toolbar toolBar;
	AppBarLayout appBarLayout;
	TextView toolbarTitle;
	RecyclerView recyclerView;
	GridLayoutManager gridLayoutManager;
	LinearLayoutManager linearLayoutManager;
	ShopActivityListAdapter adapter;
	int appBarLayoutHeight, filterLayoutHeight;
	public static final int TRANSLATION_DURATION = 200;
	ArrayList<HomeTopRatedBookObject> mData;
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

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_layout);

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

		toolBar = (Toolbar) findViewById(R.id.toolbar);
		toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
		setSupportActionBar(toolBar);
		getSupportActionBar().setTitle("");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundColor(getResources()
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

		bookType = getIntent().getExtras().getInt("home_book_type");
		Toast.makeText(this, bookType + " type", Toast.LENGTH_SHORT).show();

		actionBarTitle = getIntent().getExtras().getString("actionbarname");
		toolbarTitle.setText(actionBarTitle);

		gridLayoutManager = new GridLayoutManager(this, 2);
		linearLayoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(gridLayoutManager);

		addData();

		adapter = new ShopActivityListAdapter(mData, this);
		recyclerView.setAdapter(adapter);
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

	private void addData() {
		mData = new ArrayList<>();
		mData.add(new HomeTopRatedBookObject(
				0,
				"This is long name here and it makes sense int his thashosjbsb sjknkdn ajskan ajsjn akjnskn jansjdn kajnskn ajnsk",
				50, false, ""));
		mData.add(new HomeTopRatedBookObject(0, "This is long name here an",
				50, false, ""));
		mData.add(new HomeTopRatedBookObject(
				0,
				"This is long name here and it makes sense int his thashosjbsb sjknkdn ajskan ajsjn akjns",
				50, false, ""));
		mData.add(new HomeTopRatedBookObject(0,
				"This is long name here and it makes sense int his thashosj",
				50, false, ""));
		mData.add(new HomeTopRatedBookObject(
				0,
				"This is long name here and it makes sense int his thashosjbsb sjknkdn ajskan ajsjn akjnskn jansjdn kajnskn ajnsk",
				50, false, ""));
		mData.add(new HomeTopRatedBookObject(
				0,
				"This is long name here and it makes sense int his thashosjbsb sjknkdn ajskan ajsjn akjns",
				50, false, ""));
		mData.add(new HomeTopRatedBookObject(
				0,
				"This is long name here and it makes sense int his thashosjbsb sjknkdn ajskan ajsjn akjnskn jansjdn kajnskn ajnsk",
				50, false, ""));
		mData.add(new HomeTopRatedBookObject(0,
				"This is long name here and it makes sense int his thashosj",
				50, false, ""));
		mData.add(new HomeTopRatedBookObject(
				0,
				"This is long name here and it makes sense int his thashosjbsb sjknkdn ajskan ajsjn akjnskn jansjdn kajnskn ajnsk",
				50, false, ""));
		mData.add(new HomeTopRatedBookObject(
				0,
				"This is long name here and it makes sense int his thashosjbsb sjknkdn ajskan ajsjn akjnskn jansjdn kajnskn ajnsk",
				50, false, ""));
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.changelistviewtype:
			if (currentDisplayType == BNC_SHOP_DISPLAY_TYPE_GRID) {
				currentDisplayType = BNC_SHOP_DISPLAY_TYPE_LIST;
				changeDisplayTypeButton
						.setImageResource(R.drawable.list_long_icon);
				adapter = new ShopActivityListAdapter(mData, this);
				recyclerView.setAdapter(adapter);
				recyclerView.setLayoutManager(linearLayoutManager);
			} else if (currentDisplayType == BNC_SHOP_DISPLAY_TYPE_LIST) {
				currentDisplayType = BNC_SHOP_DISPLAY_TYPE_LONG_LIST;
				changeDisplayTypeButton.setImageResource(R.drawable.grid_icon);
				adapter = new ShopActivityListAdapter(mData, this);
				recyclerView.setAdapter(adapter);
				recyclerView.setLayoutManager(linearLayoutManager);
			} else if (currentDisplayType == BNC_SHOP_DISPLAY_TYPE_LONG_LIST) {
				currentDisplayType = BNC_SHOP_DISPLAY_TYPE_GRID;
				changeDisplayTypeButton.setImageResource(R.drawable.list_icon);
				adapter = new ShopActivityListAdapter(mData, this);
				recyclerView.setAdapter(adapter);
				recyclerView.setLayoutManager(gridLayoutManager);
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
}