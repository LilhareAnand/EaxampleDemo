package com.washhous.laundryapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.washhous.comman.CommanMethods;
import com.washhous.comman.DC_InsertFields;
import com.washhous.comman.SharedDataPrefrence;
import com.washhous.database.SessionManager;
import com.washhous.database.SqlHelper;
import com.washhous.dataclasses.HouseHoldItem;
import com.washhous.parse.AdapterWashAndFoldList;
import com.washhous.parse.ExpandListView;

public class WashAndFoldListPage extends Activity {

	ProgressDialog pDialog = null;
	private AdapterWashAndFoldList dataAdapter;
	public List<ParseObject> houseHoldItemsObject;
	private ExpandListView lvWashAndFold;
	public static List<HouseHoldItem> washAndFoldItemList = new ArrayList<HouseHoldItem>();
	SqlHelper helper;
	private SharedPreferences sharedPref;
	Editor edit;
	private Button btnDone;
	SessionManager mseSessionManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wash_and_fold_list_page);

		initControls();
		mseSessionManager=new SessionManager(WashAndFoldListPage.this);
		sharedPref = getSharedPreferences("laundry", 0);
		helper = new SqlHelper(getApplicationContext());
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
		View mCustomView = mInflater.inflate(R.layout.actionbar_drawermenu,
				null);
		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.title_text);
		ImageButton imgBack = (ImageButton) mCustomView
				.findViewById(R.id.btn_img_cart1);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(WashAndFoldListPage.this,
						OrderOptionWashAndFold.class);

				startActivity(intent);
				finish();
				updateCount();

			}
		});

		mTitleTextView.setText("Select Household Items");
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
		washAndFoldItemList = MyApplication.washAndFoldItemList;

		if (washAndFoldItemList.size() < 1) {
			if (CommanMethods.isConnected(getBaseContext())) {
				// helper.deleteDataFromAllTables();
				new RemoteDataTask().execute();
			}
		} else {
			setWashAndFoldAdapter();
		}

	}

	protected void updateCount() {
		// helper.updateHouseholdCount();

	}

	private void insertHouseholdData() {
		
		SharedDataPrefrence pref=new SharedDataPrefrence();
		String ITEMcONUT=pref.getWashAndFoldHouseHoldItem_polycomfoeter_king(WashAndFoldListPage.this);
		Log.d("Count", "Count "+ITEMcONUT);
		ArrayList<String>Size,Name,price;
		Size=new ArrayList<String>();
		Name=new ArrayList<String>();
		price=new ArrayList<String>();
		Name.add("Ploy Comforter-King");
		price.add("25.00");
		Size.add(pref.getWashAndFoldHouseHoldItem_polycomfoeter_king(WashAndFoldListPage.this)+"");
		Name.add("Ploy Comforter-Queen");
		price.add("23.00");
		Size.add(pref.getWashAndFoldHouseHoldItemployComforterQueen(WashAndFoldListPage.this)+"");
		
		Name.add("Ploy Comforter-Double");
		price.add("21.00");
		Size.add(pref.getWashAndFoldHouseHoldItemployComfoerterDouble(WashAndFoldListPage.this)+"");
		
		Name.add("Down Comforter-Queen");
		price.add("29.27");
		Size.add(pref.getWashAndFoldHouseHoldItemDownnComforterQueen(WashAndFoldListPage.this)+"");
		
		Name.add("Pillow");
		price.add("10.00");
		Size.add(pref.getWashAndFoldHouseHoldItempillow(WashAndFoldListPage.this)+"");
		
		for(int i=0;i<Name.size();i++){
			ParseObject orderHead = new ParseObject("WashFold_Item");
			orderHead.put("OrderId", String.valueOf(MyApplication.orderId));
			orderHead.put("user_id", mseSessionManager.getemailid());
			orderHead.put("Household_Name", Name.get(i));
			orderHead.put("Item_Price", price.get(i));
			orderHead.put("Count", Size.get(i));
			Log.d("Name ", "Name  "+Name.get(i));
			Log.d("Count", "Count "+Size.get(i));
			orderHead.saveInBackground();
		}
		int counthoushold=helper.getHouseholdCount();
		mseSessionManager.address("housholdcount",String.valueOf(counthoushold));
	}

	private void initControls() {

		lvWashAndFold = (ExpandListView) findViewById(R.id.lv_wash_and_fold_items);
		btnDone = (Button) findViewById(R.id.btn_household_done);
		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WashAndFoldListPage.this,
						OrderOptionWashAndFold.class);
				startActivity(intent);
				finish();
				edit = sharedPref.edit();
				edit.putBoolean("householdDone", true);
				edit.commit();
				insertHouseholdData();
			
			}

		});

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(WashAndFoldListPage.this);
			pDialog.setMessage("Please Wait... Fetching List"); // typically

			// will define
			// such
			// strings in a remote file.
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// Locate the class table named "Country" in Parse.com

			ParseQuery<ParseObject> houseHoldItems = new ParseQuery<ParseObject>(
					"HouseHoldItems");
			try {

				houseHoldItemsObject = houseHoldItems.find();
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			washAndFoldItemList.clear();

			for (ParseObject houseHoldItems : houseHoldItemsObject) {
				// stateList.add((String) stateName.get("Name"));
				DC_InsertFields insert = new DC_InsertFields();
				HouseHoldItem houseHoldItem = new HouseHoldItem();
				houseHoldItem.setItemName(houseHoldItems.get("ItemName")
						.toString());
				houseHoldItem.setPrice(houseHoldItems.get("Price").toString());

				insert.setItemName(houseHoldItems.get("ItemName").toString());
				insert.setPrice(houseHoldItems.get("Price").toString());
				helper.InserHouseholdtValues(insert);
				washAndFoldItemList.add(houseHoldItem);

			}

			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			setWashAndFoldAdapter();

		}
	}

	public void setWashAndFoldAdapter() {

		dataAdapter = new AdapterWashAndFoldList(WashAndFoldListPage.this,WashAndFoldListPage.this,
				washAndFoldItemList);

		lvWashAndFold.setAdapter(dataAdapter);
		lvWashAndFold.setExpanded(true);

	}

	private void clearSharedPrefefrence() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		sharedPref.edit().remove("houseHoldCount").commit();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		// clearSharedPrefefrence();
		updateCount();
		super.onBackPressed();
	}

}
