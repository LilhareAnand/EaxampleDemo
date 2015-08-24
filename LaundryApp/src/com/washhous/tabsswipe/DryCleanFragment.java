package com.washhous.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.washhous.comman.CalculationRate;
import com.washhous.comman.CommanMethods;
import com.washhous.comman.DC_InsertFields;
import com.washhous.comman.DC_Order_WashFold;
import com.washhous.database.SessionManager;
import com.washhous.laundryapp.MyApplication;
import com.washhous.laundryapp.R;

public class DryCleanFragment extends Fragment {

	SessionManager mSessionManager;
	public ProgressDialog pDialog;
	public List<ParseObject> dryCleanCountObject;
	public List<ParseObject> dryCleanCountObject1;
	private View rootView;
	private ListView lstDryClean;
	private AdapterDryCleanAdapterSummary dataAdapter;
	public  List<DC_Order_WashFold> dryCleanCountList = new ArrayList<DC_Order_WashFold>();
    CalculationRate calulation;
    private ArrayList<List<ParseObject>>DryList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_dry_cleaning, container,
				false);
		mSessionManager=new SessionManager(getActivity());
		lstDryClean = (ListView) rootView.findViewById(R.id.lst_dry_clean);
		DryList=new ArrayList<List<ParseObject>>();
		if (CommanMethods.isConnected(getActivity())) {

			new RemoteDataTask().execute();
		}
		return rootView;
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
            calulation=new CalculationRate();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please Wait..."); // typically

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
			dryCleanCountList.clear();
			DryList.clear();
			ParseQuery<ParseObject> dryCleanItems1 = new ParseQuery<ParseObject>("Order_DryClean");

			try {
				dryCleanItems1.whereEqualTo("user_id", mSessionManager.getemailid());
				dryCleanCountObject = dryCleanItems1.find();
				for (ParseObject dryCleanItems : dryCleanCountObject) {
					// stateList.add((String) stateName.get("Name"));
					float totalprice=0;
					DC_Order_WashFold dryClean = new DC_Order_WashFold();

					dryClean.setItem_Count(dryCleanItems.get("DryClean_Count")
							.toString());
					dryClean.setPickup_Date(dryCleanItems.get("Pickup_Date")
							.toString());
					dryClean.setDelivery_Date(dryCleanItems.get("Delivery_Date")
							.toString());
					dryClean.setPaid(dryCleanItems.get("Paid").toString());
					String order_id=dryCleanItems.get("OrderId").toString();
					dryClean.setOrderId(dryCleanItems.get("OrderId").toString());
					dryClean.setObjectId(dryCleanItems.getObjectId());
                    
					ParseQuery<ParseObject> drycleaningitem = new ParseQuery<ParseObject>("DryClean_Item");
					String total="";
					try {
						drycleaningitem.whereEqualTo("OrderId", order_id);
						dryCleanCountObject1 = drycleaningitem.find();
						DryList.add(dryCleanCountObject1);
						for (ParseObject dryCleanItems2 : dryCleanCountObject1) {
							int itemCount=0;
							double itemPrice=0,sum=0;
							itemPrice=Double.parseDouble(dryCleanItems2.get("Price").toString());
							itemCount=Integer.parseInt(dryCleanItems2.get("Count").toString());
							sum=itemCount*itemPrice;
							totalprice=(float)sum+totalprice;
							
						}
						total=String.valueOf(totalprice);
						
					} catch (com.parse.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(Exception e)
					{
					Log.d("error",e.toString())	;
					}
					dryClean.setTotalvalu(total);
					dryCleanCountList.add(dryClean);

				}
			} catch (com.parse.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			

			setDryCleanAdapter();

			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}

		}
	}

	public void setDryCleanAdapter() {

		dataAdapter = new AdapterDryCleanAdapterSummary(getActivity(),
				dryCleanCountList,DryList);

		lstDryClean.setAdapter(dataAdapter);

	}

}
