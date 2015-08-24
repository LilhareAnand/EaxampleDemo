package com.washhous.laundryapp;

import com.washhous.menudrawer.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	private static int SPLASH_TIME_OUT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (((MyApplication) getApplicationContext())
						.checkLoginStatus()) {
					Intent intent = new Intent(SplashActivity.this,
							MainActivity.class);
					startActivity(intent);

					SplashActivity.this.finish();

				} else {
					Intent intent = new Intent(SplashActivity.this,
							SignInOption.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}

			}
		}, SPLASH_TIME_OUT);

	}

}
