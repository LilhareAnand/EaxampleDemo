package com.washhous.laundryapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.washhous.database.SessionManager;
import com.washhous.database.SqlHelper;
import com.washhous.menudrawer.MainActivity;
import com.washhous.parse.AdapterHouseHoldDialog;
import com.washhous.parse.AdapterHouseholdDialogInventory;
import com.washhous.tabsswipe.OrderSummaryNew;

public class OrderSummery extends FragmentActivity {

	private SharedPreferences sharedPref;
	Editor editor;
	private String serviceName = "";
	private String totalQuantity = "";
	private String serviceType = "";
	private String clothWeight = "";
	private String pickupDate = "";
	private String pickupTime = "";
	private String deliveryDate = "";
	private Button btnOrderList;
	private Button btnViewSummery;
	private ImageView dryCleanShowHide;
	private ImageView WashFoldShowHide;
	private ImageView WashIronShowHide;
	private LinearLayout dry_cleanning_sub_layout;
	private LinearLayout wash_and_fold_sub_layout;
	private LinearLayout wash_and_iron_sub_layout;
	private ImageView img_cart;
	private LinearLayout layout_household;
	private LinearLayout layout_inventory;
	private LinearLayout layout_washAndIron;
	private LinearLayout layout_dryClean;
	SqlHelper helper;
	private int householdCount;
	private int ironCount;
	private int dryCleanCount;
	private ImageView img_iron_edit;
	private ImageView img_fold_edit;
	private ImageView img_dry_edit;

	private LinearLayout wash_and_fold_main_layout;
	private LinearLayout wash_and_iron_main_layout;
	private LinearLayout dry_cleaning_main_layout;
	private int inventoryCount;
	private TextView txt_dry_clean_item_count;
	private TextView txt_household_item_count;
	private TextView txt_inventory_item_count;
	private TextView txt_iron_item_count;
	private boolean washFoldEcoPressed;
	private boolean washFoldscent;
	private boolean washIronEcoPressed;
	private boolean washIronscent;
	private String weightEstimate;
	private float addWashFoldSubTotal;
	private float addWashIronSubTotal;
	private float dryCleanSubTotal;
	private TextView txt_option_selected;
	private TextView txt_iron_action;

	private TextView txt_wash_fold_tot_count;
	private float addWeight = 0.0f;
	private TextView txt_weight_est;
	private float addEco = 0.0f;
	private float addScent = 0.0f;
	private float houseGrandTot = 0.0f;
	private TextView txt_dry_clean_tot_count;
	private float addIronEco;
	private float addIronEcoScent;
	private TextView txt_wash_iron_tot_count;
	private float ironGrandTot;
	private float grandTotal;
	private TextView txt_grand_total;
	private String foldtxtPickDate;
	private String foldtxtDeliveryDate;
	private String irontxtPickDate;
	private String irontxtDeliveryDate;
	private String drytxtPickDate;
	private String drytxtDeliveryDate;
	public ProgressDialog pDialog;
	public List<ParseObject> houseHoldCountObject;
	public List<ParseObject> ironCountObject;
	public List<ParseObject> dryCleanCountObject;
	private Button btn_View_summary;

