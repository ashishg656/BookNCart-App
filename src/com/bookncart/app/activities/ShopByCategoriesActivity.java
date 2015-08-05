package com.bookncart.app.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bookncart.app.R;
import com.bookncart.app.circularreveal.SupportAnimator;
import com.bookncart.app.circularreveal.ViewAnimationUtils;
import com.bookncart.app.extras.MyAnimationListener;
import com.bookncart.app.fragments.ShopByCategorySelectSemesterFragment;
import com.bookncart.app.notboringactionbar.KenBurnsSupportView;
import com.bookncart.app.objects.ShopByCategoriesObject;
import com.bookncart.app.objects.ShopByCategoriesSingleCategoryObject;
import com.bookncart.app.widgets.PagerSlidingTabStrip;

@SuppressLint("NewApi")
public class ShopByCategoriesActivity extends AppCompatActivity implements
		OnPageChangeListener {

	Toolbar toolbar;
	FrameLayout mainHeaderLayout;
	int categoryPos;
	PagerSlidingTabStrip pagerTabStrip;
	public ArrayList<ShopByCategoriesObject> mData;
	ViewPager viewPager;
	MyPagerAdapter adapter;
	ImageView circularHeaderImage;
	int initialToolbarBottom, toolbarHeight;
	float minimumHeaderTranslation;
	KenBurnsSupportView kenBurnsSupportView;
	View kenburnsImageBg, circularRevealView;
	int maxHeaderHeight, maxHeaderWidth;
	private int CIRCULAR_REVEAL_ANIMATION_DURATION = 1000;
	HashMap<Integer, Fragment> hashmapFragments;
	private boolean isCircularRevealShown = true;
	SupportAnimator animator;
	int alpha;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_by_categories_activity_layout);

		hashmapFragments = new HashMap<>();

		setStatusBarColor(getResources().getColor(
				R.color.bnc_shop_by_category_color1));

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pagerTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tab_strip_category);
		viewPager = (ViewPager) findViewById(R.id.viewpagecategory);
		circularHeaderImage = (ImageView) findViewById(R.id.header_imageview);
		mainHeaderLayout = (FrameLayout) findViewById(R.id.frame_layout_header_container);
		kenBurnsSupportView = (KenBurnsSupportView) findViewById(R.id.kenburnssupportview);
		kenBurnsSupportView.setResourceIds(R.drawable.book1, R.drawable.book2);
		kenburnsImageBg = (View) findViewById(R.id.image_overlay_bg);
		circularRevealView = (View) findViewById(R.id.circular_reveal_view);

		toolbar.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		toolbar.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						toolbar.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						Rect rectToolbar = new Rect();
						toolbar.getGlobalVisibleRect(rectToolbar);
						initialToolbarBottom = rectToolbar.bottom;
						toolbarHeight = toolbar.getHeight();
					}
				});

		mainHeaderLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						int minHeaderHeight = getResources()
								.getDimensionPixelSize(
										R.dimen.bnc_button_height)
								+ getResources().getDimensionPixelSize(
										R.dimen.bnc_margin_mini);
						maxHeaderHeight = mainHeaderLayout.getHeight();
						maxHeaderWidth = mainHeaderLayout.getWidth();
						minimumHeaderTranslation = maxHeaderHeight
								- minHeaderHeight;
					}
				});

		addData();

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(3);
		pagerTabStrip.setTextColor(getResources().getColor(R.color.bnc_white));
		pagerTabStrip.setViewPager(viewPager);
		pagerTabStrip.setOnPageChangeListener(this);

		try {
			categoryPos = getIntent().getExtras().getInt("categorypos");
			if (categoryPos != 0)
				isCircularRevealShown = false;
			viewPager.setCurrentItem(categoryPos);
		} catch (Exception e) {
		}
	}

	private void addData() {
		mData = new ArrayList<>();
		ArrayList<ShopByCategoriesSingleCategoryObject> subCategories = new ArrayList<>();
		subCategories.add(new ShopByCategoriesSingleCategoryObject("", 0, 0,
				false, false, ""));
		subCategories.add(new ShopByCategoriesSingleCategoryObject("", 0, 0,
				false, false, ""));
		subCategories.add(new ShopByCategoriesSingleCategoryObject("", 0, 0,
				false, false, ""));
		subCategories.add(new ShopByCategoriesSingleCategoryObject("", 0, 0,
				false, false, ""));
		subCategories.add(new ShopByCategoriesSingleCategoryObject("", 0, 0,
				false, false, ""));
		subCategories.add(new ShopByCategoriesSingleCategoryObject("", 0, 0,
				false, false, ""));
		subCategories.add(new ShopByCategoriesSingleCategoryObject("", 0, 0,
				false, false, ""));
		subCategories.add(new ShopByCategoriesSingleCategoryObject("", 0, 0,
				false, false, ""));
		mData.add(new ShopByCategoriesObject(subCategories, "computer", 0, ""));
		mData.add(new ShopByCategoriesObject(subCategories, "computer", 0, ""));
		mData.add(new ShopByCategoriesObject(subCategories, "computer", 0, ""));
	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			Bundle bundle = new Bundle();
			bundle.putInt("position", pos);
			ShopByCategorySelectSemesterFragment frag = ShopByCategorySelectSemesterFragment
					.newInstance(bundle);
			hashmapFragments.put(pos, frag);
			return hashmapFragments.get(pos);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mData.get(position).getCategoryName();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@SuppressLint("NewApi")
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		makeHeightsOfRecyclerViewsEqualOnPageScroll();
	}

	private void makeHeightsOfRecyclerViewsEqualOnPageScroll() {
		Rect rect = new Rect();
		mainHeaderLayout.getLocalVisibleRect(rect);
		int headerLoc = mainHeaderLayout.getHeight() - rect.top - rect.bottom;
		for (int i = 0; i < 3; i++) {
			((ShopByCategorySelectSemesterFragment) hashmapFragments.get(i)).layoutManager
					.scrollToPositionWithOffset(0, headerLoc);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onPageSelected(int pos) {
		if (pos == 0) {
			pageSelectedAnimation(
					getResources()
							.getColor(R.color.bnc_shop_by_category_color1),
					getResources().getColor(
							R.color.bnc_shop_by_category_color1_light),
					R.drawable.circular_bg_category_image_1,
					R.drawable.laptop,
					getResources().getColor(
							R.color.bnc_shop_by_category_color1_lightdiff));
		} else if (pos == 1) {
			pageSelectedAnimation(
					getResources()
							.getColor(R.color.bnc_shop_by_category_color2),
					getResources().getColor(
							R.color.bnc_shop_by_category_color2_light),
					R.drawable.circular_bg_category_image_2,
					R.drawable.lights,
					getResources().getColor(
							R.color.bnc_shop_by_category_color2_lightdiff));
		} else {
			pageSelectedAnimation(
					getResources()
							.getColor(R.color.bnc_shop_by_category_color3),
					getResources().getColor(
							R.color.bnc_shop_by_category_color3_light),
					R.drawable.circular_bg_category_image_3,
					R.drawable.worker,
					getResources().getColor(
							R.color.bnc_shop_by_category_color3_lightdiff));
		}
	}

	public void translateUp(int dy) {
		float requestedTrans = mainHeaderLayout.getTranslationY() - dy;
		if (requestedTrans < -minimumHeaderTranslation)
			requestedTrans = -minimumHeaderTranslation;
		else if (requestedTrans > 0)
			requestedTrans = 0;
		mainHeaderLayout.setTranslationY(requestedTrans);
		kenBurnsSupportView.setTranslationY(-requestedTrans / 2);

		Rect rectTabStrip = new Rect();
		pagerTabStrip.getGlobalVisibleRect(rectTabStrip);
		Rect rectToolbar = new Rect();
		toolbar.getGlobalVisibleRect(rectToolbar);
		if (rectTabStrip.top <= rectToolbar.bottom
				|| initialToolbarBottom > rectToolbar.bottom) {
			float requestTransToolbar = toolbar.getTranslationY() - dy;
			if (requestTransToolbar < -toolbarHeight)
				requestTransToolbar = -toolbarHeight;
			else if (requestTransToolbar > 0)
				requestTransToolbar = 0;
			toolbar.setTranslationY(requestTransToolbar);
		}
		alpha = maxHeaderHeight == 0 ? 255 : (int) (255 - 2 * (mainHeaderLayout
				.getTranslationY() / maxHeaderHeight * 255 * -1));
		if (alpha < 0)
			alpha = 0;
		else if (alpha > 255)
			alpha = 255;
		circularHeaderImage.setImageAlpha(alpha);
		circularHeaderImage.getBackground().setAlpha(alpha);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	void setStatusBarColor(int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(color);
		}
	}

	void pageSelectedAnimation(int color, int lightColor,
			final int circularimagebg, final int circularimage,
			final int circularrevealcolor) {
		setStatusBarColor(color);
		kenburnsImageBg.setBackgroundColor(lightColor);
		if (isCircularRevealShown) {
			makeHeightsOfRecyclerViewsEqualOnPageScroll();
			Animation anim = AnimationUtils.loadAnimation(this,
					R.anim.scale_down_anim);
			circularHeaderImage.startAnimation(anim);
			anim.setAnimationListener(new MyAnimationListener() {

				@Override
				public void onAnimationEnd(Animation animation) {
					circularHeaderImage.setBackgroundResource(circularimagebg);
					circularHeaderImage.setImageResource(circularimage);
					circularHeaderImage.setImageAlpha(alpha);
					circularHeaderImage.getBackground().setAlpha(alpha);
					Animation anim1 = AnimationUtils
							.loadAnimation(ShopByCategoriesActivity.this,
									R.anim.scale_up_anim);
					circularHeaderImage.startAnimation(anim1);
				}
			});

			int cx = (circularRevealView.getLeft() + circularRevealView
					.getRight()) / 2;
			int cy = (circularRevealView.getTop() + circularRevealView
					.getBottom()) / 2;
			int finalRadius = Math.max(maxHeaderHeight, maxHeaderWidth);

			animator = ViewAnimationUtils.createCircularReveal(
					circularRevealView, cx, cy, 0, finalRadius);
			animator.setInterpolator(new AccelerateDecelerateInterpolator());
			animator.setDuration(CIRCULAR_REVEAL_ANIMATION_DURATION);
			animator.addListener(new SupportAnimator.AnimatorListener() {
				@Override
				public void onAnimationStart() {
					Log.w("reveal", "animation start");
					circularRevealView.setBackgroundColor(circularrevealcolor);
				}

				@Override
				public void onAnimationRepeat() {
				}

				@Override
				public void onAnimationEnd() {
					Log.w("reveal", "animation end");
					circularRevealView.setBackgroundColor(getResources()
							.getColor(android.R.color.transparent));
				}

				@Override
				public void onAnimationCancel() {
					Log.w("reveal", "animation cancel");
				}
			});
			animator.start();
		} else {
			isCircularRevealShown = true;
			circularHeaderImage.setBackgroundResource(circularimagebg);
			circularHeaderImage.setImageResource(circularimage);
		}
	}
}