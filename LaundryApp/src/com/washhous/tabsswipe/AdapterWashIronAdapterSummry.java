package com.washhous.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.washhous.comman.CalculationRate;
import com.washhous.comman.DC_InsertFields;
import com.washhous.comman.DC_Order_WashFold;
import com.washhous.database.SessionManager;
import com.washhous.laundryapp.R;
import com.washhous.parse.AdapterHouseHoldDialog;

public class AdapterWashIronAdapterSummry extends BaseAdapter {
	public List<DC_InsertFields> WashIronCountList = new ArrayList<DC_InsertFields>();
	private List<DC_Order_WashFold> ironCountList;
	holder mholder=new holder();
	Activity act;
	private LayoutInflater inflater;
	private SessionManager mSessionManager;
	AdapterWashIronAdapterSummry(Activity act,
			List<DC_Order_WashFold> ironCountList) {
		this.act = act;
		this.ironCountList = ironCountList;
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSessionManager=new SessionManager(act);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ironCountList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		String optionSelected1="",optionSelected2="",finaloptionSelected="";
		int var;
		final int pos=arg0;
		var=arg0+1;
		View vi = inflater.inflate(R.layout.child_wash_iron, null);
		mholder.txtOrderNum=(TextView)vi.findViewById(R.id.txtOrderNum);
		mholder.txtNoOfItems=(TextView)vi.findViewById(R.id.txt_inventory_item_count);
		mholder.selectedOption=(TextView)vi.findViewById(R.id.txtSelectedOptions);
		mholder.peekUpdate=(TextView)vi.findViewById(R.id.txtFromDate);
		mholder.deliveryDate=(TextView)vi.findViewById(R.id.txtTodate);
		mholder.totalAmount=(TextView)vi.findViewById(R.id.txttotalAmount);
		mholder.layout_inventory=(LinearLayout)vi.findViewById(R.id.layout_inventory);
		mholder.imageDelete=(ImageView)vi.findViewById(R.id.imageviewDelete);
		mholder.imageDelete.setTag(ironCountList.get(arg0).getOrderId().trim());
		
		mholder.txtOrderNum.setText("Order No."+var);
		mholder.txtNoOfItems.setText(": "+ironCountList.get(arg0).getItem_Count());
		//mholder.selectedOption.setText(": "+ironCountList.get(arg0).getEco_Detergent());
		mholder.peekUpdate.setText(": "+ironCountList.get(arg0).getPickup_Date());
		mholder.deliveryDate.setText(": "+ironCountList.get(arg0).getDelivery_Date());
		mholder.layout_inventory.setTag(ironCountList.get(arg0).getOrderId().trim());
		
		
		int NoOfItems=Integer.parseInt(ironCountList.get(arg0).getItem_Count().trim());
		double basCharge=0.0,EcoCharge=0.0,ScentCharge=0.0;
		basCharge=CalculationRate.washAndIron_basicIronRatePerItom*NoOfItems;
		Log.d("No of Items", "No of Items "+ironCountList.get(arg0).getItem_Count().trim());
		Log.d("basCharge", "basCharge "+basCharge);
		
		//if Eco Detergent
		if(ironCountList.get(arg0).getEco_Detergent().contains("true")){
			EcoCharge=NoOfItems*CalculationRate.washAndIron_EcoDetegentRatePerItem;
			optionSelected1="Eco Detergent";
		}
		
		//if Scent
		if(ironCountList.get(arg0).getScent_Free().contains("true")){
			ScentCharge=NoOfItems*CalculationRate.washAndIron_ScentFreeRatePerItem;
			optionSelected2="Scent free softner";
		}
		if(!optionSelected1.equals("")||!optionSelected2.equals("")){
			finaloptionSelected=optionSelected1+","+optionSelected2;
			finaloptionSelected=finaloptionSelected.replace(",", "\n").trim();
			
			mholder.selectedOption.setText(finaloptionSelected);
		}else{
			mholder.selectedOption.setText("No selection");
		}
		
		double totalCharges=basCharge+EcoCharge+ScentCharge;
		float sum=(float)totalCharges;
		mholder.totalAmount.setText("$ "+sum);
		
		//onlclick
		mholder.layout_inventory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String orderId1 = mholder.layout_inventory.toString();
				new RemoteDataTask(orderId1).execute();
				//Toast.makeText(act, orderId1, 300).show();
			}
		});
		mholder.imageDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String orderId2 =mholder.imageDelete.getTag().toString();
				new DeleteOrderTask(orderId2,pos).execute();
			}
		});
		return vi;
	}

	class holder{
		
		TextView txtOrderNum,txtNoOfItems,selectedOption,peekUpdate,deliveryDate,totalAmount;
		ImageView imageDelete,imageEdit;
		LinearLayout layout_inventory;
	}



	
	class RemoteDataTask extends AsyncTask<String, String, String>{
		public List<ParseObject> washAndIronCountObject;
		ProgressDialog pDialog;
		
		String OrderId;
		public RemoteDataTask(String OrderId){
			this.OrderId=OrderId;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			SetupDialog(washAndIronCountObject);
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


		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ParseQuery<ParseObject> dryCleanItems = new ParseQuery<ParseObject>("WashIron_Item");

			try {
				//dryCleanItems.whereEqualTo("user_id", mSessionManager.getemailid());
				String id=OrderId;
				dryCleanItems.whereEqualTo("OrderId", OrderId);
				washAndIronCountObject = dryCleanItems.find();
			} catch (com.parse.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	class DeleteOrderTask extends AsyncTask<String, String, String>{
		
		ProgressDialog pg;
		String orderId;
		int precount1=0,postcount1=0,precount2=0,postcount2=0;
		int pos;
		DeleteOrderTask(String orderId,int pos){
			this.orderId=orderId;
			this.pos=pos;
			 Log.d("Order Id", "Order Id"+orderId);
			 Log.d("pos Id", "pos Id"+pos);
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Order_WashIron");
			ParseQuery<ParseObject> query2 = ParseQuery.getQuery("WashIron_Item");
			
			query1.whereEqualTo("OrderId", orderId);
			query2.whereEqualTo("OrderId", orderId);
			query1.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> messages, ParseException e1) {
					// TODO Auto-generated method stub
					if(e1==null){
						for(ParseObject message : messages)
						{
						     message.deleteInBackground();
						    // ironCountList.remove(pos);
						    // notifyDataSetChanged();
						     	
						    
						}  
					}else{
						Log.d("Semothing went wrong. Show useful message based on ParseException data", e1.getMessage());
					}
				}
			});
			query2.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> messages, ParseException e2) {
					// TODO Auto-generated method stub
					if(e2==null){
						for(ParseObject message : messages)
						{
						     message.deleteInBackground();
						     notifyDataSetChanged();
						}  
					}else{
						Log.d("Semothing went wrong. Show useful message based on ParseException data", e2.getMessage());
					}
				}
			});
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pg.cancel();
			  ironCountList.remove(pos);
			  notifyDataSetChanged();
			  Intent intent = new Intent(act,
						OrderSummaryNew.class);
				act.startActivity(intent);
				act.finish();
			Log.d("Pre", "Pre1 "+precount1);
			Log.d("post", "post1 "+postcount1);
			Log.d("Pre", "Pre2 "+precount2);
			Log.d("Pre", "post2 "+postcount2);
			Toast.makeText(act, "Record deleted", 300).show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pg=new ProgressDialog(act);
			pg.setTitle("Please wait");
			pg.setMessage("Order deleting");
			pg.show();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			
		}
		
		
		
	}

	public void SetupDialog(List<ParseObject> list) {
		// TODO Auto-generated method stub
		WashIronCountList.clear();
		List<DC_InsertFields> filterList = new ArrayList<DC_InsertFields>();
		Log.d("List Size", "List Size "+list.size());
		for (ParseObject dryCleanItems : list) {
			// stateList.add((String) stateName.get("Name"));

			DC_InsertFields dryclean = new DC_InsertFields();

			dryclean.setItemName(dryCleanItems.get("Iron_Items").toString());
			dryclean.setPrice(dryCleanItems.get("Item_Price").toString());
			dryclean.setCount(dryCleanItems.get("Count").toString());
			dryclean.setOrderId(dryCleanItems.get("OrderId").toString());

			WashIronCountList.add(dryclean);

			filterList.add(dryclean);
		}

		setDialogList(filterList);
	}

	private void setDialogList(List<DC_InsertFields> filterList) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(act);
				// Include dialog.xml file

				float Total = 0.0f;
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

				txt_dialog_heading.setText("Iron Item");

				for (int i = 0; i < filterList.size(); i++) {
					int floatQty = 0;
					Float price = Float.parseFloat(filterList.get(i).getPrice());
					floatQty = Integer.parseInt(filterList.get(i).getCount());

					if (floatQty != 0) {

						float subTotal = price * floatQty;
						Total = Total + subTotal;
					}

				}

				total_household.setText(":$" + Total);

				AdapterHouseHoldDialog adptr = new AdapterHouseHoldDialog(act,
						filterList);
				lvHouseHold.setAdapter(adptr);
				dialog.show();
	}
}
