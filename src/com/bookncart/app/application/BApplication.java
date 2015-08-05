package com.bookncart.app.application;

import android.app.Application;

import com.bookncart.app.utils.ZFontsOverride;

public class BApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ZFontsOverride.setDefaultFont(getApplicationContext(), "DEFAULT",
				"fonts/Hind-Light.ttf");
		ZFontsOverride.setDefaultFont(getApplicationContext(), "MONOSPACE",
				"fonts/Hind-Light.ttf");
		ZFontsOverride.setDefaultFont(getApplicationContext(), "SERIF",
				"fonts/Hind-Light.ttf");
		ZFontsOverride.setDefaultFont(getApplicationContext(), "SANS_SERIF",
				"fonts/Hind-Light.ttf");

		// ZFontsOverride.setDefaultFont(this, "DEFAULT",
		// "fonts/Gentona-Light.ttf");
	}
}
