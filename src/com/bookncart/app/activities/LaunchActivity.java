package com.bookncart.app.activities;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.fragments.LaunchScreen1Fragment;
import com.bookncart.app.fragments.LaunchScreen4Fragment;
import com.bookncart.app.widgets.CirclePageIndicator;

@SuppressLint("NewApi")
public class LaunchActivity extends AppCompatActivity implements
		OnPageChangeListener, OnClickListener {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_activity_layout);

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

		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			Scroller scroller = new Scroller(this,
					new DecelerateInterpolator(), true);
			mScroller.set(viewPager, scroller);
		} catch (Exception e) {
		}

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
			startActivity(i);
			break;

		default:
			break;
		}
	}
}