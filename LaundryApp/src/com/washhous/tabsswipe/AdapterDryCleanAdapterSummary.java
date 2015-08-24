package com.washhous.tabsswipe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.washhous.laundryapp.OrderSummery;
import com.washhous.laundryapp.R;
import com.washhous.parse.AdapterHouseHoldDialog;

public class AdapterDryCleanAdapterSummary extends BaseAdapter {

	private FragmentActivity act;
	private List<DC_Order_WashFold> data;
	private LayoutInflater inflater;
	private ViewHolder holder;
	
	
	public ProgressDialog pDialog;
	public List<ParseObject> dryCleanCountObject;
	private ArrayList<List<ParseObject>>DryList;
	public List<DC_InsertFields> dryCleanCountList = new ArrayList<DC_InsertFields>();

	public AdapterDryCleanAdapterSummary(FragmentActivity activity,
			List<DC_Order_WashFold> dryCleanCountList, ArrayList<List<ParseObject>> dryList2) {
		act = activity;
		data = dryCleanCountList;
		inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
		DryList=dryList2;
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
		TextView lblOrderNo;
		TextView lblOrderTotal;
		LinearLayout layout_dryClean;
		ImageView imageviewDelete;

	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		final int pos=position;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.child_dry_clean, null);
			holder = new ViewHolder();
			vi.setTag(holder);
			holder.lblCount = (TextView) vi
					.findViewById(R.id.txt_dry_clean_item_count);
			holder.lblPickDt = (TextView) vi
					.findViewById(R.id.txt_pickdt_dry_summary);
			holder.lblDeliverDt = (TextView) vi
					.findViewById(R.id.txt_deliverdt_dry_summary);
			holder.lblOrderNo = (TextView) vi
					.findViewById(R.id.txt_order_count);
			holder.lblOrderTotal = (TextView) vi
					.findViewById(R.id.txt_order_total_dry);
			holder.layout_dryClean = (LinearLayout) vi
					.findViewById(R.id.layout_dry_clean);
			holder.imageviewDelete=(ImageView)vi.findViewById(R.id.imageviewDelete);

		} else {
			holder = (ViewHolder) vi.getTag();
		}
		// new RemoteDataTask().execute();
		holder.lblOrderTotal.setTag(position);
		//new RemoteDataTask(data.get(position).getOrderId(),holder.lblOrderTotal).execute();
		holder.lblOrderNo.setText("Order No:" + (position + 1));
		holder.lblCount.setText(":" + data.get(position).getItem_Count());
		holder.lblPickDt.setText(":" + data.get(position).getPickup_Date());
		holder.lblDeliverDt.setText(":" + data.get(position).getDelivery_Date());
		holder.lblOrderTotal.setText("$ "+data.get(position).getTotalvalu());
		holder.layout_dryClean.setTag(position + "");
		holder.layout_dryClean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = Integer.parseInt((String) v.getTag());

				String orderId = data.get(position).getOrderId().toString().trim();
				//getList(orderId);
				SetupDialog(DryList.get(position));

			}
		});
		holder.imageviewDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String orderId = data.get(pos).getOrderId().toString().trim();
				new DeleteAsync(orderId,pos).execute();
			}
		});
		return vi;
	}

	protected void SetupDialog(List<ParseObject> list) {
		// TODO Auto-generated method stub
		try {
			dryCleanCountList.clear();
			List<DC_InsertFields> filterList = new ArrayList<DC_InsertFields>();
			Log.d("List Size", "List Size "+list.size());
			for (ParseObject dryCleanItems5 : list) {
				// stateList.add((String) stateName.get("Name"));

				DC_InsertFields dryclean = new DC_InsertFields();
                  String itme_name=dryCleanItems5.get("DryClean_Items").toString();
				dryclean.setItemName(dryCleanItems5.get("DryClean_Items").toString());
				dryclean.setPrice(dryCleanItems5.get("Price").toString());
				dryclean.setCount(dryCleanItems5.get("Count").toString());
				dryclean.setOrderId(dryCleanItems5.get("OrderId").toString());

				dryCleanCountList.add(dryclean);

				filterList.add(dryclean);
			}

			setDialogList(filterList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	protected void getList(String orderId) {

		if (CommanMethods.isConnected(act)) {

			
			// getDryCleanFilterCount();
		}

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, String, String> {
		String orderId;
		TextView textViewTotal;
		RemoteDataTask(String id,TextView textViewTotal) {
			orderId = id;
			this.textViewTotal=textViewTotal;
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
			//pDialog.show();

		}

		@Override
		protected String doInBackground(Void... params) {
			
			ParseQuery<ParseObject> dryCleanItems = new ParseQuery<ParseObject>("DryClean_Item");
			String total="";
			try {
				dryCleanItems.whereEqualTo("OrderId", orderId);
				dryCleanCountObject = dryCleanItems.find();
				DryList.add(dryCleanCountObject);
				total=calculateTotal(dryCleanCountObject);
			} catch (com.parse.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(Exception e)
			{
				
			}
			
			return total;
		}

		@Override
		protected void onPostExecute(String result) {

			
			
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			textViewTotal.setText("$ "+result);
			

		}
	}

	private void setDialogList(List<DC_InsertFields> filterList) {

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

		txt_dialog_heading.setText("Dry Cleaning Item");

		try {
			for (int i = 0; i < filterList.size(); i++) {
				int floatQty = 0;
				Float price = Float.parseFloat(filterList.get(i).getPrice());
				floatQty = Integer.parseInt(filterList.get(i).getCount());

				if (floatQty != 0) {

					float subTotal = price * floatQty;
					Total = Total + subTotal;
				}
				total_household.setText(":$" + Total);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		AdapterHouseHoldDialog adptr = new AdapterHouseHoldDialog(act,
				filterList);
		lvHouseHold.setAdapter(adptr);
		dialog.show();

	}

	public String calculateTotal(List<ParseObject> dryCleanCountObject2) {
		// TODO Auto-generated method stub
		float totalprice=0;
		for (ParseObject dryCleanItems : dryCleanCountObject2) {
			// stateList.add((String) stateName.get("Name"));

			int itemCount=0;
			double itemPrice=0,sum=0;
			itemPrice=Double.parseDouble(dryCleanItems.get("Price").toString());
			itemCount=Integer.parseInt(dryCleanItems.get("Count").toString());
			sum=itemCount*itemPrice;
			totalprice=(float)sum+totalprice;
	}
		return totalprice+"";
	}
	
	class DeleteAsync extends AsyncTask<String, String, String>{

		String OrderId;
		int pos;
		DeleteAsync(String OrderId,int pos){
			this.OrderId=OrderId;
			this.pos=pos;
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Order_DryClean");
			ParseQuery<ParseObject> query2 = ParseQuery.getQuery("DryClean_Item");
			query1.whereEqualTo("OrderId", OrderId);
			query2.whereEqualTo("groupname", OrderId);
			
			query1.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> messages, ParseException e1) {
					// TODO Auto-generated method stub
					if(e1==null){
						for(ParseObject message : messages)
						{
						     message.deleteInBackground();
						    // data.remove(pos);
						     //notifyDataSetChanged();
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
			pDialog.cancel();
			data.remove(pos);
		    notifyDataSetChanged();
		    
		    Intent intent = new Intent(act,
					OrderSummaryNew.class);
			act.startActivity(intent);
			act.finish();
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
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			
		}
		
		
	}
}
