package com.washhous.menudrawer;

import java.util.ArrayList;
import java.util.Stack;

import com.parse.ParseUser;
import com.washhous.database.SessionManager;
import com.washhous.drawermenus.About;
import com.washhous.drawermenus.FAQ;
import com.washhous.drawermenus.MyCreditBalance;
import com.washhous.drawermenus.MyOrderBox;
import com.washhous.drawermenus.MyProfile;
import com.washhous.drawermenus.PlaceAnOrder;
import com.washhous.drawermenus.PriceList;
import com.washhous.drawermenus.Promotions;
import com.washhous.drawermenus.TermsAndConditions;
import com.washhous.laundryapp.MyApplication;
import com.washhous.laundryapp.R;
import com.washhous.laundryapp.SignInOption;
import com.washhous.tabsswipe.OrderSummaryNew;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener {

	private ActionBar actionBar;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private String[] navMenuTitles;
	private boolean flag1 = false;
    SessionManager sessionmanger;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// load slide menu items
         sessionmanger=new SessionManager(MainActivity.this);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#F5F5F5")));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		int titleId = Resources.getSystem().getIdentifier("action_bar_title",
				"id", "android");
		TextView actionText = (TextView) findViewById(titleId);

		actionText.setTextColor(getResources().getColor(R.color.blue));

		int drawerIcons[] = { R.drawable.profile, R.drawable.orders,
				R.drawable.balance, R.drawable.promotion, R.drawable.pricelist,
				R.drawable.faq, R.drawable.aboutwash, R.drawable.terms,
				R.drawable.logout };
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navDrawerItems = new ArrayList<NavDrawerItem>();
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], drawerIcons[1]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], drawerIcons[2]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], drawerIcons[3],
				true, "22"));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], drawerIcons[4]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], drawerIcons[5]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], drawerIcons[6]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], drawerIcons[7]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], drawerIcons[8]));

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);

		moveDrawerToTop();
		initActionBar();
		initDrawer();
		// Quick cheat: Add Fragment 1 to default view
		onItemClick(null, null, 0, 0);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	private void moveDrawerToTop() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.decor,
				null); // "null" is important.

		// HACK: "steal" the first child of decor view
		ViewGroup decor = (ViewGroup) getWindow().getDecorView();
		View child = decor.getChildAt(0);
		decor.removeView(child);
		LinearLayout container = (LinearLayout) drawer
				.findViewById(R.id.drawer_content); // This is the container we
													// defined just now.
		container.addView(child, 0);
		drawer.findViewById(R.id.drawer).setPadding(0, getStatusBarHeight(), 0,
				0);

		// Make the drawer replace the first child
		decor.addView(drawer);
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	private int getContentIdResource() {
		return getResources().getIdentifier("content", "id", "android");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_drawer, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		mDrawerToggle.syncState();
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	private void initDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer);
		mDrawerLayout.setDrawerListener(createDrawerToggle());
		// ListAdapter adapter = (ListAdapter)(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1,
		// getResources().getStringArray(R.array.nav_items)));
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(this);
	}

	private DrawerListener createDrawerToggle() {
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.menu, R.string.drawer_open, R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

			}

			@Override
			public void onDrawerStateChanged(int state) {
			}
		};
		return mDrawerToggle;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mDrawerLayout.closeDrawer(mDrawerList);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ftx = fragmentManager.beginTransaction();

		switch (position) {

		case 0:
			if (flag1 == false) {
				ftx.replace(R.id.main_content, new PlaceAnOrder());
				flag1 = true;
			} else {
				Intent myProfile = new Intent(MainActivity.this,
						MyProfile.class);
				startActivity(myProfile);
				// ftx.replace(R.id.main_content, new MyProfile());
			}

			break;

		case 1:
			Intent myOrderBox = new Intent(MainActivity.this, MyOrderBox.class);
			startActivity(myOrderBox);
			finish();
			// ftx.replace(R.id.main_content, new MyOrderBox());
			break;

		case 2:
			Intent myCreditBalance = new Intent(MainActivity.this,
					MyCreditBalance.class);
			startActivity(myCreditBalance);
			finish();
			// ftx.replace(R.id.main_content, new MyCreditBalance());
			break;

		case 3:
			Intent promotions = new Intent(MainActivity.this, Promotions.class);
			startActivity(promotions);
			finish();
			// ftx.replace(R.id.main_content, new Promotions());
			break;

		case 4:
			Intent priceList = new Intent(MainActivity.this, PriceList.class);
			startActivity(priceList);
			finish();
			// ftx.replace(R.id.main_content, new PriceList());
			break;

		case 5:
			Intent fAQ = new Intent(MainActivity.this, FAQ.class);
			startActivity(fAQ);
			finish();
			// ftx.replace(R.id.main_content, new FAQ());
			
			break;

		case 6:
			Intent about = new Intent(MainActivity.this, About.class);
			startActivity(about);
			finish();
			break;

		case 7:
			Intent termsAndConditions = new Intent(MainActivity.this,
					TermsAndConditions.class);
			startActivity(termsAndConditions);
			finish();
			// ftx.replace(R.id.main_content, new TermsAndConditions());
			break;
		case 8:
			ParseUser.logOut();
			sessionmanger.logoutUser();
			finish();
			Intent intent = new Intent(MainActivity.this, SignInOption.class);
			startActivity(intent);
			((MyApplication) getApplicationContext()).saveLoginStatus(false);
			break;
		case 9:
			Fragment fragment = new PlaceAnOrder();
			break;
		}

		ftx.commit();
	}

	@Override
	public void onBackPressed() {

		// super.onBackPressed();

		Stack<Fragment> stk = ((MyApplication) getApplication())
				.getFragmentStack();
		if (stk.size() <= 1) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Exit Application?");
			alertDialogBuilder
					.setMessage("Do you want to close the application?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}

		if (stk.size() >= 2) {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			stk.lastElement().onPause();
			ft.remove(stk.pop());
			stk.lastElement().onResume();
			ft.show(stk.lastElement());
			ft.commit();

		}

	}

}
