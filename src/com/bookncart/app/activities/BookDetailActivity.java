package com.bookncart.app.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.adapters.ProductDetailSimilarBooksRecyclerAdapter;
import com.bookncart.app.objects.HomeTopRatedBookObject;
import com.bookncart.app.widgets.ObservableScrollView;
import com.bookncart.app.widgets.ObservableScrollViewListener;
import com.bookncart.app.widgets.RecyclerViewHorizontalScrolling;

@SuppressLint("NewApi")
public class BookDetailActivity extends AppCompatActivity implements
		ObservableScrollViewListener {

	Toolbar toolbarWhiteIcon;
	int bookId;
	ObservableScrollView scrollView;
	ImageView bookImage;
	int deviceWidth;
	TextView toolbarTitle;
	LinearLayout buyNowContainerLayoutFake, buyNowContainerLayoutOriginal;
	RecyclerViewHorizontalScrolling recyclerView;
	LinearLayoutManager layoutManager;
	ProductDetailSimilarBooksRecyclerAdapter adapter;
	ArrayList<HomeTopRatedBookObject> similarBooksData;
	View bookImageDividerView;
	int statusBarHeight, toolbarHeight;
	LinearLayout transparentToolbar;
	FrameLayout appbarContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_detail_activity_layout);

		toolbarWhiteIcon = (Toolbar) findViewById(R.id.toolbar);
		scrollView = (ObservableScrollView) findViewById(R.id.bookdetailscrollview);
		bookImage = (ImageView) findViewById(R.id.book_image_detail_activity);
		toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
		buyNowContainerLayoutFake = (LinearLayout) findViewById(R.id.buynowaddtocartfake);
		buyNowContainerLayoutOriginal = (LinearLayout) findViewById(R.id.buynowaddtocartorig);
		recyclerView = (RecyclerViewHorizontalScrolling) findViewById(R.id.home_recycler_view_toprated_similar_Details);
		bookImageDividerView = (View) findViewById(R.id.image_divider_view);
		transparentToolbar = (LinearLayout) findViewById(R.id.faketoolbarshopdetail);
		appbarContainer = (FrameLayout) findViewById(R.id.appbarcontainer);

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

		toolbarTitle.setText("Set book name here");

		appbarContainer.setTranslationY(-toolbarHeight);

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bookImage
				.getLayoutParams();
		params.height = deviceWidth
				- getResources().getDimensionPixelSize(
						R.dimen.bnc_toolbar_height);
		bookImage.setLayoutParams(params);

		scrollView.setScrollListnerer(this);

		// color extraction-
		extractColorFromImageAndFillColors();

		fillSimilarBooksDataInRecyclerView();
	}

	private void fillSimilarBooksDataInRecyclerView() {
		layoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.HORIZONTAL, false);
		recyclerView.setLayoutManager(layoutManager);

		similarBooksData = new ArrayList<>();
		for (int i = 0; i < 14; i++) {
			similarBooksData.add(new HomeTopRatedBookObject(0, "f", 50, true,
					""));
		}

		adapter = new ProductDetailSimilarBooksRecyclerAdapter(
				similarBooksData, this);
		recyclerView.setAdapter(adapter);
	}

	private void extractColorFromImageAndFillColors() {
		Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.book_sample);
		bookImage.setImageBitmap(myBitmap);
		Palette.from(myBitmap).generate(new PaletteAsyncListener() {

			@Override
			public void onGenerated(Palette p) {
				int colorLight = p.getDarkMutedColor(0x000000);
				int colorDark = p.getDarkVibrantColor(0x000000);
				toolbarWhiteIcon.setBackgroundColor(colorLight);
				setStatusBarColor(colorDark);
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	void setStatusBarColor(int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(color);
		}
	}

	@Override
	public void onScrollStopped() {

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
}
