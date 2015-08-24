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
import com.washhous.dataclasses.IornItems;
import com.washhous.parse.AdapterWashAndIronList;
import com.washhous.parse.ExpandListView;

public class WashAndIronListPage extends Activity {

	ProgressDialog pDialog = null;
	private AdapterWashAndIronList dataAdapterIron;
	public List<ParseObject> ironItemsObject;
	private ExpandListView lvWashAndIron;
	List<IornItems> iornItemsList = new ArrayList<IornItems>();
	SqlHelper helper;
	private SharedPreferences sharedPref;
	private Button btnDone;
	SharedDataPrefrence mSharedDataPrefrence;
	SessionManager msSessionManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wash_and_iron_page_list);
		mSharedDataPrefrence=new SharedDataPrefrence();
		msSessionManager=new SessionManager(WashAndIronListPage.this);
		initControls();
		helper = new SqlHelper(getApplicationContext());
		sharedPref = getSharedPreferences("laundry", 0);
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
		View mCustomView = mInflater.inflate(R.layout.actionbar_drawermenu,null);
		ImageButton imgBack = (ImageButton) mCustomView.findViewById(R.id.btn_img_cart1);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
              confirmOrderDialog();
				

				// helper.updateIronCount();
				// clearPreference();

			}
		});

		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
		mTitleTextView.setText("Select Clothes");
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
		iornItemsList = MyApplication.iornItemsList;

		if (iornItemsList.size() < 1) {
			if (CommanMethods.isConnected(getBaseContext())) {
				// helper.deleteDataFromAllTables();
				new RemoteDataTask().execute();
			}
		} else {
			setWashAndFoldAdapter();
		}

	}



	private void initControls() {

		lvWashAndIron = (ExpandListView) findViewById(R.id.lv_wash_and_iron_items);

		btnDone = (Button) findViewById(R.id.btn_iron_done);
		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				for(int i=0;i<iornItemsList.size();i++){
					int count=mSharedDataPrefrence.getWashIronOrderItemCount(WashAndIronListPage.this,i);
					String name=mSharedDataPrefrence.getWashIronOrderItemName(WashAndIronListPage.this,i);
					String price=mSharedDataPrefrence.getWashIronOrderItemPrice(WashAndIronListPage.this,i);
					Log.d("Item name", "Item Name" +name);
					Log.d("Item name", "Item count" +count);
					Log.d("Item price", "Item price" +count);
					if(count!=-1&&!price.equals("")){
						ParseObject orderHead = new ParseObject("WashIron_Item");
						orderHead.put("user_id", msSessionManager.getemailid());
						orderHead.put("OrderId", String.valueOf(MyApplication.orderId));
						orderHead.put("Iron_Items", name);
						orderHead.put("Count", count+"");
						orderHead.put("Item_Price", price);
						orderHead.saveInBackground();
					}
					
				}
				
				 int ironCount = helper.getIronCount();
				// msSessionManager.address("ironItemCount",String.valueOf(ironCount));
				Intent intent = new Intent(WashAndIronListPage.this,OrderOptionWashAndIron.class);
				startActivity(intent);
				finish();
				Editor edit = sharedPref.edit();
				edit.putBoolean("householdDone", true);
				edit.commit();
				
			}
		});
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(WashAndIronListPage.this);
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

			ParseQuery<ParseObject> ironItems = new ParseQuery<ParseObject>("IronItems");
			try {
				ironItemsObject = ironItems.find();
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			iornItemsList.clear();

			for (ParseObject ironItems : ironItemsObject) {
				// stateList.add((String) stateName.get("Name"));
				DC_InsertFields insert = new DC_InsertFields();
				IornItems iornItem = new IornItems();

				iornItem.setItemName(ironItems.get("ItemName").toString());
				iornItem.setPrice(ironItems.get("Price").toString());

				insert.setItemName(ironItems.get("ItemName").toString());
				insert.setPrice(ironItems.get("Price").toString());
				helper.InserIronValues(insert);

				iornItemsList.add(iornItem);
			}

			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			setWashAndFoldAdapter();

		}
	}

	public void setWashAndFoldAdapter() {

		dataAdapterIron = new AdapterWashAndIronList(WashAndIronListPage.this,iornItemsList);
		lvWashAndIron.setAdapter(dataAdapterIron);
		lvWashAndIron.setExpanded(true);

	}

	protected void clearPreference() {
		sharedPref.edit().remove("ironItemCount").commit();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// helper.updateIronCount();
		super.onBackPressed();
		// clearPreference();

	}
	private void confirmOrderDialog() {

		final Dialog dialog = new Dialog(WashAndIronListPage.this);
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
				Intent intent = new Intent(WashAndIronListPage.this,
						OrderOptionWashAndIron.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent);
				msSessionManager.address("ironItemCount", "0");
				dialog.dismiss();
				finish();
				
			}
		});
		dialog.show();

	}
}
