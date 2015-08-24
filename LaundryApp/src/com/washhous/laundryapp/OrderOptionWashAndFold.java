package com.washhous.laundryapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.washhous.comman.Order_it_status;
import com.washhous.comman.SharedDataPrefrence;
import com.washhous.database.SessionManager;
import com.washhous.database.SqlHelper;
import com.washhous.menudrawer.MainActivity;

public class OrderOptionWashAndFold extends FragmentActivity implements
		OnClickListener {

	private TextView txtPickUpDate;
	private TextView txtPickUpTime;
	private TextView txtDeliveryDate;
	private TextView txtDeliveryTime;
	private TextView txtServiceName;
	private TextView txtWeight;
	private TextView txtClothQty;

	private ImageView imgEco;
	private ImageView imgSfs;
	private ImageView imgEcoPressed;
	private ImageView imgSfsPressed;
	ImageButton imgBtnBack;
	private Button btnConfirmOrder;

	private LinearLayout layoutHouseholdItem;
	private String weightEstiValue;
	private Calendar cal;
	private int mDay;
	private int mMonth;
	private int mYear;
	private int mHour;
	private int mMin;
	private String selected = "default";
	// private static int weightCount = 0;
	static final int TIME_DIALOG_ID = 999;
	Stack<Fragment> stk;
	private SharedPreferences sharedPref;
	// private String serviceName = "";
	// private String totalQuantity = "0";
	private Editor editor;

	private TableRow tableRowWeightEstimateWF;
	private TableRow tableRowHouseholdItemsWF;
	private TableRow tableRowInventoryCheckListWF;
	private TableRow tableRowPickupDateWF;
	private TableRow tableRowDeliveryDateWF;
	private TextView weightValueWF;
	private TextView houseHoldValueWF;
	private TextView inventoryValueWF;
	private LinearLayout linearConfirmOrder;
	// private LinearLayout txtConfirmOrder;
	private FragmentManager fragmentManager;
	// private TextView tvWeight;
	final String[] nums = new String[10];
	boolean onWeightDone = false;
	private int houseHoldCount;
	private int inventoryCount;
	private boolean inventoryDone;
	private boolean householdDone;
	private SqlHelper helper;
	private boolean washFoldEcoPressed;
	private boolean washFoldscent;
	String formated_time = "";
	// Timepicker
	static final int TIME_DIALOG_ID1 = 2;
	private int hours, min;
	String year = "";
	String month = "";
	String day = "";
	SessionManager sessionManager;
	private SimpleDateFormat mFormatter = new SimpleDateFormat(
			"dd-MM-yyyy hh:mm aa");
	private SimpleDateFormat mFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_option_wash_fold);
		initControls();
		helper = new SqlHelper(getApplicationContext());
		fragmentManager = getSupportFragmentManager();
		((MyApplication) getApplication()).setManager(fragmentManager);
		sharedPref = getSharedPreferences("laundry", 0);
		editor = sharedPref.edit();
		//getSharedPref();

		// parseUpdate();
		sessionManager = new SessionManager(OrderOptionWashAndFold.this);
		cal = Calendar.getInstance();
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mMonth = cal.get(Calendar.MONTH) + 1;
		mYear = cal.get(Calendar.YEAR);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMin = cal.get(Calendar.MINUTE);
		hours = cal.get(Calendar.HOUR);
		min = cal.get(Calendar.MINUTE);
		updateDate();
		// updateTime();
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater
				.inflate(R.layout.actionbar_wash_fold, null);
		imgBtnBack = (ImageButton) mCustomView
				.findViewById(R.id.btn_img_back_wash_fold);
		imgBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderOptionWashAndFold.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				Order_it_status.setWeight_estimate("0");
				Order_it_status.setHoushold("0");
				Order_it_status.setInvertyStr("0");
				sessionManager.address("inventorycount", "0");
				sessionManager.address("housholdcount", "0");
			}
		});
		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.title_text);
		mTitleTextView.setText("Order Option");

		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
		getCounts();
	}

	private void parseUpdate() {
		// TODO Auto-generated method stub
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Countries");
		query.getInBackground("OauGTWdotP", new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject country, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					country.put("Name", "India");
					country.saveInBackground();

				}

			}
		});
	}

	private void getSharedPref() {
		// TODO Auto-generated method stub

		washFoldEcoPressed = sharedPref.getBoolean("washFoldEcoPressed", false);
		washFoldscent = sharedPref.getBoolean("washFoldscent", false);

		if (washFoldEcoPressed) {

			imgEco.setImageResource(R.drawable.detergent_pressed);

		} else {
			imgEco.setImageResource(R.drawable.detergent_normal);
		}

		if (washFoldscent) {
			imgSfs.setImageResource(R.drawable.free_scent_pressed);
		} else {
			imgSfs.setImageResource(R.drawable.free_scent_normal);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.action_settings:

			// Fragment fragment = null;
			// fragment = new PlaceAnOrder();
			//
			// callForNextFragmentBack(fragment);

			break;

		}

		return true;
	}

	public void numberpicker(NumberPicker np) {
		np.setMaxValue(9);
		np.setMinValue(0);
		np.setWrapSelectorWheel(false);
		np.setDisplayedValues(nums);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getCounts();

	}
	
	private void initControls() {

		weightValueWF = (TextView) findViewById(R.id.weightvaluewf);

		TableLayout contact_table = (TableLayout) findViewById(R.id.tblMain);
		for (int i = 0; i < nums.length; i++) {
			nums[i] = Integer.toString(i);
		}

		txtPickUpDate = (TextView) findViewById(R.id.pickvaluewf);
		/*
		 * txtPickUpDate.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub selected = "PickUp"; setDate(); }
		 * 
		 * });
		 */
		txtDeliveryDate = (TextView) findViewById(R.id.deliveryvaluewf);
		final View row = contact_table.getChildAt(3);
		row.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(OrderOptionWashAndFold.this);
				// Include dialog.xml file
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

				dialog.setContentView(R.layout.weight_dialog);
				// Set dialog title

				final NumberPicker np = (NumberPicker) dialog
						.findViewById(R.id.np);
				final NumberPicker np1 = (NumberPicker) dialog
						.findViewById(R.id.NumberPicker01);
				final NumberPicker np2 = (NumberPicker) dialog
						.findViewById(R.id.NumberPicker02);

				numberpicker(np);
				numberpicker(np1);
				numberpicker(np2);
				np.setValue(2);
				np2.setValue(0);
				final LinearLayout ll = (LinearLayout) dialog
						.findViewById(R.id.Dialogdone);
				ll.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (np1.getValue() == 0) {
							weightValueWF.setText(nums[np.getValue()]
									+ nums[np2.getValue()]);
						} else if (np1.getValue() == 0 && np.getValue() == 0) {
							weightValueWF.setText(nums[np2.getValue()]);
						} else {
							weightValueWF.setText(nums[np1.getValue()]
									+ nums[np.getValue()]
									+ nums[np2.getValue()]);
						}
						Order_it_status.setWeight_estimate(weightValueWF
								.getText().toString());
						dialog.dismiss();

					}
				});
				// set values for custom dialog components - text, image and
				// button

				dialog.show();
			}
		});

		final View row4 = contact_table.getChildAt(4);
		row4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Order_it_status.setHoushold("0");
				Intent intent = new Intent(OrderOptionWashAndFold.this,
						WashAndFoldListPage.class);
				startActivity(intent);

				sessionManager.address("housholdcount", "0");
			}
		});

		final View row5 = contact_table.getChildAt(5);
		row5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Order_it_status.setInvertyStr("0");
				Intent intent = new Intent(OrderOptionWashAndFold.this,
						InventoryListPage.class);
				startActivity(intent);
				sessionManager.address("inventorycount", "0");
			}
		});

		/*
		 * final View row6 = contact_table.getChildAt(6);
		 * row6.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub selected = "PickUp"; setDate();
		 * 
		 * } });
		 * 
		 * final View row7 = contact_table.getChildAt(7);
		 * row7.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub selected = "Delivery"; setDate();
		 * 
		 * } });
		 */

		imgEco = (ImageView) findViewById(R.id.img_ecowf);
		imgEco.setImageResource(R.drawable.detergent_normal);

		imgEco.setOnClickListener(new OnClickListener() {
			int button01pos = 0;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (button01pos == 1) {
					imgEco.setImageResource(R.drawable.detergent_normal);
					button01pos = 0;
					editor.putBoolean("washFoldEcoPressed", false);
				} else if (button01pos == 0) {
					imgEco.setImageResource(R.drawable.detergent_pressed);
					button01pos = 1;
					editor.putBoolean("washFoldEcoPressed", true);

				}
				editor.commit();
			}
		});
		imgSfs = (ImageView) findViewById(R.id.img_sfswf);
		imgSfs.setImageResource(R.drawable.free_scent_normal);

		imgSfs.setOnClickListener(new OnClickListener() {
			int button01pos = 0;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (button01pos == 1) {
					imgSfs.setImageResource(R.drawable.free_scent_normal);
					button01pos = 0;
					editor.putBoolean("washFoldscent", false);
				} else if (button01pos == 0) {

					imgSfs.setImageResource(R.drawable.free_scent_pressed);
					button01pos = 1;
					editor.putBoolean("washFoldscent", true);
				}
			}
		});
		tableRowWeightEstimateWF = (TableRow) findViewById(R.id.table_weight_estimatewf);
		tableRowHouseholdItemsWF = (TableRow) findViewById(R.id.table_household_itemswf);
		tableRowInventoryCheckListWF = (TableRow) findViewById(R.id.table_inventory_checklistwf);

		tableRowPickupDateWF = (TableRow) findViewById(R.id.table_pickup_datewf);
		tableRowDeliveryDateWF = (TableRow) findViewById(R.id.table_delivey_datewf);
		tableRowDeliveryDateWF.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selected = "Delivery";
				new SlideDateTimePicker.Builder(getSupportFragmentManager())
						.setListener(listener)
						.setInitialDate(new Date())
						// .setMinDate(minDate)
						// .setMaxDate(maxDate)
						// .setIs24HourTime(true)
						// .setTheme(SlideDateTimePicker.HOLO_DARK)
						.setIndicatorColor(Color.parseColor("#990000")).build()
						.show();
			}
		});

		tableRowPickupDateWF.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selected = "PickUp";
				new SlideDateTimePicker.Builder(getSupportFragmentManager())
						.setListener(listener)
						.setInitialDate(new Date())
						// .setMinDate(minDate)
						// .setMaxDate(maxDate)
						// .setIs24HourTime(true)
						// .setTheme(SlideDateTimePicker.HOLO_DARK)
						.setIndicatorColor(Color.parseColor("#990000")).build()
						.show();
			}
		});
		houseHoldValueWF = (TextView) findViewById(R.id.house_hold_valuewf);
		houseHoldValueWF.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent i = new Intent(OrderOptionWashAndFold.this,
				// abc.class);
				// startActivity(i);
			}
		});
		inventoryValueWF = (TextView) findViewById(R.id.invetory_valuewf);
		inventoryValueWF.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent i = new Intent(OrderOptionWashAndFold.this,
				// abc.class);
				// startActivity(i);
			}
		});
		linearConfirmOrder = (LinearLayout) findViewById(R.id.linear_confirm_orderwf);

		linearConfirmOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				confirmOrderDialog();

			}
		});

	}

	protected void setTimedate() {
		// TODO Auto-generated method stub

		new TimePickerDialog(OrderOptionWashAndFold.this, timeListener, hours,
				min, false).show();
	}

	private void setDateTime() {

		new DatePickerDialog(OrderOptionWashAndFold.this, mDateSetListener,
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH)).show();

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;

			updateDateAccordingToType();

		}
	};
	protected String pickDate;
	protected String deliveryDate;

	@Override
	public void onClick(View v) {
	}

	private void confirmOrderDialog() {

		final Dialog dialog = new Dialog(OrderOptionWashAndFold.this);
		// Include dialog.xml file
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_add_more);
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
				Intent intent = new Intent(OrderOptionWashAndFold.this,
						OrderSummery.class);
				startActivity(intent);

				weightEstiValue = weightValueWF.getText().toString();
				pickDate = txtPickUpDate.getText().toString();
				deliveryDate = txtDeliveryDate.getText().toString();
				editor.putString("weightEstimate", weightEstiValue);
				editor.putString("foldPickDate", pickDate);
				editor.putString("foldDeliveryDate", deliveryDate);
				editor.commit();

				insertData();
				Order_it_status.setWeight_estimate("0");
				Order_it_status.setHoushold("0");

				sessionManager.address("inventorycount", "0");
				sessionManager.address("housholdcount", "0");
				dialog.dismiss();
			}

			private void insertData() {
				// TODO Auto-generated method stub
				getSharedPref();
				String finalTotal = calculation();
				ParseObject orderHead = new ParseObject("Order_WashFold");
				orderHead.put("Eco_Detergent",
						String.valueOf(washFoldEcoPressed));
				orderHead.put("Scent_Free", String.valueOf(washFoldscent));
				orderHead.put("Weight_Est_Count", weightEstiValue);
				orderHead.put("Household_Count", houseHoldValueWF.getText()
						.toString());
				orderHead.put("Inventory_Count", inventoryValueWF.getText()
						.toString());
				orderHead.put("Pickup_Date", pickDate);
				orderHead.put("Delivery_Date", deliveryDate);
				orderHead.put("Delivery_Date", deliveryDate);
				orderHead.put("Paid", "No");
				orderHead.put("total_sum", finalTotal);
				String emaild = sessionManager.getemailid();
				orderHead.put("user_id", emaild);
				orderHead.put("OrderId", String.valueOf(MyApplication.orderId));
				orderHead.saveInBackground();
				
				clearAllFields();
			}

			private void clearAllFields() {
				// TODO Auto-generated method stub
				SharedDataPrefrence pref = new SharedDataPrefrence();
				pref.setWashAndFoldHouseHoldItem_polycomfoeter_king(OrderOptionWashAndFold.this,"");
				pref.setWashAndFoldHouseHoldItemployComforterQueen(OrderOptionWashAndFold.this,"");
				pref.setWashAndFoldHouseHoldItemployComfoerterDouble(OrderOptionWashAndFold.this,"");
				pref.setWashAndFoldHouseHoldItemDownnComforterQueen(OrderOptionWashAndFold.this,"");
				pref.setWashAndFoldHouseHoldItempillow(OrderOptionWashAndFold.this,"");
				
			}

			private String calculation() {
				int weight = 0, c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0;
				SharedDataPrefrence pref = new SharedDataPrefrence();
				double fianltotal = 0.00d, sum1 = 0, sum2 = 0, sum3 = 0;
				try {
					weight = Integer.parseInt(weightEstiValue);
					c1 = Integer.parseInt(pref.getWashAndFoldHouseHoldItem_polycomfoeter_king(OrderOptionWashAndFold.this));
					c2 = Integer.parseInt(pref.getWashAndFoldHouseHoldItemployComforterQueen(OrderOptionWashAndFold.this));
					c3 = Integer.parseInt(pref.getWashAndFoldHouseHoldItemployComfoerterDouble(OrderOptionWashAndFold.this));
					c4 = Integer.parseInt(pref.getWashAndFoldHouseHoldItemDownnComforterQueen(OrderOptionWashAndFold.this));
					c5 = Integer.parseInt(pref.getWashAndFoldHouseHoldItempillow(OrderOptionWashAndFold.this));
				} catch (Exception e) {
					// TODO: handle exception
				}
				sum1 = weight * 1.25;
				Log.d("", "washFoldEcoPressed " + washFoldEcoPressed
						+ "\nwashFoldscent" + washFoldscent);
				if (washFoldEcoPressed == true || washFoldscent == true) {
					sum2 = weight * 0.05;
				}

				// Items standar price
				/*
				 * Name.add("Ploy Comforter-King"); price.add("25.00");
				 * Size.add(pref.getWashAndFoldHouseHoldItem_polycomfoeter_king(
				 * WashAndFoldListPage.this)+"");
				 * Name.add("Ploy Comforter-Queen"); price.add("23.00");
				 * Size.add(pref.getWashAndFoldHouseHoldItemployComforterQueen(
				 * WashAndFoldListPage.this)+"");
				 * 
				 * Name.add("Ploy Comforter-Double"); price.add("21.00");
				 * Size.add
				 * (pref.getWashAndFoldHouseHoldItemployComfoerterDouble(
				 * WashAndFoldListPage.this)+"");
				 * 
				 * Name.add("Down Comforter-Queen"); price.add("29.27");
				 * Size.add(pref.getWashAndFoldHouseHoldItemDownnComforterQueen(
				 * WashAndFoldListPage.this)+"");
				 * 
				 * Name.add("Pillow"); price.add("10.00");
				 * Size.add(pref.getWashAndFoldHouseHoldItempillow
				 * (WashAndFoldListPage.this)+"");
				 */

				sum3 = c1 * 25 + c2 * 23 + c3 * 21 + c4 * 29 + c5 * 10;
				fianltotal = sum1 + sum2 + sum3;
				return fianltotal + "";

			}
		});

		Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderOptionWashAndFold.this,
						MainActivity.class);
				startActivity(intent);

				dialog.dismiss();
				finish();
			}
		});
		dialog.show();

	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	protected void updateTimeAccordingToType() {

		if (mHour < 12) {

			if (selected.equalsIgnoreCase("PickUp")
					|| selected.equalsIgnoreCase("default")) {
				txtPickUpTime
						.setText(new StringBuilder().append(pad(mHour))
								.append(":").append(pad(mMin)).append(" ")
								.append("AM"));

			} else {
				txtDeliveryTime
						.setText(new StringBuilder().append(pad(mHour))
								.append(":").append(pad(mMin)).append(" ")
								.append("AM"));

			}

		} else {

			int currentHour = mHour - 12;
			if (currentHour == 0) {

				if (selected.equalsIgnoreCase("PickUp")
						|| selected.equalsIgnoreCase("default")) {
					txtPickUpTime.setText(new StringBuilder()
							.append(pad(mHour)).append(":").append(pad(mMin))
							.append(" ").append("PM"));

				} else {
					txtDeliveryTime.setText(new StringBuilder()
							.append(pad(mHour)).append(":").append(pad(mMin))
							.append(" ").append("PM"));

				}

			} else {

				if (selected.equalsIgnoreCase("PickUp")
						|| selected.equalsIgnoreCase("default")) {

					txtPickUpTime.setText(new StringBuilder()
							.append(pad(currentHour)).append(":")
							.append(pad(mMin)).append(" ").append("PM"));

				} else {
					txtDeliveryTime.setText(new StringBuilder()
							.append(pad(currentHour)).append(":")
							.append(pad(mMin)).append(" ").append("PM"));

				}

			}

		}

	}

	protected void updateTime() {

		if (mHour < 12) {

			txtPickUpTime.setText(new StringBuilder().append(pad(mHour))
					.append(":").append(pad(mMin)).append(" ").append("AM"));

			txtDeliveryTime.setText(new StringBuilder().append(pad(mHour))
					.append(":").append(pad(mMin)).append(" ").append("AM"));

		} else {

			int currentHour = mHour - 12;
			if (currentHour == 0) {
				txtPickUpTime
						.setText(new StringBuilder().append(pad(mHour))
								.append(":").append(pad(mMin)).append(" ")
								.append("PM"));

				txtDeliveryTime
						.setText(new StringBuilder().append(pad(mHour))
								.append(":").append(pad(mMin)).append(" ")
								.append("PM"));

			} else {
				txtPickUpTime.setText(new StringBuilder()
						.append(pad(currentHour)).append(":").append(pad(mMin))
						.append(" ").append("PM"));

				txtDeliveryTime.setText(new StringBuilder()
						.append(pad(currentHour)).append(":").append(pad(mMin))
						.append(" ").append("PM"));

			}

		}

	}

	protected void updateDateAccordingToType() {

		if (mMonth < 10) {
			month = "0" + mMonth;
		} else {
			month = "" + mMonth;
		}
		if (mDay < 10) {
			day = "0" + mDay;
		} else {
			day = "" + mDay;
		}
		if (mYear < 10) {
			year = "0" + mYear;
		} else {
			year = "" + mYear;
		}

		String currentTime = getcurrentTime();
		if (selected.equalsIgnoreCase("PickUp")
				|| selected.equalsIgnoreCase("default")) {

			txtPickUpDate.setText(new StringBuilder().append(day).append("-")
					.append(month).append("-").append(year)
					+ " " + formated_time);
			// Increament time of deliveryDate; anand
			String currentdate = year + "-" + month + "-" + day;
			String deliverydate = addingTwoday(currentdate);

			txtDeliveryDate.setText(deliverydate + " " + formated_time);
		} else if (selected.equalsIgnoreCase("Delivery")) {
			txtDeliveryDate.setText(new StringBuilder().append(day).append("-")
					.append(month).append("-").append(year)
					+ " " + formated_time);
		}

	}

	// adding dialog box for timing picker by ananand
	private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			hours = hourOfDay;
			min = minute;
			updateTime12(hours, min);

		}

	};

	private String getcurrentTime() {
		// TODO Auto-generated method stub
		String currentTime;
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1h"));
		Date currentLocalTime = cal.getTime();
		DateFormat date = new SimpleDateFormat("hh:mm a");
		currentTime = date.format(currentLocalTime).toString();
		return currentTime;
	}

	protected void updateTime12(int hours, int mins) {
		// TODO Auto-generated method stub
		String timeSet = "";
		if (hours > 12) {
			hours -= 12;
			timeSet = "PM";
		} else if (hours == 0) {
			hours += 12;
			timeSet = "AM";
		} else if (hours == 12)
			timeSet = "PM";
		else
			timeSet = "AM";

		String minutes = "";
		if (mins < 10)
			minutes = "0" + mins;
		else
			minutes = String.valueOf(mins);

		// Append in a StringBuilder

		formated_time = new StringBuilder().append(hours).append(':')
				.append(minutes).append(" ").append(timeSet).toString();

	}

	protected void updateDate() {

		String year = "";
		String month = "";
		String day = "";

		if (mMonth < 10) {
			month = "0" + mMonth;
		} else {
			month = "" + mMonth;
		}
		if (mDay < 10) {
			day = "0" + mDay;
		} else {
			day = "" + mDay;
		}
		if (mYear < 10) {
			year = "0" + mYear;
		} else {
			year = "" + mYear;
		}

		// adding currentTime
		String currentTime = getcurrentTime();
		txtPickUpDate.setText(new StringBuilder().append(day).append("-")
				.append(month).append("-").append(year)
				+ " " + currentTime);

		// addding code for increase date delivery date by 2 anand.
		String currentdate = year + "-" + month + "-" + day;
		String deliverydate = addingTwoday(currentdate);
		// txtDeliveryDate.setText(new StringBuilder().append(day).append("-")
		// .append(month).append("-").append(year));

		txtDeliveryDate.setText(deliverydate + " " + currentTime);

	}

	// this fuction use for increase two day.
	private String addingTwoday(String currentdate) {
		// TODO Auto-generated method stub
		String currentdateStr = "";
		// String dt = "2012-01-04"; // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(currentdate));
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.add(Calendar.DATE, 2); // number of days to add, can also use
									// Calendar.DAY_OF_MONTH in place of
									// Calendar.DATE
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		currentdateStr = sdf1.format(c.getTime());

		return currentdateStr;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Order_it_status.setWeight_estimate("0");
		Intent intent = new Intent(OrderOptionWashAndFold.this,
				MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);

	}

	private void getCounts() {
		// TODO Auto-generated method stub

		// /houseHoldCount = helper.getHouseholdCount();
		// inventoryCount = helper.getInventoryCount();
		// sessionManager.getaddress("housholdcount");
		// sessionManager.getaddress("inventorycount");
		houseHoldValueWF.setText(sessionManager.getaddress("housholdcount"));
		inventoryValueWF.setText(" "
				+ sessionManager.getaddress("inventorycount"));
		// setCount();

	}

	private void setCount() {
		houseHoldValueWF.setText("" + houseHoldCount);
		inventoryValueWF.setText("" + inventoryCount);
		weightValueWF.setText(Order_it_status.getWeight_estimate());
	}

	private SlideDateTimeListener listener = new SlideDateTimeListener() {

		@Override
		public void onDateTimeSet(Date date) {

			String date1 = mFormatter.format(date);
			String[] spiltstr = date1.split(" ");
			formated_time = spiltstr[1];
			String ampmstr = spiltstr[2];
			String converdate = mFormatter1.format(date);
			String currentTime = getcurrentTime();
			if (selected.equalsIgnoreCase("PickUp")
					|| selected.equalsIgnoreCase("default")) {

				txtPickUpDate.setText(date1);
				// Increament time of deliveryDate; anand
				String currentdate = year + "-" + month + "-" + day;
				String deliverydate = addingTwoday(converdate);

				txtDeliveryDate.setText(deliverydate + " " + formated_time
						+ " " + ampmstr);
			} else if (selected.equalsIgnoreCase("Delivery")) {
				txtDeliveryDate.setText(date1);
			}

		}

		// Optional cancel listener
		@Override
		public void onDateTimeCancel() {

		}
	};

}
