package com.bookncart.app.activities;

import java.util.ArrayList;

import com.bookncart.app.R;
import com.bookncart.app.adapters.ShopActivityListAdapter;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.objects.HomeTopRatedBookObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_layout);

		recyclerView = (RecyclerView) findViewById(R.id.shop_recycler_view);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
		changeDisplayTypeButton = (ImageView) findViewById(R.id.changelistviewtype);

		toolBar = (Toolbar) findViewById(R.id.toolbar);
		toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
		setSupportActionBar(toolBar);
		getSupportActionBar().setTitle("");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundColor(getResources()
				.getColor(R.color.PrimaryColor));

		changeDisplayTypeButton.setOnClickListener(this);

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

		default:
			break;
		}
	}
}
