package com.washhous.laundryapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.washhous.comman.CommanMethods;
import com.washhous.comman.DC_InsertFields;
import com.washhous.database.SqlHelper;
import com.washhous.dataclasses.DryCleaningItems;
import com.washhous.dataclasses.HouseHoldItem;
import com.washhous.dataclasses.InventoryCheckList;
import com.washhous.dataclasses.IornItems;
import com.washhous.dataclasses.PriceListParse;
import com.washhous.dataclasses.StateList;

public class MyApplication extends Application {

	private Stack<Fragment> fragmentStack;
	private FragmentManager manager = null;
	private ArrayList<Group> group_list;
	private ArrayList<Child> ch_list;
	private Group grpName;
	private Child child;
	SqlHelper helper;

	public static List<DryCleaningItems> dryCleaningItemsCount = new ArrayList<DryCleaningItems>();

	// String Array List
	public static List<String> countryList = new ArrayList<String>();
	public static List<String> usaStateList = new ArrayList<String>();
	public static List<String> canadaStateList = new ArrayList<String>();

	// getset Class Array List
	public static List<StateList> dataStateList = new ArrayList<StateList>();
	public static List<DryCleaningItems> dryCleaningItemsList = new ArrayList<DryCleaningItems>();
	public static List<HouseHoldItem> washAndFoldItemList = new ArrayList<HouseHoldItem>();
	public static List<InventoryCheckList> inventoryCheckLists = new ArrayList<InventoryCheckList>();
	public static List<IornItems> iornItemsList = new ArrayList<IornItems>();
	public static List<PriceListParse> PriceList = new ArrayList<PriceListParse>();

	public static int orderId;

	public List<ParseObject> countryObject;
	public List<ParseObject> stateObject;
	public List<ParseObject> dryCleaningObject;
	public List<ParseObject> houseHoldItemsObject;
	public List<ParseObject> inventoryCheckListObject;
	public List<ParseObject> ironItemsObject;
	public List<ParseObject> priceListObject;
	private List<DC_InsertFields> householdDbList;
	private List<DC_InsertFields> ironDbList;
	private List<DC_InsertFields> drycleanDbList;
	public List<ParseObject> dryCleanCountObject;
	public static List<DC_InsertFields> dryCleanCountList = new ArrayList<DC_InsertFields>();

	public FragmentManager getManager() {
		return manager;
	}

	public void setManager(FragmentManager manager) {
		this.manager = manager;
	}

	public void insertFragment(Fragment fr) {
		fragmentStack.push(fr);
	}

	public Stack<Fragment> getFragmentStack() {
		return fragmentStack;
	}

	public void setFragmentStack(Stack<Fragment> fragmentStack) {
		this.fragmentStack = fragmentStack;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		fragmentStack = new Stack<Fragment>();
		helper = new SqlHelper(getApplicationContext());

		generatedOrderId();
		callMethodToGetData();
		// Add your initialization code here
		// Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

		// Parse.initialize(this, "pRcn2XXK68uW8jDqyjJUVxtwnX5Vq9eD6hyt18qE",
		// "TtC24qttElD3LMxrz1BeUfcdcK0bv8PmaeX0xov3");
		Parse.initialize(this, "zqCI5u8m8jW33kuKlb1zO9sHnkdLtUPq0e5fs0YG",
				"pRNDI1nYExOfp4mfiCEJ0lSVonvxBTXcdXp7dc6c");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
	}

	public void callMethodToGetData() {
		helper.deleteDataFromAllTables();
		if (CommanMethods.isConnected(getBaseContext())) {

			// helper.deleteDataFromAllTables();
			// new RemoteDataTask().execute();
		}

	}

