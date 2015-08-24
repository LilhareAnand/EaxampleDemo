package com.washhous.drawermenus;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.washhous.laundryapp.OrderOptionWashAndIron;
import com.washhous.laundryapp.R;
import com.washhous.laundryapp.SignInOption;
import com.washhous.laundryapp.SignUp;
import com.washhous.menudrawer.MainActivity;

public class MyOrderBox extends Activity {
	private View rootView;
	private ImageButton imgBtnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order_box);

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar_order, null);
		imgBtnBack = (ImageButton) mCustomView
				.findViewById(R.id.btn_img_back_order);
		imgBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyOrderBox.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// initControls();
		// getSharedPreference();
		// getActionBar().setTitle("Order Options");
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_order_box, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent intentSignIN = new Intent(MyOrderBox.this, MainActivity.class);
		startActivity(intentSignIN);
		finish();
	}

}
