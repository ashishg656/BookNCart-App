package com.bookncart.app.animations;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bookncart.app.R;

public class ProgressLayoutAnimation {

	View darkCircle, lightCircle;
	ImageView image;
	FrameLayout mainLayout;

	Context context;

	public ProgressLayoutAnimation(View darkCircle, View lightCircle,
			ImageView image, FrameLayout mainLayout, Context context) {
		super();
		this.darkCircle = darkCircle;
		this.lightCircle = lightCircle;
		this.mainLayout = mainLayout;
		this.image = image;
		this.context = context;
	}

	public void startAnimations() {
		mainLayout.setVisibility(View.VISIBLE);
		Animation imageAnim = AnimationUtils.loadAnimation(context,
				R.anim.progress_layout_image_rotate_anim);
		image.startAnimation(imageAnim);

		Animation darkCircleAnim = AnimationUtils.loadAnimation(context,
				R.anim.progress_layout_dark_circle_anim);
		darkCircle.startAnimation(darkCircleAnim);

		Animation lightCircleAnim = AnimationUtils.loadAnimation(context,
				R.anim.progress_layout_light_circle_anim);
		lightCircle.startAnimation(lightCircleAnim);
	}

	public void stopAnimations() {
		darkCircle.clearAnimation();
		lightCircle.clearAnimation();
		image.clearAnimation();
		mainLayout.setVisibility(View.GONE);
	}
}