	public static void generatedOrderId() {
		Random r = new Random(System.currentTimeMillis());
		orderId = 10000 + r.nextInt(20000);
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// Locate the class table named "Country" in Parse.com
			ParseQuery<ParseObject> country = new ParseQuery<ParseObject>(
					"Countries");
			ParseQuery<ParseObject> states = new ParseQuery<ParseObject>(
					"States");
			ParseQuery<ParseObject> dryCleaning = new ParseQuery<ParseObject>(
					"DryCleaningItems");
			ParseQuery<ParseObject> houseHoldItems = new ParseQuery<ParseObject>(
					"HouseHoldItems");
			ParseQuery<ParseObject> inventoryCheckList = new ParseQuery<ParseObject>(
					"InventoryChecklist");
			ParseQuery<ParseObject> ironItems = new ParseQuery<ParseObject>(
					"IronItems");
			ParseQuery<ParseObject> priceList = new ParseQuery<ParseObject>(
					"PriceList");

			country.orderByDescending("_created_at");
			states.orderByAscending("Name");
			try {
				countryObject = country.find();
				stateObject = states.find();
				dryCleaningObject = dryCleaning.find();
				houseHoldItemsObject = houseHoldItems.find();
				inventoryCheckListObject = inventoryCheckList.find();
				ironItemsObject = ironItems.find();
				priceListObject = priceList.find();

			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// Retrieve object "name" from Parse.com database
			for (ParseObject country : countryObject) {
				countryList.add((String) country.get("Name"));
			}

			for (ParseObject stateName : stateObject) {
				// stateList.add((String) stateName.get("Name"));

				StateList sList = new StateList();
				sList.setStateName(stateName.get("Name").toString());
				sList.setCountryId(stateName.get("countryId").toString());
				dataStateList.add(sList);

			}

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

			for (ParseObject priceList : priceListObject) {
				// stateList.add((String) stateName.get("Name"));

				PriceListParse priceListItem = new PriceListParse();
				priceListItem.setClothItem(priceList.get("ClothItems")
						.toString());
				priceListItem.setLaundrypriceRate(priceList.get(
						"LaundryPriceRate").toString());
				priceListItem.setDryCleaningpriceRate(priceList.get(
						"DryCleaningPriceRate").toString());
				PriceList.add(priceListItem);

			}

			addStateForCountryIdOne();
			addStateForCountryIdTwo();
		}
	}

	public ArrayList<Group> addOrderSummery(String serviceName,
			String serviceType, String totalQuantity, String txtClothweight,
			String txtPickUpdt, String txtPickUptm, String txtDelivery) {

		group_list = new ArrayList<Group>();

		ch_list = new ArrayList<Child>();

		grpName = new Group();
		child = new Child();

		grpName.setName(serviceName);
		child.setType(serviceType);
		child.setTotalCloth(totalQuantity);
		child.setWeight(txtClothweight);
		child.setPickupDate(txtPickUpdt);
		child.setPickupTime(txtPickUptm);
		child.setDeliveryDate(txtDelivery);

		ch_list.add(child);
		grpName.setItems(ch_list);

		group_list.add(grpName);

		return group_list;

	}

	public List<String> addStateForCountryIdTwo() {

		for (int i = 0; i < dataStateList.size(); i++) {

			if (dataStateList.get(i).getCountryId().equals("2")) {

				usaStateList
						.add(dataStateList.get(i).getStateName().toString());

			}
		}
		return usaStateList;

	}

	public List<String> addStateForCountryIdOne() {

		for (int i = 0; i < dataStateList.size(); i++) {

			if (dataStateList.get(i).getCountryId().equals("1")) {

				canadaStateList.add(dataStateList.get(i).getStateName()
						.toString());

			}
		}
		return canadaStateList;

	}

	public void saveLoginStatus(boolean status) {

		SharedPreferences sharedpref = getSharedPreferences("laundry", 0);
		Editor editor;

		editor = sharedpref.edit();
		editor.putBoolean("loginStatus", status);
		editor.commit();
	}

	public boolean checkLoginStatus() {
		SharedPreferences sharedpref = getSharedPreferences("laundry", 0);
		return sharedpref.getBoolean("loginStatus", false);
	}

	public void getList() {

		if (CommanMethods.isConnected(getBaseContext())) {

			new GetDialogList().execute();
		}

	}

	// RemoteDataTask AsyncTask
	private class GetDialogList extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// Locate the class table named "Country" in Parse.com

			// ParseQuery<ParseObject> houseHoldItems = new
			// ParseQuery<ParseObject>(
			// "DryClean_Item");
			// ParseQuery<ParseObject> ironItems = new ParseQuery<ParseObject>(
			// "Order_WashIron");
			ParseQuery<ParseObject> dryCleanItems = new ParseQuery<ParseObject>(
					"DryClean_Item");

			try {
				// houseHoldCountObject = houseHoldItems.find();
				// ironCountObject = ironItems.find();
				dryCleanCountObject = dryCleanItems.find();
			} catch (com.parse.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			dryCleanCountList.clear();

			for (ParseObject dryCleanItems : dryCleanCountObject) {
				// stateList.add((String) stateName.get("Name"));

				DC_InsertFields dryclean = new DC_InsertFields();

				dryclean.setItemName(dryCleanItems.get("DryClean_Items")
						.toString());
				dryclean.setPrice(dryCleanItems.get("Price").toString());
				dryclean.setCount(dryCleanItems.get("Count").toString());
				dryclean.setOrderId(dryCleanItems.get("OrderId").toString());

				dryCleanCountList.add(dryclean);

			}

		}
	}
}
