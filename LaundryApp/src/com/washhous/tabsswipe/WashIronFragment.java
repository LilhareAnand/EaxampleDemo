package com.washhous.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.washhous.comman.CommanMethods;
import com.washhous.comman.DC_Order_WashFold;
import com.washhous.database.SessionManager;
import com.washhous.laundryapp.OrderSummery;
import com.washhous.laundryapp.R;

public class WashIronFragment extends Fragment {

	public ProgressDialog pDialog;
	public List<ParseObject> ironCountObject;
	ListView lst_wash_and_iron;
	List<DC_Order_WashFold> ironCountList = new ArrayList<DC_Order_WashFold>();
	SessionManager mSessionManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_wash_iron,
				container, false);
		mSessionManager=new SessionManager(getActivity());
		lst_wash_and_iron=(ListView)rootView.findViewById(R.id.lst_wash_and_iron);
		if (CommanMethods.isConnected(getActivity())) {

			new RemoteDataTask().execute();
		}
		return rootView;
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(getActivity());
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
			// Locate the class table named "Country" in Parse.com

			
			ParseQuery<ParseObject> ironItems1 = new ParseQuery<ParseObject>("Order_WashIron");

			try {
				ironItems1.whereEqualTo("user_id", mSessionManager.getemailid());
				ironCountObject = ironItems1.find();
				for (ParseObject ironItems : ironCountObject) {
					// stateList.add((String) stateName.get("Name"));

					DC_Order_WashFold washIron = new DC_Order_WashFold();

					washIron.setEco_Detergent(ironItems.get("Eco_Detergent")
							.toString());
					washIron.setScent_Free(ironItems.get("Scent_Free").toString());

					washIron.setItem_Count(ironItems.get("Iron_Count").toString());

					washIron.setPickup_Date(ironItems.get("Pickup_Date").toString());
					washIron.setDelivery_Date(ironItems.get("Delivery_Date")
							.toString());
					washIron.setPaid(ironItems.get("Paid").toString());
					washIron.setOrderId(ironItems.get("OrderId").toString());
					washIron.setObjectId(ironItems.getObjectId());

					ironCountList.add(washIron);

				}
			} catch (com.parse.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			
			AdapterWashIronAdapterSummry mAdapterWashIronAdapterSummry=new AdapterWashIronAdapterSummry(getActivity(),ironCountList);
			lst_wash_and_iron.setAdapter(mAdapterWashIronAdapterSummry);
			mAdapterWashIronAdapterSummry.notifyDataSetChanged();
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}

		}
	}
}
