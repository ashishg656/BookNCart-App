package com.bookncart.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.Toast;

import com.bookncart.app.R;
import com.bookncart.app.adapters.HomeActivityRecyclerViewAdapter;
import com.bookncart.app.adapters.SimilarBooksRecyclerViewAdapter;
import com.bookncart.app.extras.MyAnimatorListener;
import com.bookncart.app.objects.HomeActivityListObject;
import com.bookncart.app.objects.HomeTopRatedBookObject;
import com.bookncart.app.objects.RecentlyViewedBooksObject;
import com.bookncart.app.objects.TagObject;
import com.bookncart.app.widgets.AnimatorUtils;
import com.bookncart.app.widgets.TouchEventInterceptLinearLayout;

@SuppressLint("NewApi")
public class HomeActivity extends AppCompatActivity implements OnDragListener {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	Toolbar toolbar;
	NavigationView navigationView;
	DrawerLayout drawerLayout;
	HomeActivityRecyclerViewAdapter adapter;
	HomeActivityListObject mData;
	FrameLayout buttonsLayout, mainContentFrame;
	ImageView favouriteButton, thumbImageView, shareButton, similarButton;
	int radius, deviceWidth, thumbImageWidth, cornerAllowance, deviceHeight;
	float angle, angleCorrection, x, y, leftx, lefty, rightx, righty, touchx,
			touchy;
	int toolbarHeight;
	public static final int TRANSLATION_DURATION = 200;
	final float scaleFactorForSimilarBooksLayout = 0.6f;
	AppBarLayout appBarLayout;
	ImageView similarBooksBackgroundView;
	TouchEventInterceptLinearLayout similarBooksLayout;
	FrameLayout similarBooksContainerLayout;
	public RecyclerView similarBooksRecyclerView;
	boolean isSimilarBooksLayoutCompletetlyVisible = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);

		recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		buttonsLayout = (FrameLayout) findViewById(R.id.buttons_layout);
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

		addData();

		adapter = new HomeActivityRecyclerViewAdapter(this, mData);
		recyclerView.setAdapter(adapter);

		favouriteButton.setOnDragListener(this);
		similarButton.setOnDragListener(this);
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
	}

	private void addData() {
		ArrayList<String> headerImages = new ArrayList<>();
		headerImages.add("");
		headerImages.add("");
		headerImages.add("");
		headerImages.add("");
		headerImages.add("");
		ArrayList<String> categoriesImages = new ArrayList<>();
		categoriesImages.add("");
		categoriesImages.add("");
		categoriesImages.add("");
		ArrayList<Integer> categoriesImagesId = new ArrayList<>();
		categoriesImagesId.add(0);
		categoriesImagesId.add(0);
		categoriesImagesId.add(0);
		ArrayList<HomeTopRatedBookObject> topRatedBooks = new ArrayList<>();
		topRatedBooks.add(new HomeTopRatedBookObject(0, "", 50, false, ""));
		topRatedBooks.add(new HomeTopRatedBookObject(0, "", 50, false, ""));
		topRatedBooks.add(new HomeTopRatedBookObject(0, "", 50, false, ""));
		topRatedBooks.add(new HomeTopRatedBookObject(0, "", 50, false, ""));
		topRatedBooks.add(new HomeTopRatedBookObject(0, "", 50, false, ""));
		topRatedBooks.add(new HomeTopRatedBookObject(0, "", 50, false, ""));
		topRatedBooks.add(new HomeTopRatedBookObject(0, "", 50, false, ""));
		topRatedBooks.add(new HomeTopRatedBookObject(0, "", 50, false, ""));
		topRatedBooks.add(new HomeTopRatedBookObject(0, "", 50, false, ""));
		ArrayList<RecentlyViewedBooksObject> recentlyViewedBooks = new ArrayList<>();
		recentlyViewedBooks.add(new RecentlyViewedBooksObject(0, "", 50, false,
				""));
		recentlyViewedBooks.add(new RecentlyViewedBooksObject(0, "", 50, false,
				""));
		recentlyViewedBooks.add(new RecentlyViewedBooksObject(0, "", 50, false,
				""));
		recentlyViewedBooks.add(new RecentlyViewedBooksObject(0, "", 50, false,
				""));
		recentlyViewedBooks.add(new RecentlyViewedBooksObject(0, "", 50, false,
				""));
		recentlyViewedBooks.add(new RecentlyViewedBooksObject(0, "", 50, false,
				""));
		recentlyViewedBooks.add(new RecentlyViewedBooksObject(0, "", 50, false,
				""));
		ArrayList<TagObject> tags = new ArrayList<>();
		tags.add(new TagObject(0, ""));
		tags.add(new TagObject(0, ""));
		tags.add(new TagObject(0, ""));
		tags.add(new TagObject(0, ""));
		tags.add(new TagObject(0, ""));
		tags.add(new TagObject(0, ""));
		tags.add(new TagObject(0, ""));
		tags.add(new TagObject(0, ""));
		tags.add(new TagObject(0, ""));
		tags.add(new TagObject(0, ""));
		mData = new HomeActivityListObject(headerImages, categoriesImages,
				categoriesImagesId, topRatedBooks, topRatedBooks,
				topRatedBooks, topRatedBooks, recentlyViewedBooks,
				topRatedBooks, tags);
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
						case R.id.drawer_feeds:
							Toast.makeText(HomeActivity.this, "feeds select",
									Toast.LENGTH_SHORT).show();
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
		// if (id == R.id.action_cart) {
		// return true;
		// } else if (id == R.id.action_search) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	public void showButtonsLayout() {
		buttonsLayout.setVisibility(View.VISIBLE);
	}

	public void setButtonsLocation(View v, final DragEvent event) {
		angle = 270;
		angleCorrection = 15;

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

	@Override
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
				Toast.makeText(this, "fav button", Toast.LENGTH_SHORT).show();
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

		default:
			break;
		}
		return true;
	}

	private void hideSimilarButtonsLayout() {
		isSimilarBooksLayoutCompletetlyVisible = false;
		appBarLayout.setAlpha(255);
		similarBooksBackgroundView.setImageAlpha(0);
		ObjectAnimator recyclerAnimator1 = ObjectAnimator.ofFloat(recyclerView,
				"scaleX", 1);
		ObjectAnimator recyclerAnimator2 = ObjectAnimator.ofFloat(recyclerView,
				"scaleY", 1);
		ObjectAnimator animTrans = ObjectAnimator.ofFloat(similarBooksLayout,
				"translationY", deviceHeight);
		ObjectAnimator animToolbar = ObjectAnimator.ofArgb(
				toolbar.getBackground(), "alpha", 255);

		List<Animator> animSetList = new ArrayList<>();
		animSetList.add(animTrans);
		animSetList.add(recyclerAnimator1);
		animSetList.add(recyclerAnimator2);
		animSetList.add(animToolbar);

		AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(700);
		animSet.setInterpolator(new AccelerateDecelerateInterpolator());
		animSet.addListener(new MyAnimatorListener() {

			@Override
			public void onAnimationEnd(Animator animation) {
				similarBooksContainerLayout.setVisibility(View.GONE);
			}
		});
		animSet.playTogether(animSetList);
		animSet.start();
	}

	private void showSimilarBooksLayout() {
		isSimilarBooksLayoutCompletetlyVisible = false;
		appBarLayout.setTranslationY(0);
		appBarLayout.setAlpha(0);
		similarBooksContainerLayout.setVisibility(View.VISIBLE);
		similarBooksBackgroundView.setImageAlpha(0);
		recyclerView.setPivotY(0);

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

		AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(700);
		animSet.setInterpolator(new AccelerateDecelerateInterpolator());
		animSet.playTogether(animSetList);
		animSet.start();

		addDataInSimilarBooksRecyclerView();
	}

	private void addDataInSimilarBooksRecyclerView() {
		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
		similarBooksRecyclerView.setLayoutManager(gridLayoutManager);

		ArrayList<HomeTopRatedBookObject> similarBooksData = new ArrayList<>();
		similarBooksData.add(new HomeTopRatedBookObject(0,
				"ashsh fjkvnjkfkvn dkcnkv kdcnk", 50, false, ""));
		similarBooksData.add(new HomeTopRatedBookObject(0,
				"ashsh fjkvnjkfkvn dkcnkv kdcnk", 50, false, ""));
		similarBooksData.add(new HomeTopRatedBookObject(0,
				"ashsh fjkvnjkfkvn dkcnkv kdcnk", 50, false, ""));
		similarBooksData.add(new HomeTopRatedBookObject(0,
				"ashsh fjkvnjkfkvn dkcnkv kdcnk", 50, false, ""));
		similarBooksData.add(new HomeTopRatedBookObject(0,
				"ashsh fjkvnjkfkvn dkcnkv kdcnk", 50, false, ""));
		similarBooksData.add(new HomeTopRatedBookObject(0,
				"ashsh fjkvnjkfkvn dkcnkv kdcnk", 50, false, ""));
		similarBooksData.add(new HomeTopRatedBookObject(0,
				"ashsh fjkvnjkfkvn dkcnkv kdcnk", 50, false, ""));
		similarBooksData.add(new HomeTopRatedBookObject(0,
				"ashsh fjkvnjkfkvn dkcnkv kdcnk", 50, false, ""));
		SimilarBooksRecyclerViewAdapter adapterSimilarBooks = new SimilarBooksRecyclerViewAdapter(
				similarBooksData, this);
		similarBooksRecyclerView.setAdapter(adapterSimilarBooks);

		similarBooksBackgroundView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toolbar.getBackground().setAlpha(0);
				hideSimilarButtonsLayout();
			}
		});

		similarBooksLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					if (similarBooksLayout.getTranslationY() > deviceHeight
							* scaleFactorForSimilarBooksLayout) {
						hideSimilarButtonsLayout();
					} else {
						if (isSimilarBooksLayoutCompletetlyVisible) {
							isSimilarBooksLayoutCompletetlyVisible = false;
							ObjectAnimator animTrans = ObjectAnimator.ofFloat(
									similarBooksLayout, "translationY",
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
							Log.w("as", "down negative");
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
						float requestedTrans = similarBooksLayout
								.getTranslationY() + dy;
						if (requestedTrans > deviceHeight)
							requestedTrans = deviceHeight;
						else if (requestedTrans < 0) {
							requestedTrans = 0;
							isSimilarBooksLayoutCompletetlyVisible = true;
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
		});
	}

	@Override
	public void onBackPressed() {
		if (similarBooksContainerLayout.getVisibility() == View.VISIBLE) {
			toolbar.getBackground().setAlpha(0);
			hideSimilarButtonsLayout();
		} else {
			super.onBackPressed();
		}
	}
}