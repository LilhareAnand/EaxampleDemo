package com.washhous.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.textservice.SpellCheckerService.Session;
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
import com.washhous.comman.DC_Order_WashFold;
import com.washhous.database.SessionManager;
import com.washhous.laundryapp.OrderSummery;
import com.washhous.laundryapp.R;

public class WashFoldFragment extends Fragment {

	private ProgressDialog pDialog;
	public List<ParseObject> houseHoldCountObject;
	public List<ParseObject> inventryCountObject;
	SessionManager mySession;
	private ListView lstwashFold;
	private AdapterWashFoldAdapterSummary dataAdapter;
	private ArrayList<List<ParseObject>>woshFoldItemList;
	private ArrayList<List<ParseObject>>InvertyItemList;
	public List<ParseObject> InventoryCountObject1;
	public List<ParseObject> woshfolditemtObject1;
	public static List<DC_Order_WashFold> washAndFoldCountList = new ArrayList<DC_Order_WashFold>();
    CalculationRate calculation;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_wash_fold,
				container, false);
		mySession=new SessionManager(getActivity());
		lstwashFold = (ListView) rootView.findViewById(R.id.lst_wash_and_fold);
		woshFoldItemList=new ArrayList<List<ParseObject>>();
		InvertyItemList=new ArrayList<List<ParseObject>>();
		if (CommanMethods.isConnected(getActivity())) {

			new RemoteDataTask().execute();
		}

		return rootView;
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			calculation=new CalculationRate();
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
			washAndFoldCountList.clear();
			ParseQuery<ParseObject> houseHoldItems = new ParseQuery<ParseObject>("Order_WashFold");
			
			try {
				houseHoldItems.whereEqualTo("user_id" , mySession.getemailid());
				
				houseHoldCountObject = houseHoldItems.find();
				for (ParseObject houseHoldItems1 : houseHoldCountObject) {
					// stateList.add((String) stateName.get("Name"));
					float totalBasic=0,totlaHouseHold=0,totaladditional=0;
					Log.d("Parser Object ","parser Object "+houseHoldItems1);
					DC_Order_WashFold washFold = new DC_Order_WashFold();

					washFold.setEco_Detergent(houseHoldItems1.get("Eco_Detergent").toString());
					String Iseco_dtergent=houseHoldItems1.get("Eco_Detergent").toString();
					washFold.setScent_Free(houseHoldItems1.get("Scent_Free").toString());
					String scent_free=houseHoldItems1.get("Scent_Free").toString();
					washFold.setWeight_Est_Count(houseHoldItems1.get("Weight_Est_Count").toString());
					String weight_estimate=houseHoldItems1.get("Weight_Est_Count").toString();
					int weightTotal=Integer.parseInt(houseHoldItems1.get("Weight_Est_Count").toString());
					String total_sum=houseHoldItems1.get("total_sum").toString();
					double total=0.2f;
					try {
						total=Double.parseDouble(total_sum);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					
					washFold.setTotalcalculation(total+"");
					/*if(weightTotal!=0)
					{
					totalBasic=calculation.getBasicvalue(weightTotal);
					totaladditional=calculation.getaddtionalCharger(Iseco_dtergent,scent_free,weightTotal);
					}*/
					
					washFold.setItem_Count(houseHoldItems1.get("Household_Count").toString());
					washFold.setInventory_Count(houseHoldItems1.get("Inventory_Count").toString());
					washFold.setPickup_Date(houseHoldItems1.get("Pickup_Date").toString());
					washFold.setDelivery_Date(houseHoldItems1.get("Delivery_Date").toString());
					washFold.setPaid(houseHoldItems1.get("Paid").toString());
					washFold.setOrderId(houseHoldItems1.get("OrderId").toString());
					String order_id=houseHoldItems1.get("OrderId").toString();
					washFold.setObjectId(houseHoldItems1.getObjectId());
				
					/*ParseQuery<ParseObject> WashFold_Item = new ParseQuery<ParseObject>("WashFold_Item");
					String total="";
					try {
						WashFold_Item.whereEqualTo("OrderId", order_id);
						 woshfolditemtObject1 =WashFold_Item.find();
						 woshFoldItemList.add(woshfolditemtObject1);
						for (ParseObject washandfold : woshfolditemtObject1) {
							int itemCount=0;
							double itemPrice=0,sum=0;
							itemPrice=Double.parseDouble(washandfold.get("Item_Price").toString());
							itemCount=Integer.parseInt(washandfold.get("Count").toString());
							sum=itemCount*itemPrice;
							totlaHouseHold=(float)sum+totlaHouseHold;
							
						}
						
						
					} catch (com.parse.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(Exception e)
					{
					Log.d("error",e.toString())	;
					}
					float totalPrices=totaladditional+totalBasic+totlaHouseHold;
					washFold.setTotalvalu(String.valueOf(totalPrices));*/
					washAndFoldCountList.add(washFold);

				}
				
			} catch (com.parse.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setWashAndFoldAdapter();
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			

		}
	}

	public void setWashAndFoldAdapter() {
		// TODO Auto-generated method stub
		dataAdapter = new AdapterWashFoldAdapterSummary(getActivity(),
				washAndFoldCountList,woshFoldItemList);

		lstwashFold.setAdapter(dataAdapter);
	}
}
