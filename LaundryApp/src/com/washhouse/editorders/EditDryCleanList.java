package com.washhouse.editorders;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.washhous.database.SqlHelper;
import com.washhous.dataclasses.DryCleaningItems;
import com.washhous.laundryapp.MyApplication;
import com.washhous.laundryapp.OrderOptionDryCleaning;
import com.washhous.laundryapp.R;
import com.washhous.parse.AdapterDryCleaningList;
import com.washhous.parse.ExpandListView;

public class EditDryCleanList extends Activity {

	ProgressDialog pDialog = null;
	private ExpandListView lvDryCleaning;
	public List<ParseObject> dryCleaningObject;
	List<DryCleaningItems> dryCleaningItemsList = new ArrayList<DryCleaningItems>();
	private EditAdapterDryCleaningList dataAdapterDryClean;
	SqlHelper helper;
	private SharedPreferences sharedPref;
	private Button btnDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dry_clean_page_list);
		initControls();
		helper = new SqlHelper(getApplicationContext());
		sharedPref = getSharedPreferences("laundry", 0);
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

				Intent intent = new Intent(EditDryCleanList.this,
						OrderOptionDryCleaning.class);

				startActivity(intent);
				finish();
			}
		});

		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.title_text);
		mTitleTextView.setText("Select Clothes");
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

		dryCleaningItemsList = MyApplication.dryCleaningItemsList;
		if (dryCleaningItemsList.size() < 1) {
			if (CommanMethods.isConnected(getBaseContext())) {
				// helper.deleteDataFromAllTables();
				new RemoteDataTask().execute();
			}
		} else {
			setDryCleanAdapter();
		}
	}

	private void setDryCleanAdapter() {

		dataAdapterDryClean = new EditAdapterDryCleaningList(EditDryCleanList.this,
				dryCleaningItemsList);
		lvDryCleaning.setAdapter(dataAdapterDryClean);
		lvDryCleaning.setExpanded(true);

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(EditDryCleanList.this);
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

			ParseQuery<ParseObject> dryCleaning = new ParseQuery<ParseObject>(
					"DryCleaningItems");
			try {

				dryCleaningObject = dryCleaning.find();
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dryCleaningItemsList.clear();

			for (ParseObject dryCleaning : dryCleaningObject) {
				// stateList.add((String) stateName.get("Name"));
				DC_InsertFields insert = new DC_InsertFields();
				DryCleaningItems dryCleaningList = new DryCleaningItems();
				dryCleaningList.setItemName(dryCleaning.get("ItemName")
						.toString());
				dryCleaningList.setPrice(dryCleaning.get("Price").toString());

				insert.setItemName(dryCleaning.get("ItemName").toString());
				insert.setPrice(dryCleaning.get("Price").toString());

				helper.InserDryCleanValues(insert);
				dryCleaningItemsList.add(dryCleaningList);

			}

			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			setDryCleanAdapter();

		}
	}

	private void initControls() {
		lvDryCleaning = (ExpandListView) findViewById(R.id.lv_dry_clean_items);
		btnDone = (Button) findViewById(R.id.btn_dry_clean_done);
		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditDryCleanList.this,
						OrderOptionDryCleaning.class);
				startActivity(intent);

				finish();
				// Editor edit = sharedPref.edit();
				// edit.putBoolean("dryDone", true);
				// edit.commit();

			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
		// clearPreference();

	}

}
