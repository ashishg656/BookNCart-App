package com.bookncart.app.application;

import android.app.Application;

import com.bookncart.app.utils.FontsOverride;

public class BApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		FontsOverride.setDefaultFont(getApplicationContext(), "DEFAULT",
				"fonts/Hind-Light.ttf");
		FontsOverride.setDefaultFont(getApplicationContext(), "MONOSPACE",
				"fonts/Hind-Light.ttf");
		FontsOverride.setDefaultFont(getApplicationContext(), "SERIF",
				"fonts/Hind-Light.ttf");
		FontsOverride.setDefaultFont(getApplicationContext(), "SANS_SERIF",
				"fonts/Hind-Light.ttf");

		// ZFontsOverride.setDefaultFont(this, "DEFAULT",
		// "fonts/Gentona-Light.ttf");
	}
}
