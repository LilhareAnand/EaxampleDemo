package com.washhous.laundryapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.washhous.comman.CommanMethods;
import com.washhous.comman.DC_InsertFields;
import com.washhous.comman.SharedDataPrefrence;
import com.washhous.database.SessionManager;
import com.washhous.database.SqlHelper;
import com.washhous.dataclasses.InventoryCheckList;
import com.washhous.parse.AdapterInventoryList;
import com.washhous.parse.ExpandListView;

public class InventoryListPage extends Activity {

	ProgressDialog pDialog = null;
	private AdapterInventoryList dataAdapter;
	public List<ParseObject> inventoryCheckListObject;
	private ExpandListView lvInventory;
	public List<InventoryCheckList> inventoryCheckLists = new ArrayList<InventoryCheckList>();
	SqlHelper helper;
	private SharedPreferences sharedPref;
	private Button btnDone;
    SessionManager msessManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory_list_page);

		initControls();
		msessManager=new SessionManager(InventoryListPage.this);
		helper = new SqlHelper(getApplicationContext());
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
		View mCustomView = mInflater.inflate(R.layout.actionbar_drawermenu,
				null);
		ImageButton imgBack = (ImageButton) mCustomView
				.findViewById(R.id.btn_img_cart1);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				confirmOrderDialog();
				

				// helper.updateInventoryCount();

			}
		});

		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.title_text);
		mTitleTextView.setText("Select Clothes");
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
		inventoryCheckLists = MyApplication.inventoryCheckLists;

		if (inventoryCheckLists.size() < 1) {
			if (CommanMethods.isConnected(getBaseContext())) {
				// helper.deleteDataFromAllTables();
				new RemoteDataTask().execute();
			}
		} else {
			setInventoryAdapter();
		}

	}

	private void initControls() {

		lvInventory = (ExpandListView) findViewById(R.id.lv_inventory_items);
		btnDone = (Button) findViewById(R.id.btn_inventory_done);
		btnDone.setOnClickListener(new OnClickListener() {

			private Editor edit;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InventoryListPage.this,
						OrderOptionWashAndFold.class);
				startActivity(intent);
				insertData();
				int inventorycount=helper.getInventoryCount();
				//msessManager.address("inventorycount", String.valueOf(inventorycount));
				finish();

			}
		});

	}

	private void insertData() {
		SharedDataPrefrence pref=new SharedDataPrefrence();
		ArrayList<String>Name,Size;
		Name=new ArrayList<String>();
		Size=new ArrayList<String>();
		Name.add("Shirt/T-Shirt/Blouse");
		Size.add(pref.getInvertoryShirt(InventoryListPage.this));
		Name.add("Skirt");
		Size.add(pref.getInvertorySkirt(InventoryListPage.this));
		Name.add("Sweater/Cardigan");
		Size.add(pref.getInvertorySweater(InventoryListPage.this));
		Name.add("Trousers/Pants/Jeans");
		Size.add(pref.getInvertoryTrousers(InventoryListPage.this));
		Name.add("Coat/Jumper");
		Size.add(pref.getInvertoryCoat(InventoryListPage.this));
		for(int i=0;i<Name.size();i++){
			ParseObject orderHead = new ParseObject("Inventory_Item");
			orderHead.put("OrderId", String.valueOf(MyApplication.orderId));
			orderHead.put("Inventory_Items", Name.get(i));
			orderHead.put("Count", Size.get(i));
			orderHead.put("user_id", msessManager.getemailid());
			orderHead.saveInBackground();
		}
		

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(InventoryListPage.this);
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

			ParseQuery<ParseObject> inventoryCheckList = new ParseQuery<ParseObject>(
					"InventoryChecklist");
			try {

				inventoryCheckListObject = inventoryCheckList.find();
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			inventoryCheckLists.clear();

			for (ParseObject inventoryCheckList : inventoryCheckListObject) {
				// stateList.add((String) stateName.get("Name"));
				DC_InsertFields insert = new DC_InsertFields();
				InventoryCheckList inventoryItem = new InventoryCheckList();

				inventoryItem.setItemName(inventoryCheckList.get("ItemName")
						.toString());
				inventoryItem.setPrice(inventoryCheckList.get("Price")
						.toString());

				insert.setItemName(inventoryCheckList.get("ItemName")
						.toString());
				insert.setPrice(inventoryCheckList.get("Price").toString());
				helper.InserInventoryValues(insert);

				inventoryCheckLists.add(inventoryItem);
			}

			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			setInventoryAdapter();

		}
	}

	public void setInventoryAdapter() {

		dataAdapter = new AdapterInventoryList(InventoryListPage.this,
				inventoryCheckLists);

		lvInventory.setAdapter(dataAdapter);
		lvInventory.setExpanded(true);

	}

	private void clearSharedPrefefrence() {
		// TODO Auto-generated method stub
		sharedPref = getSharedPreferences("laundry", 0);
		sharedPref.edit().remove("inventoryCount").commit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		// clearSharedPrefefrence();
		// helper.updateInventoryCount();

	}
	private void confirmOrderDialog() {

		final Dialog dialog = new Dialog(InventoryListPage.this);
		// Include dialog.xml file
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.conformation_dailog);
		// Set dialog title
		ImageView img_close_dialog = (ImageView) dialog
				.findViewById(R.id.img_close_dialog);
		img_close_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialog.dismiss();
			}
		});
		Button btnno = (Button) dialog.findViewById(R.id.btn_no);
		btnno.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				dialog.dismiss();
			}
		});
		Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InventoryListPage.this,
						OrderOptionWashAndFold.class);

				startActivity(intent);
				msessManager.address("inventorycount", "0");
				dialog.dismiss();
				finish();
				
			}
		});
		dialog.show();

	}
}