	public static List<DC_Order_WashFold> washAndFoldCountList = new ArrayList<DC_Order_WashFold>();
	public static List<DC_Order_WashFold> ironCountList = new ArrayList<DC_Order_WashFold>();
	public static List<DC_InsertFields> dryCleanCountList = new ArrayList<DC_InsertFields>();
	SessionManager sessionmanger;
	EditText pickaddressEdite, deliveryaddress;
	TextView enterpickaddress, enterdeliveryaddress;
	String email_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_summery);
		initControls();
		sharedPref = getSharedPreferences("laundry", 0);
		editor = sharedPref.edit();
		sharedPref.edit().remove("dryItemCount");

		helper = new SqlHelper(getApplicationContext());
		getWashFoldSubTotal();
		getWashIronSubTotal();
		getdryCleanSubTotal();
		getSharedPreference();
		// if (CommanMethods.isConnected(getBaseContext())) {
		//
		// new RemoteDataTask().execute();
		// }
		sessionmanger = new SessionManager(OrderSummery.this);
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar_order_summary,
				null);
		pickaddressEdite = (EditText) findViewById(R.id.pick_address);
		deliveryaddress = (EditText) findViewById(R.id.deliveraddrew_edite);
		enterpickaddress = (TextView) findViewById(R.id.enter_pickupdaddres);

		String pickaddress = sessionmanger.getaddress("pickaddress");
		String deliveryaddres = sessionmanger.getaddress("delivery_address");
		enterdeliveryaddress = (TextView) findViewById(R.id.enter_deliveraddress);
		if (pickaddress.equals("")) {
			enterpickaddress.setText("Enter Address");
		} else {
			pickaddressEdite.setText(pickaddress);
			enterpickaddress.setText("Change Address");
		}
		if (deliveryaddres.equals("")) {
			enterdeliveryaddress.setText("Enter Address");
		} else {
			deliveryaddress.setText(deliveryaddres);
			enterdeliveryaddress.setText("Change Address");
		}
		enterpickaddress.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String valueString = enterpickaddress.getText().toString();
				if (valueString.equals("Enter Address")) {
					new EnterAddress().execute("Enter pickaddress");
				} else {
					new EditeAddress().execute("Edite pickaddress");
				}

			}
		});
		enterdeliveryaddress.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String valueString = enterdeliveryaddress.getText().toString();
				if (valueString.equals("Enter Address")) {
					new EnterAddress().execute("Enter deliveryaddress");
				} else {
					new EditeAddress().execute("Edite deliveryaddress");
				}
			}
		});

		ImageButton btnBackOrderSummery = (ImageButton) mCustomView
				.findViewById(R.id.btn_img_back_order_summary);
		btnBackOrderSummery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(OrderSummery.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.title_text);
		mTitleTextView.setText("Order Summary");
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

		getCounts();
		setLayoutVisibility();
		houseHoldGrandTotal();
		ironGrandTotal();
		GrandTotal();
	}

	private void GrandTotal() {

		grandTotal = houseGrandTot + ironGrandTot + dryCleanSubTotal;
		txt_grand_total = (TextView) findViewById(R.id.txt_grand_total);
		txt_grand_total.setText("$" + grandTotal);

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(OrderSummery.this);
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
			washAndFoldCountList.clear();

			// for (ParseObject houseHoldItems : houseHoldCountObject) {
			// // stateList.add((String) stateName.get("Name"));
			//
			// DC_Order_WashFold washFold = new DC_Order_WashFold();
			//
			// washFold.setEco_Detergent(houseHoldItems.get("Eco_Detergent")
			// .toString());
			// washFold.setScent_Free(houseHoldItems.get("Scent_Free")
			// .toString());
			// washFold.setWeight_Est_Count(houseHoldItems.get(
			// "Weight_Est_Count").toString());
			// washFold.setItem_Count(houseHoldItems.get("Household_Count")
			// .toString());
			// washFold.setInventory_Count(houseHoldItems.get(
			// "Inventory_Count").toString());
			// washFold.setPickup_Date(houseHoldItems.get("Pickup_Date")
			// .toString());
			// washFold.setDelivery_Date(houseHoldItems.get("Delivery_Date")
			// .toString());
			// washFold.setPaid(houseHoldItems.get("Paid").toString());
			// washFold.setOrderId(houseHoldItems.get("OrderId").toString());
			// washFold.setObjectId(houseHoldItems.getObjectId());
			//
			// washAndFoldCountList.add(washFold);
			//
			// }

			// for (ParseObject ironItems : ironCountObject) {
			// // stateList.add((String) stateName.get("Name"));
			//
			// DC_Order_WashFold washIron = new DC_Order_WashFold();
			//
			// washIron.setEco_Detergent(ironItems.get("Eco_Detergent")
			// .toString());
			// washIron.setScent_Free(ironItems.get("Scent_Free").toString());
			//
			// washIron.setItem_Count(ironItems.get("Iron_Count").toString());
			//
			// washIron.setPickup_Date(ironItems.get("Pickup_Date").toString());
			// washIron.setDelivery_Date(ironItems.get("Delivery_Date")
			// .toString());
			// washIron.setPaid(ironItems.get("Paid").toString());
			// washIron.setOrderId(ironItems.get("OrderId").toString());
			// washIron.setObjectId(ironItems.getObjectId());
			//
			// ironCountList.add(washIron);
			//
			// }

			for (ParseObject dryCleanItems : dryCleanCountObject) {
				// stateList.add((String) stateName.get("Name"));

				DC_InsertFields dryclean = new DC_InsertFields();

				dryclean.setItemName(dryCleanItems.get("DryClean_Items")
						.toString());
				dryclean.setPrice(dryCleanItems.get("DryClean_Count")
						.toString());
				dryclean.setCount(dryCleanItems.get("DryClean_Count")
						.toString());
				dryclean.setOrderId(dryCleanItems.get("OrderId").toString());

				dryCleanCountList.add(dryclean);

			}

			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			// setCounts();

			// setWashAndFoldAdapter();

		}
	}

	private void ironGrandTotal() {

		ironGrandTot = addIronEco + addIronEcoScent + addWashIronSubTotal;
		txt_wash_iron_tot_count.setText("$" + ironGrandTot);

	}

	public void setCounts() {

		txt_wash_fold_tot_count.setText("" + washAndFoldCountList.size());

		ironCountList.size();
		dryCleanCountList.size();

	}

	private void getdryCleanSubTotal() {

		List<DC_InsertFields> datalist = helper.getDrycleanData();
		for (int i = 0; i < datalist.size(); i++) {

			float price = Float.parseFloat(datalist.get(i).getPrice());
			int floatQty = Integer.parseInt(datalist.get(i).getCount());
			if (floatQty != 0) {

				float subTotal = price * floatQty;
				dryCleanSubTotal = dryCleanSubTotal + subTotal;
			}
		}
		txt_dry_clean_tot_count.setText("$" + dryCleanSubTotal);

	}

	private void getWashIronSubTotal() {

		List<DC_InsertFields> datalist = helper.getIronData();
		for (int i = 0; i < datalist.size(); i++) {

			float price = Float.parseFloat(datalist.get(i).getPrice());
			int floatQty = Integer.parseInt(datalist.get(i).getCount());
			if (floatQty != 0) {

				float subTotal = price * floatQty;
				addWashIronSubTotal = addWashIronSubTotal + subTotal;
			}
		}

	}

	private void getWashFoldSubTotal() {
		List<DC_InsertFields> datalist = helper.getHouseholdData();
		for (int i = 0; i < datalist.size(); i++) {

			float price = Float.parseFloat(datalist.get(i).getPrice());
			int floatQty = Integer.parseInt(datalist.get(i).getCount());
			if (floatQty != 0) {

				float subTotal = price * floatQty;
				addWashFoldSubTotal = addWashFoldSubTotal + subTotal;
			}
		}

	}

	private void setWashFoldSubTotal() {
		DecimalFormat df = new DecimalFormat("#.##");
		// tv_wash_fold_tot_count.setText("$" + df.format(houseGrandTot));

	}

	private void setLayoutVisibility() {

		// if (householdCount == 0) {
		//
		// wash_and_fold_main_layout.setVisibility(View.GONE);
		// } else {
		// wash_and_fold_main_layout.setVisibility(View.VISIBLE);
		// }
		// if (ironCount == 0) {
		// wash_and_iron_main_layout.setVisibility(View.GONE);
		// } else {
		// wash_and_iron_main_layout.setVisibility(View.VISIBLE);
		// }
		// if (dryCleanCount == 0) {
		// dry_cleaning_main_layout.setVisibility(View.GONE);
		// } else {
		// dry_cleaning_main_layout.setVisibility(View.VISIBLE);
		// }
	}

	private void getCounts() {
		// TODO Auto-generated method stub

		householdCount = helper.getHouseholdCount();
		inventoryCount = helper.getInventoryCount();
		ironCount = helper.getIronCount();
		dryCleanCount = helper.getDrycleanCount();

		setCount();

	}

	private void setCount() {

		txt_household_item_count.setText(":" + householdCount);
		txt_inventory_item_count.setText(":" + inventoryCount);
		txt_iron_item_count.setText(":" + ironCount);
		txt_dry_clean_item_count.setText(":" + dryCleanCount);

	}

	private void getSharedPreference() {

		serviceName = sharedPref.getString("serviceName", "");
		washFoldEcoPressed = sharedPref.getBoolean("washFoldEcoPressed", false);
		washFoldscent = sharedPref.getBoolean("washFoldscent", false);
		washIronEcoPressed = sharedPref.getBoolean("washIronEcoPressed", false);
		washIronscent = sharedPref.getBoolean("washIronscent", false);
		weightEstimate = sharedPref.getString("weightEstimate", "");
		foldtxtPickDate = sharedPref.getString("foldPickDate", "");
		foldtxtDeliveryDate = sharedPref.getString("foldDeliveryDate", "");

		irontxtPickDate = sharedPref.getString("ironPickDate", "");
		irontxtDeliveryDate = sharedPref.getString("ironDeliveryDate", "");

		drytxtPickDate = sharedPref.getString("dryPickDate", "");
		drytxtDeliveryDate = sharedPref.getString("dryDeliveryDate", "");

		setWashFoldOption();
		setWashIronOption();
		getWeightTotByFormula();

		totalQuantity = sharedPref.getString("clothTotal", "0");
		clothWeight = sharedPref.getString("Clothweight", "");
		pickupDate = sharedPref.getString("PickUpdt", "");
		pickupTime = sharedPref.getString("PickUptm", "");
		deliveryDate = sharedPref.getString("Delivery", "");

	}

	private void setWashIronOption() {

		String ecoText = "";
		String scentText = "";
		String optionText = ":";

		if (washIronEcoPressed && washIronscent) {
			ecoText = "Eco Detergent";
			scentText = "Scent free softner";
			optionText = ":" + ecoText + "," + scentText;
			addEco = helper.getIronCount() * 0.05f;
			addScent = helper.getIronCount() * 0.05f;
			editor.putBoolean("washIronEcoPressed", false);
			editor.putBoolean("washFoldscent", false);
		} else {
			if (washIronEcoPressed) {

				ecoText = "Eco Detergent";
				optionText = ":" + ecoText;
				addIronEco = helper.getIronCount() * 0.05f;
				editor.putBoolean("washIronEcoPressed", false);
			}
			if (washIronscent) {
				scentText = "Scent free softner";
				optionText = ":" + scentText;
				addIronEcoScent = helper.getIronCount() * 0.05f;
				editor.putBoolean("washIronscent", false);
			}
		}

		editor.commit();
		setIronOptionText(optionText);

	}

	private void setIronOptionText(String optionText) {

		txt_iron_action.setText(optionText);

	}

	private void getWeightTotByFormula() {
		if (!weightEstimate.isEmpty()) {
			txt_weight_est.setText(":" + weightEstimate + " lbs");
			addWeight = Float.parseFloat(weightEstimate) * 1.25f;
		}
	}

	private void setWashFoldOption() {

		String ecoText = "";
		String scentText = "";
		String optionText = ":";

		if (washFoldEcoPressed && washFoldscent) {
			ecoText = "Eco Detergent";
			scentText = "Scent free softner";
			optionText = ":" + ecoText + "," + scentText;
			addEco = Float.parseFloat(weightEstimate) * 0.05f;
			addScent = Float.parseFloat(weightEstimate) * 0.05f;
			editor.putBoolean("washFoldEcoPressed", false);
			editor.putBoolean("washFoldEcoPressed", false);
		} else {
			if (washFoldEcoPressed) {

				ecoText = "Eco Detergent";
				optionText = ":" + ecoText;
				addEco = Float.parseFloat(weightEstimate) * 0.05f;
				editor.putBoolean("washFoldEcoPressed", false);
			}
			if (washFoldscent) {
				scentText = "Scent free softner";
				optionText = ":" + scentText;
				addScent = Float.parseFloat(weightEstimate) * 0.05f;
				editor.putBoolean("washFoldEcoPressed", false);
			}
		}

		editor.commit();
		setFoldOptionText(optionText);

	}

	private void setFoldOptionText(String optionText) {

		txt_option_selected.setText(optionText);
	}

	private void initControls() {
		dry_cleanning_sub_layout = (LinearLayout) findViewById(R.id.dry_cleanning_sub_layout);
		wash_and_fold_sub_layout = (LinearLayout) findViewById(R.id.wash_and_fold_sub_layout);
		wash_and_iron_sub_layout = (LinearLayout) findViewById(R.id.wash_and_iron_sub_layout);
		dry_cleaning_main_layout = (LinearLayout) findViewById(R.id.dry_cleaning_main_layout);
		wash_and_fold_main_layout = (LinearLayout) findViewById(R.id.wash_and_fold_main_layout);
		wash_and_iron_main_layout = (LinearLayout) findViewById(R.id.wash_and_iron_main_layout);
		txt_dry_clean_item_count = (TextView) findViewById(R.id.txt_dry_clean_item_count);
		txt_household_item_count = (TextView) findViewById(R.id.txt_household_item_count);
		txt_inventory_item_count = (TextView) findViewById(R.id.txt_inventory_item_count);
		txt_iron_item_count = (TextView) findViewById(R.id.txt_iron_item_count);
		txt_option_selected = (TextView) findViewById(R.id.txt_option_selected);
		txt_iron_action = (TextView) findViewById(R.id.txt_iron_action);
		txt_dry_clean_tot_count = (TextView) findViewById(R.id.txt_dry_clean_tot_count);
		txt_wash_fold_tot_count = (TextView) findViewById(R.id.txt_wash_fold_tot_count);
		txt_weight_est = (TextView) findViewById(R.id.txt_weight_est);
		txt_wash_iron_tot_count = (TextView) findViewById(R.id.txt_wash_iron_tot_count);

		btn_View_summary = (Button) findViewById(R.id.btn_View_summary);
		btn_View_summary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!pickaddressEdite.getText().toString().equals(" ")
						&& !deliveryaddress.getText().toString().equals("")) {
					Intent intent = new Intent(OrderSummery.this,
							OrderSummaryNew.class);
					startActivity(intent);
				} else if (!deliveryaddress.getText().toString().equals("")) {
					Toast.makeText(OrderSummery.this,
							"Please Enter Pick Address", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(OrderSummery.this,
							"Please Enter Delivery Address", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		img_iron_edit = (ImageView) findViewById(R.id.img_iron_edit);
		img_iron_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderSummery.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent);
			}
		});

		img_fold_edit = (ImageView) findViewById(R.id.img_fold_edit);
		img_fold_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderSummery.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent);
			}
		});
		img_dry_edit = (ImageView) findViewById(R.id.img_dry_edit);
		img_dry_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderSummery.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent);
			}
		});

		img_cart = (ImageView) findViewById(R.id.img_cart);

		img_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderSummery.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});

		dryCleanShowHide = (ImageView) findViewById(R.id.img_show_hide_dryclean);
		dryCleanShowHide.setOnClickListener(new OnClickListener() {

			int button01pos = 0;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (button01pos == 0) {
					dry_cleanning_sub_layout.setVisibility(View.VISIBLE);
					button01pos = 1;
				} else if (button01pos == 1) {
					dry_cleanning_sub_layout.setVisibility(View.GONE);
					button01pos = 0;
				}
			}
		});
		WashFoldShowHide = (ImageView) findViewById(R.id.img_show_hide_fold);
		WashFoldShowHide.setOnClickListener(new OnClickListener() {

			int button01pos = 0;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (button01pos == 0) {
					wash_and_fold_sub_layout.setVisibility(View.VISIBLE);
					button01pos = 1;
				} else if (button01pos == 1) {
					wash_and_fold_sub_layout.setVisibility(View.GONE);
					button01pos = 0;
				}
			}
		});
		WashIronShowHide = (ImageView) findViewById(R.id.img_show_hide_iron);
		WashIronShowHide.setOnClickListener(new OnClickListener() {

			int button01pos = 0;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (button01pos == 0) {
					wash_and_iron_sub_layout.setVisibility(View.VISIBLE);
					button01pos = 1;
				} else if (button01pos == 1) {
					wash_and_iron_sub_layout.setVisibility(View.GONE);
					button01pos = 0;
				}
			}
		});
		layout_household = (LinearLayout) findViewById(R.id.layout_household);
		layout_household.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(OrderSummery.this);
				// Include dialog.xml file
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

				dialog.setContentView(R.layout.household_dialog);
				// Set dialog title
				final ListView lvHouseHold;
				final TextView total_household;
				lvHouseHold = (ListView) dialog.findViewById(R.id.lv_household);
				total_household = (TextView) dialog
						.findViewById(R.id.total_household);
				total_household.setText("$" + addWashFoldSubTotal);
				ImageView img_dialog_close = (ImageView) dialog
						.findViewById(R.id.img_dialog_close);
				img_dialog_close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				SqlHelper helper = new SqlHelper(OrderSummery.this);
				List<DC_InsertFields> datalist = helper.getHouseholdData();
				AdapterHouseHoldDialog adptr = new AdapterHouseHoldDialog(
						OrderSummery.this, datalist);
				lvHouseHold.setAdapter(adptr);
				dialog.show();
			}
		});

		layout_inventory = (LinearLayout) findViewById(R.id.layout_inventory);
		layout_inventory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(OrderSummery.this);
				// Include dialog.xml file
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

				dialog.setContentView(R.layout.inventory_dialog);
				// Set dialog title
				final ListView lvHouseHold;
				lvHouseHold = (ListView) dialog.findViewById(R.id.lv_household);
				TextView total_inventory_count = (TextView) dialog
						.findViewById(R.id.total_inventory_count);
				total_inventory_count.setText(": " + helper.getInventoryCount()
						+ " Items");
				ImageView img_dialog_close = (ImageView) dialog
						.findViewById(R.id.img_inventory_dialog_close);
				img_dialog_close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				SqlHelper helper = new SqlHelper(OrderSummery.this);
				List<DC_InsertFields> datalist = helper.getInventoryData();
				AdapterHouseholdDialogInventory adptr = new AdapterHouseholdDialogInventory(
						OrderSummery.this, datalist);

				lvHouseHold.setAdapter(adptr);
				dialog.show();

			}
		});

		layout_dryClean = (LinearLayout) findViewById(R.id.layout_dry_clean);
		layout_dryClean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(OrderSummery.this);
				// Include dialog.xml file
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

				dialog.setContentView(R.layout.household_dialog);
				// Set dialog title
				final ListView lvHouseHold;
				lvHouseHold = (ListView) dialog.findViewById(R.id.lv_household);
				TextView txt_dialog_heading = (TextView) dialog
						.findViewById(R.id.txt_dialog_heading);
				ImageView img_dialog_close = (ImageView) dialog
						.findViewById(R.id.img_dialog_close);
				img_dialog_close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				TextView total_household = (TextView) dialog
						.findViewById(R.id.total_household);
				total_household.setText(":$" + dryCleanSubTotal);
				getDryCleanItems();
				txt_dialog_heading.setText("Dry Cleaning Item");
				SqlHelper helper = new SqlHelper(OrderSummery.this);
				List<DC_InsertFields> datalist = helper.getDrycleanData();
				AdapterHouseHoldDialog adptr = new AdapterHouseHoldDialog(
						OrderSummery.this, datalist);
				lvHouseHold.setAdapter(adptr);
				dialog.show();
			}
		});

		layout_washAndIron = (LinearLayout) findViewById(R.id.layout_wash_and_iron);
		layout_washAndIron.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(OrderSummery.this);
				// Include dialog.xml file
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

				dialog.setContentView(R.layout.household_dialog);
				// Set dialog title
				final ListView lvHouseHold;
				lvHouseHold = (ListView) dialog.findViewById(R.id.lv_household);

				TextView total_household = (TextView) dialog
						.findViewById(R.id.total_household);
				total_household.setText(":$" + addWashIronSubTotal);
				TextView txt_dialog_heading = (TextView) dialog
						.findViewById(R.id.txt_dialog_heading);
				txt_dialog_heading.setText("Wash And Iron Items");
				ImageView img_dialog_close = (ImageView) dialog
						.findViewById(R.id.img_dialog_close);
				img_dialog_close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				SqlHelper helper = new SqlHelper(OrderSummery.this);
				List<DC_InsertFields> datalist = helper.getIronData();
				AdapterHouseHoldDialog adptr = new AdapterHouseHoldDialog(
						OrderSummery.this, datalist);
				lvHouseHold.setAdapter(adptr);
				dialog.show();
			}
		});

	}

	protected void getDryCleanItems() {
		if (CommanMethods.isConnected(getBaseContext())) {

			new RemoteDataTask().execute();
		}

	}

	private void houseHoldGrandTotal() {

		houseGrandTot = addEco + addScent + addWashFoldSubTotal + addWeight;
		setWashFoldSubTotal();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private class EnterAddress extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String address = params[0];
			if (address.equals("Enter pickaddress")) {
				pickuppaddress();
				
			} else {
				deliveryaddress();
			}

			return address;
		}

		protected void onPostExecute(String res) {
			pDialog.dismiss();
			if (res.equals("Enter pickaddress")) {
				enterpickaddress.setText("Change Address");
				
			} else {
				enterdeliveryaddress.setText("Change Address");
			}
		}

		protected void onPreExecute() {
			// do something before execution
			pDialog = new ProgressDialog(OrderSummery.this);
			pDialog.setMessage("Please Wait..."); // typically

			// will define
			// such
			// strings in a remote file.
			pDialog.setCancelable(true);
			pDialog.setIndeterminate(false);
			pDialog.show();
		}
	}

	private class EditeAddress extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String address = params[0];
			if (address.equals("Edite pickaddress")) {
				editepickupaddress();
			} else {
				editedelviryaddress();
			}

			return null;
		}

		protected void onPostExecute(String res) {
			pDialog.dismiss();
		}

		protected void onPreExecute() {
			// do something before execution
			pDialog = new ProgressDialog(OrderSummery.this);
			pDialog.setMessage("Please Wait..."); // typically

			// will define
			// such
			// strings in a remote file.
			pDialog.setCancelable(true);
			pDialog.setIndeterminate(false);
		}
	}
	private void pickuppaddress() {
		ParseObject orderHead = new ParseObject("User_Address");
		orderHead.put("pickup_address", pickaddressEdite.getText().toString());
        email_id=sessionmanger.getemailid();
		orderHead.put("user_emailid", email_id);

		orderHead.saveInBackground();
		sessionmanger.address("pickaddress", pickaddressEdite.getText().toString());
	}

	public void deliveryaddress() {
		// TODO Auto-generated method stub
		ParseObject orderHead = new ParseObject("User_Address");
		orderHead.put("delivey_address", deliveryaddress.getText().toString());

		orderHead.put("user_emailid", email_id);

		orderHead.saveInBackground();
		sessionmanger.address("delivery_address", deliveryaddress.getText().toString());
		
	}	
	
	public void editepickupaddress()
	{
		/*ParseObject orderHead = new ParseObject("User_Address");
		// ParseQuery<ParseObject> query = ParseQuery.getQuery("User_Address");
		orderHead.whereEqualTo("user_emailid", "user_emailid"); 
		orderHead.put("pickup_address", pickaddressEdite.getText().toString());
        

		 query.saveInBackground();*/
		
	/*	try {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("User_Address");
			query.whereEqualTo("user_emailid", email_id); 
			query.findInBackground(new FindCallback<ParseObject>() { 
			 
				@Override
				public void done(List<ParseObject> arg0, ParseException e) {
					// TODO Auto-generated method stub
					  if (e == null) { 
				            ParseObject person = arg0.get(0);
				            person.put("pickup_address", pickaddressEdite.getText().toString());
				            person.saveInBackground();
				        } else { 
				           // Log.d("score", "Error: " + e.getMessage());
				        } 
				} 
			 });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
		
		
		
		sessionmanger.address("pickaddress", pickaddressEdite.getText().toString());
	}
	public void editedelviryaddress()
	{
		/*ParseObject orderHead = new ParseObject("User_Address");
		orderHead.put("delivey_address", deliveryaddress.getText().toString());

		orderHead.put("user_emailid", email_id);

		orderHead.saveInBackground();*/
	/*	ParseQuery<ParseObject> query = ParseQuery.getQuery("User_Address");
		query.whereEqualTo("user_emailid", email_id); 
		query.findInBackground(new FindCallback<ParseObject>() { 
		 
			@Override
			public void done(List<ParseObject> arg0, ParseException e) {
				// TODO Auto-generated method stub
				  if (e == null) { 
			            ParseObject person = arg0.get(0);
			            person.put("delivey_address", deliveryaddress.getText().toString());
			            person.saveInBackground();
			        } else { 
			           // Log.d("score", "Error: " + e.getMessage());
			        } 
			} 
		 }); 
		*/
		
		
		sessionmanger.address("delivery_address", deliveryaddress.getText().toString());
	}

}
