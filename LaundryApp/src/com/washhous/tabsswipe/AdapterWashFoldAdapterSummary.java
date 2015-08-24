 package com.washhous.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.washhous.comman.CommanMethods;
import com.washhous.comman.DC_InsertFields;
import com.washhous.comman.DC_Order_WashFold;
import com.washhous.comman.InventoryModel;
import com.washhous.laundryapp.R;
import com.washhous.parse.AdapterHouseHoldDialog;
import com.washhous.tabsswipe.adapter.AdapterInvertoryDialogSummry;


public class AdapterWashFoldAdapterSummary extends BaseAdapter {

	private FragmentActivity act;
	private List<DC_Order_WashFold> data;
	private LayoutInflater inflater;
	private ViewHolder holder;
	private int count;
	private String orderId;
	public ProgressDialog pDialog;
	
	private String ecoDetergent;
	private String sentFree;
	private ArrayList<List<ParseObject>>householdList;
	public AdapterWashFoldAdapterSummary(FragmentActivity activity,
			List<DC_Order_WashFold> dryCleanCountList,  ArrayList<List<ParseObject>> woshFoldItemList) {
		act = activity;
		data = dryCleanCountList;
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		count = dryCleanCountList.size();
householdList=woshFoldItemList;


	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder {

		TextView lblCount;
		TextView lblPickDt;
		TextView lblDeliverDt;
		TextView lblweight;
		TextView lblOrderNo;
		TextView lblOrderTotal;
		TextView lblOptionSelected;
		LinearLayout layout_household, layout_inventory;
		ImageView imageviewEdit, imageviewDelete;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;

		if (convertView == null) {
			vi = inflater.inflate(R.layout.child_wash_fold, null);
			holder = new ViewHolder();
			vi.setTag(holder);
			holder.lblOptionSelected = (TextView) vi
					.findViewById(R.id.txt_option_selected);
			holder.lblweight = (TextView) vi.findViewById(R.id.txt_weight_est);
			holder.lblCount = (TextView) vi
					.findViewById(R.id.txt_household_item_count);
			holder.lblPickDt = (TextView) vi
					.findViewById(R.id.washfold_pickdt_dry_summary);
			holder.lblDeliverDt = (TextView) vi
					.findViewById(R.id.washfold_delivdate_dry_summary);
			holder.lblOrderNo = (TextView) vi
					.findViewById(R.id.txt_washfold_order_count);
			holder.lblOrderTotal = (TextView) vi.findViewById(R.id.txt_order_total_washfold);
			holder.layout_household = (LinearLayout) vi
					.findViewById(R.id.layout_household);
			holder.layout_inventory = (LinearLayout) vi
					.findViewById(R.id.layout_inventory);
			holder.imageviewEdit = (ImageView) vi
					.findViewById(R.id.imageviewEdit);
			holder.imageviewDelete = (ImageView) vi
					.findViewById(R.id.imageviewDelete);

		} else {
			holder = (ViewHolder) vi.getTag();
		}
		ecoDetergent = data.get(position).getEco_Detergent();
		sentFree = data.get(position).getScent_Free();
		holder.lblweight.setText(":" + data.get(position).getWeight_Est_Count());
		setWashFoldOption(ecoDetergent,sentFree,holder.lblOptionSelected);
		holder.lblOrderNo.setText("Order No:" + (position + 1));
		holder.lblCount.setText(":" + data.get(position).getItem_Count());
		holder.lblPickDt.setText(":" + data.get(position).getPickup_Date());
		holder.lblDeliverDt
				.setText(":" + data.get(position).getDelivery_Date());
		orderId = data.get(position).getOrderId().toString().trim();
		// by Love---
		holder.lblOrderTotal.setText("$ "+data.get(position).getTotalcalculation()); 
		holder.imageviewDelete.setTag(orderId);
		holder.imageviewDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(act, "Delete This", Toast.LENGTH_LONG).show();
			}
		});
		holder.imageviewDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String id = holder.imageviewDelete.getTag().toString();
				new Deletetask(id, position).execute();

			}
		});
		holder.layout_household.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getList(orderId);

			}
		});
		holder.layout_inventory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getInvetoryList(orderId);
			}
		});
		return vi;
	}

	protected void getInvetoryList(String orderId2) {
		// TODO Auto-generated method stub
		if (CommanMethods.isConnected(act)) {

			
			new GetInventoryAsyn(orderId).execute();

		}
	}

	@SuppressWarnings("unchecked")
	protected void getList(String orderId) {

		if (CommanMethods.isConnected(act)) {

			new RemoteDataTask(orderId).execute();
		}

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

		String mOrderId;
		public List<ParseObject> washFoldCountObject;
		public List<DC_InsertFields> washFoldCountList = new ArrayList<DC_InsertFields>();

		public RemoteDataTask(String OrderId) {
			// TODO Auto-generated constructor stub
			this.mOrderId = OrderId;
			
		}

		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(act);
			pDialog.setMessage("Please Wait..."); // typically

			// will define
			// such
			// strings in a remote file.
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			
			ParseQuery<ParseObject> washfoldItems = new ParseQuery<ParseObject>("WashFold_Item");
			washfoldItems.whereEqualTo("OrderId", mOrderId);
			try {
				// washfoldItems.whereEqualTo("", );
				washFoldCountObject = washfoldItems.find();
			} catch (com.parse.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			for (ParseObject washfoldItems : washFoldCountObject) {
				// stateList.add((String) stateName.get("Name"));

				DC_InsertFields washfold = new DC_InsertFields();

				washfold.setItemName(washfoldItems.get("Household_Name").toString());
				washfold.setPrice(washfoldItems.get("Item_Price").toString());
				washfold.setCount(washfoldItems.get("Count").toString());
				washfold.setOrderId(washfoldItems.get("OrderId").toString());

				washFoldCountList.add(washfold);

			}

			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			setDialogList(washFoldCountList);

		}
	}

	private void setDialogList(final List<DC_InsertFields> filterList) {

		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(act);
		// Include dialog.xml file
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.household_dialog);
		// Set dialog title
		final ListView lvHouseHold;
		lvHouseHold = (ListView) dialog.findViewById(R.id.lv_household);
		TextView txt_dialog_heading = (TextView) dialog.findViewById(R.id.txt_dialog_heading);
		ImageView img_dialog_close = (ImageView) dialog.findViewById(R.id.img_dialog_close);
		img_dialog_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		TextView total_household = (TextView) dialog.findViewById(R.id.total_household);
		txt_dialog_heading.setText("Household Items");

		AdapterHouseHoldDialog adptr = new AdapterHouseHoldDialog(act,filterList,total_household);
		lvHouseHold.setAdapter(adptr);
		adptr.notifyDataSetChanged();
		dialog.show();

	}

	private void setWashFoldOption(String ecoDetergent2, String sentFree2, TextView lblOptionSelected) {

		

		if (ecoDetergent2.equalsIgnoreCase("True") && sentFree2.equalsIgnoreCase("True")) {
			
			lblOptionSelected.setText("Eco Detergent\nScent free softner");

		} else {
			if (ecoDetergent2.equals("True")) {

				lblOptionSelected.setText("Eco Detergent");
				

			}
			if (sentFree.equals("True")) {
				
				lblOptionSelected.setText("Scent free softner");

			}
		}

		

	}

	class Deletetask extends AsyncTask<String, String, String> {

		String orderId;
		int pos;

		Deletetask(String id, int pos) {
			this.pos = pos;
			this.orderId = id;
			Log.d("OrderId", "pos " + pos);
			Log.d("OrderId", "OrderId " + orderId);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			ParseQuery<ParseObject> query1 = ParseQuery
					.getQuery("Order_WashFold");
			ParseQuery<ParseObject> query2 = ParseQuery
					.getQuery("WashFold_Item");
			ParseQuery<ParseObject> query3 = ParseQuery
					.getQuery("Inventory_Item");
			query1.whereEqualTo("OrderId", orderId);
			query2.whereEqualTo("OrderId", orderId);
			query2.whereEqualTo("OrderId", orderId);
			query1.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> messages, ParseException e1) {
					// TODO Auto-generated method stub
					if (e1 == null) {
						for (ParseObject message : messages) {
							try {
								message.deleteInBackground();
								
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						}
					} else {
						Log.d("Semothing went wrong. Show useful message based on ParseException data",
								e1.getMessage());
					}
				}
			});
			query2.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> messages, ParseException e2) {
					// TODO Auto-generated method stub
					if (e2 == null) {
						for (ParseObject message : messages) {
							message.deleteInBackground();
						}
					} else {
						Log.d("Semothing went wrong. Show useful message based on ParseException data",
								e2.getMessage());
					}
				}
			});
			query3.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> messages, ParseException e2) {
					// TODO Auto-generated method stub
					if (e2 == null) {
						for (ParseObject message : messages) {
							message.deleteInBackground();
						}
					} else {
						Log.d("Semothing went wrong. Show useful message based on ParseException data",
								e2.getMessage());
					}
				}
			});
			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			data.remove(pos);
			notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(act);
			pDialog.setMessage("Please Wait..."); // typically

			// will define
			// such
			// strings in a remote file.
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(false);
			pDialog.show();

		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}

	class GetInventoryAsyn extends AsyncTask<String, String, String> {
		public List<ParseObject> washFoldCountObject;
		
		
		ArrayList<InventoryModel> InventoryList = new ArrayList<InventoryModel>();
		String orderId;

		ProgressDialog pg;
		GetInventoryAsyn(String orderId) {
			this.orderId = orderId;
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			ParseQuery<ParseObject> washfoldItems = new ParseQuery<ParseObject>("Inventory_Item");
			washfoldItems.whereEqualTo("OrderId", orderId);
			try {
				// washfoldItems.whereEqualTo("", );

				washFoldCountObject = washfoldItems.find();
				for (int i = 0; i < washFoldCountObject.size(); i++) {
					int count = Integer.parseInt(washFoldCountObject.get(i).get("Count").toString());
					String name = washFoldCountObject.get(i).get("Inventory_Items").toString();
					InventoryList.add(new InventoryModel(name, count));
					Log.d("nAME ", "nAME "+name);
					Log.d("count ", "count "+count);
				}
			} catch (com.parse.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("count ", "InventoryList Size "+InventoryList.size());
			setDialg(InventoryList);
			pg.dismiss();

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pg=new ProgressDialog(act);
			pg.setMessage("Please wait...");
			pg.show();
		}

	}

	public void setDialg(ArrayList<InventoryModel> inventoryList) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(act);
		// Include dialog.xml file

		int Total = 0;
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.inventory_dialog);
		// Set dialog title
		final ListView lvHouseHold;
		lvHouseHold = (ListView) dialog.findViewById(R.id.lv_household);

		ImageView img_dialog_close = (ImageView) dialog
				.findViewById(R.id.img_inventory_dialog_close);
		img_dialog_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		TextView total_household = (TextView) dialog
				.findViewById(R.id.total_inventory_count);

		for (int i = 0; i < inventoryList.size(); i++) {
			
			int count = inventoryList.get(i).getCount();

			Total=Total+count;

		}

		total_household.setText(""+Total);

		AdapterInvertoryDialogSummry adptr = new AdapterInvertoryDialogSummry(act,inventoryList);
		lvHouseHold.setAdapter(adptr);
		dialog.show();
	}
	
}
