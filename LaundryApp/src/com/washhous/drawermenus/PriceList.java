package com.washhous.drawermenus;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.washhous.comman.CommanMethods;
import com.washhous.dataclasses.DryCleaningItems;
import com.washhous.dataclasses.HouseHoldItem;
import com.washhous.dataclasses.InventoryCheckList;
import com.washhous.dataclasses.IornItems;
import com.washhous.laundryapp.MyApplication;
import com.washhous.laundryapp.OrderOptionWashAndIron;
import com.washhous.laundryapp.R;
import com.washhous.menudrawer.MainActivity;
import com.washhous.parse.AdapterDryCleaning;
import com.washhous.parse.AdapterWashAndFold;
import com.washhous.parse.AdapterWashAndIron;
import com.washhous.parse.ExpandListView;

public class PriceList extends FragmentActivity {
	private View rootView;
	private ExpandListView lvWashAndFold;
	private ExpandListView lvWashAndIron;
	private ExpandListView lvDryCleaning;
	ProgressDialog pDialog = null;

	public List<ParseObject> dryCleaningObject;
	public List<ParseObject> houseHoldItemsObject;
	public List<ParseObject> ironItemsObject;

	List<DryCleaningItems> dryCleaningItemsList = new ArrayList<DryCleaningItems>();
	List<HouseHoldItem> washAndFoldItemList = new ArrayList<HouseHoldItem>();
	List<IornItems> iornItemsList = new ArrayList<IornItems>();

	private AdapterWashAndFold dataAdapter;
	private AdapterWashAndIron dataAdapterIron;
	private AdapterDryCleaning dataAdapterDryClean;
	private ImageButton imgBtnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_price_list);
		initControls();

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
		View mCustomView = mInflater.inflate(R.layout.actionbar_price_list,
				null);
		imgBtnBack = (ImageButton) mCustomView
				.findViewById(R.id.btn_img_back_price_list);
		imgBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PriceList.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
		callMethodToGetData();

	}

	public void callMethodToGetData() {
		washAndFoldItemList = MyApplication.washAndFoldItemList;
		iornItemsList = MyApplication.iornItemsList;
		dryCleaningItemsList = MyApplication.dryCleaningItemsList;
		if (washAndFoldItemList.size() < 1 || iornItemsList.size() < 1
				|| dryCleaningItemsList.size() < 1) {
			if (CommanMethods.isConnected(getApplicationContext())) {
				new RemoteDataTask().execute();

			}
		} else {
			setWashAndFoldAdapter();
		}

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(PriceList.this);
			pDialog.setMessage("Please Wait... Fetching List"); // typically

			// will define
			// such
			// strings in a remote file.
			pDialog.setCancelable(true);
			pDialog.setIndeterminate(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// Locate the class table named "Country" in Parse.com

			ParseQuery<ParseObject> dryCleaning = new ParseQuery<ParseObject>(
					"DryCleaningItems");
			ParseQuery<ParseObject> houseHoldItems = new ParseQuery<ParseObject>(
					"HouseHoldItems");

			ParseQuery<ParseObject> ironItems = new ParseQuery<ParseObject>(
					"IronItems");

			try {

				dryCleaningObject = dryCleaning.find();
				houseHoldItemsObject = houseHoldItems.find();

				ironItemsObject = ironItems.find();

			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			for (ParseObject dryCleaning : dryCleaningObject) {
				// stateList.add((String) stateName.get("Name"));

				DryCleaningItems dryCleaningList = new DryCleaningItems();
				dryCleaningList.setItemName(dryCleaning.get("ItemName")
						.toString());
				dryCleaningList.setPrice(dryCleaning.get("Price").toString());
				dryCleaningItemsList.add(dryCleaningList);

			}

			for (ParseObject houseHoldItems : houseHoldItemsObject) {
				// stateList.add((String) stateName.get("Name"));

				HouseHoldItem houseHoldItem = new HouseHoldItem();
				houseHoldItem.setItemName(houseHoldItems.get("ItemName")
						.toString());
				houseHoldItem.setPrice(houseHoldItems.get("Price").toString());
				washAndFoldItemList.add(houseHoldItem);

			}

			for (ParseObject ironItems : ironItemsObject) {
				// stateList.add((String) stateName.get("Name"));

				IornItems iornItem = new IornItems();

				iornItem.setItemName(ironItems.get("ItemName").toString());
				iornItem.setPrice(ironItems.get("Price").toString());
				iornItemsList.add(iornItem);
			}

			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			setWashAndFoldAdapter();

		}
	}

	private void initControls() {
		lvWashAndFold = (ExpandListView) findViewById(R.id.lv_wash_and_fold);
		lvWashAndIron = (ExpandListView) findViewById(R.id.lv_wash_and_iorn);
		lvDryCleaning = (ExpandListView) findViewById(R.id.lv_dry_cleaning);
	}

	public void setWashAndFoldAdapter() {

		dataAdapter = new AdapterWashAndFold(PriceList.this,
				washAndFoldItemList);

		lvWashAndFold.setAdapter(dataAdapter);
		lvWashAndFold.setExpanded(true);

		dataAdapterIron = new AdapterWashAndIron(PriceList.this, iornItemsList);
		lvWashAndIron.setAdapter(dataAdapterIron);
		lvWashAndIron.setExpanded(true);

		dataAdapterDryClean = new AdapterDryCleaning(PriceList.this,
				dryCleaningItemsList);
		lvDryCleaning.setAdapter(dataAdapterDryClean);
		lvDryCleaning.setExpanded(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.price_list, menu);
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
		Intent intentSignIN = new Intent(PriceList.this, MainActivity.class);
		startActivity(intentSignIN);
		finish();
	}
}
