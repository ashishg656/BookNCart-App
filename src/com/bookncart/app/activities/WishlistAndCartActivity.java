package com.bookncart.app.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.bookncart.app.R;
import com.bookncart.app.fragments.CartFragment;
import com.bookncart.app.fragments.WishlistFragment;
import com.bookncart.app.widgets.PagerSlidingTabStrip;

public class WishlistAndCartActivity extends AppCompatActivity {

	ViewPager viewPager;
	PagerSlidingTabStrip pagerSlidingTabStrip;
	Toolbar toolbar;
	int toolbarHeight;
	MyPagerAdapter adapter;
	AppBarLayout appBarLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wishlist_and_cart_activity_layout);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
		viewPager = (ViewPager) findViewById(R.id.pager_shopandcart);
		pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabstripcartwishlist);

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
				viewPager.setCurrentItem(1);
			}
		} catch (Exception e) {
		}
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
}