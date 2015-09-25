package com.bookncart.app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.bookncart.app.R;
import com.bookncart.app.fragments.CartFragment;
import com.bookncart.app.fragments.WishlistFragment;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.widgets.PagerSlidingTabStrip;

@SuppressLint("NewApi")
public class WishlistAndCartActivity extends BaseActivity implements
		OnPageChangeListener, OnClickListener {

	public ViewPager viewPager;
	PagerSlidingTabStrip pagerSlidingTabStrip;
	MyPagerAdapter adapter;
	AppBarLayout appBarLayout;
	FloatingActionButton floatingActionButton;
	public static final int TRANSLATION_DURATION = 200;
	public CoordinatorLayout coordinatorLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wishlist_and_cart_activity_layout);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
		viewPager = (ViewPager) findViewById(R.id.pager_shopandcart);
		pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabstripcartwishlist);
		floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_cart);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinatorwishlist);

		floatingActionButton.setOnClickListener(this);

		toolbar.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						toolbarHeight = toolbar.getHeight();
					}
				});

		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setBackgroundColor(getResources()
				.getColor(R.color.PrimaryColor));

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		pagerSlidingTabStrip.setTextColor(getResources().getColor(
				R.color.bnc_white_transparency));
		pagerSlidingTabStrip.setViewPager(viewPager);

		try {
			if (getIntent().getExtras().getBoolean("wishlist")) {
				floatingActionButton.setTranslationY(getResources()
						.getDimensionPixelSize(R.dimen.bnc_button_height));
				viewPager.setCurrentItem(1);
			}
		} catch (Exception e) {
		}

		pagerSlidingTabStrip.setOnPageChangeListener(this);
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {

		String[] names = { "Cart", "Wishlist" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			Bundle bundle = new Bundle();
			if (pos == 0) {
				return CartFragment.newInstance(bundle);
			} else {
				return WishlistFragment.newInstance(bundle);
			}
		}

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return names[position];
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

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_IDLE) {
			if (viewPager.getCurrentItem() == 1) {
				floatingActionButton
						.animate()
						.translationY(
								getResources().getDimensionPixelSize(
										R.dimen.bnc_button_height))
						.setDuration(200).start();
			} else {
				floatingActionButton.animate().translationY(0).setDuration(200)
						.start();
			}
		} else {
			floatingActionButton
					.animate()
					.translationY(
							getResources().getDimensionPixelSize(
									R.dimen.bnc_button_height))
					.setDuration(200).start();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onPageScrolled(int pos, float positionOffset,
			int positionOffsetPixels) {

	}

	@SuppressLint("NewApi")
	@Override
	public void onPageSelected(int pos) {

	}

	@SuppressLint("NewApi")
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

	@SuppressLint("NewApi")
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
		if (firstChild.getTop() > pagerSlidingTabStrip.getHeight()) {
			appBarLayout.animate().translationY(0)
					.setDuration(TRANSLATION_DURATION)
					.setInterpolator(new DecelerateInterpolator());
		} else {
			scrollToolbarAfterTouchEnds();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fab_cart:
			openConfirmOrderActivity();
			break;

		default:
			break;
		}
	}

	public void openConfirmOrderActivity() {
		if (ZPreferences.isUserLogIn(this)) {
			Intent i = new Intent(this, ConfirmOrderActivity.class);
			startActivity(i);
		} else {
			Intent i = new Intent(this, LaunchActivity.class);
			startActivity(i);
			Toast.makeText(this, "You need to login before placing an order.",
					Toast.LENGTH_SHORT).show();
		}
	}
}
