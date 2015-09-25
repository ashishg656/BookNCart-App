package com.bookncart.app.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.bookncart.app.R;

public class SelectAddressActivity extends BaseActivity {

	CoordinatorLayout coordinatorLayout;
	Snackbar snackbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_address_activity_layout);

		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinatorhome);

		snackbar = Snackbar
				.make(coordinatorLayout,
						"Currently only CASH ON DELIVERY payment mode is available",
						Snackbar.LENGTH_INDEFINITE)
				.setActionTextColor(
						getResources()
								.getColor(R.color.bnc_green_color_primary))
				.setAction("Okay", new OnClickListener() {

					@Override
					public void onClick(View v) {
						snackbar.dismiss();
					}
				});
		snackbar.show();
	}
}
