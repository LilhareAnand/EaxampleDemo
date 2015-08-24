//package com.washhous.database;
//
//
//
//import java.util.Calendar;
//import java.util.Stack;
//
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.app.DatePickerDialog.OnDateSetListener;
//import android.app.TimePickerDialog;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.TimePicker;
//
//import com.washhous.comman.CommanMethods;
//import com.washhous.drawermenus.PlaceAnOrder;
//
//public class OrderOption extends Fragment implements OnClickListener {
//
//	private View rootView;
//	private TextView txtPickUpDate;
//	private TextView txtPickUpTime;
//	private TextView txtDeliveryDate;
//	private TextView txtDeliveryTime;
//	private TextView txtServiceName;
//	private TextView txtWeight;
//	private TextView txtClothQty;
//
//	private ImageView imgEco;
//	private ImageView imgSfs;
//	private ImageView imgEcoPressed;
//	private ImageView imgSfsPressed;
//
//	private Button btnConfirmOrder;
//
//	private LinearLayout layoutHouseholdItem;
//
//	private Calendar cal;
//	private int mDay;
//	private int mMonth;
//	private int mYear;
//	private int mHour;
//	private int mMin;
//	private String selected = "default";
//	private static int weightCount = 0;
//	static final int TIME_DIALOG_ID = 999;
//	Stack<Fragment> stk;
//	private SharedPreferences sharedPref;
//	private String serviceName = "";
//	private String totalQuantity = "0";
//	private Editor editor;
//	private String serviceType;
//	private Spinner spnWeight;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater,
//			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//
//		getSharedPreference();
//		if (serviceName.equals("Wash & Fold")) {
//			rootView = inflater.inflate(
//					R.layout.activity_order_option_wash_fold, container, false);
//
//		} else if (serviceName.equals("Wash & Iron")) {
//			rootView = inflater.inflate(
//					R.layout.activity_order_option_wash_iron, container, false);
//
//		} else if (serviceName.equals("Dry Clean")) {
//			rootView = inflater.inflate(
//					R.layout.activity_order_option_dry_clean, container, false);
//
//		}
//		initControls();
//		sharedPref = getActivity().getSharedPreferences("laundry", 0);
//		editor = sharedPref.edit();
//		setHasOptionsMenu(true);
//		stk = ((MyApplication) getActivity().getApplication())
//				.getFragmentStack();
//		try {
//			if (stk.size() == 3) {
//				for (int i = 3; i > 0; i--) {
//					stk.remove(0);
//				}
//			} else if (stk.size() > 2) {
//				for (int i = 0; i < stk.size(); i++) {
//					stk.remove(i);
//				}
//
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		if (stk.size() == 0) {
//			((MyApplication) getActivity().getApplication())
//					.insertFragment(this);
//		}
//		cal = Calendar.getInstance();
//		mDay = cal.get(Calendar.DAY_OF_MONTH);
//		mMonth = cal.get(Calendar.MONTH) + 1;
//		mYear = cal.get(Calendar.YEAR);
//		mHour = cal.get(Calendar.HOUR_OF_DAY);
//		mMin = cal.get(Calendar.MINUTE);
//		updateDate();
//		updateTime();
//		getSharedPreference();
//		setServiceName();
//		saveServiceType();
//		setEstimatedWeight();
//		setHasOptionsMenu(true);
//		setWeightAdapter();
//		spnWeight.setSelection(19);
//		editor.putString("clothTotal", "0");
//		editor.commit();
//		return rootView;
//	}
//
//	private void setWeightAdapter() {
//
//		ArrayAdapter<String> weightAdapter = new ArrayAdapter<String>(
//				getActivity(), android.R.layout.simple_spinner_item,
//				getResources().getStringArray(R.array.array_weight));
//		weightAdapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spnWeight.setAdapter(weightAdapter);
//
//	}
//
//	private void setEstimatedWeight() {
//
//	}
//
//	private void saveServiceType() {
//
//		editor.putString("serviceType", "Eco Detergent");
//		editor.commit();
//	}
//
//	private void setServiceName() {
//		// TODO Auto-generated method stub
//
//		txtServiceName.setText(serviceName);
//	}
//
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		// TODO Auto-generated method stub
//		super.onCreateOptionsMenu(menu, inflater);
//		menu.clear();
//		inflater.inflate(R.menu.order_option, menu);
//
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//
//		switch (item.getItemId()) {
//		case R.id.action_settings:
//
//			Fragment fragment = null;
//			fragment = new PlaceAnOrder();
//
//			callForNextFragmentBack(fragment);
//
//			break;
//
//		}
//
//		return true;
//	}
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		getSharedPreference();
//		if (!totalQuantity.equalsIgnoreCase("0")) {
//
//			txtClothQty.setText(totalQuantity);
//		}
//	}
//
//	private void getSharedPreference() {
//
//		sharedPref = getActivity().getSharedPreferences("laundry", 0);
//		serviceName = sharedPref.getString("serviceName", "");
//		totalQuantity = sharedPref.getString("clothTotal", "0");
//		serviceType = sharedPref.getString("serviceType", "");
//	}
//
//	private void initControls() {
//		// TODO Auto-generated method stub
//
//		txtWeight = (TextView) rootView.findViewById(R.id.txt_household_items);
//		txtPickUpDate = (TextView) rootView.findViewById(R.id.txt_pickup_date);
//		txtPickUpTime = (TextView) rootView.findViewById(R.id.txt_pickup_time);
//		txtDeliveryDate = (TextView) rootView
//				.findViewById(R.id.txt_delivery_date);
//		txtDeliveryTime = (TextView) rootView
//				.findViewById(R.id.txt_delivery_time);
//
//		txtServiceName = (TextView) rootView
//				.findViewById(R.id.txt_service_name);
//		txtClothQty = (TextView) rootView.findViewById(R.id.txt_cloth_qty);
//		imgEco = (ImageView) rootView.findViewById(R.id.img_eco);
//		imgSfs = (ImageView) rootView.findViewById(R.id.img_sfs);
//		imgEcoPressed = (ImageView) rootView.findViewById(R.id.img_eco_pressed);
//		imgSfsPressed = (ImageView) rootView.findViewById(R.id.img_sfs_pressed);
//		layoutHouseholdItem = (LinearLayout) rootView
//				.findViewById(R.id.layout_household_items);
//		btnConfirmOrder = (Button) rootView
//				.findViewById(R.id.btn_confirm_order);
//		spnWeight = (Spinner) rootView.findViewById(R.id.spn_weight_estimate);
//		spnWeight.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		imgEco.setOnClickListener(this);
//		imgSfs.setOnClickListener(this);
//		imgEcoPressed.setOnClickListener(this);
//		imgSfsPressed.setOnClickListener(this);
//		txtPickUpDate.setOnClickListener(this);
//		txtPickUpTime.setOnClickListener(this);
//		txtDeliveryDate.setOnClickListener(this);
//		txtDeliveryTime.setOnClickListener(this);
//		layoutHouseholdItem.setOnClickListener(this);
//
//		btnConfirmOrder.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//
//		switch (v.getId()) {
//		case R.id.txt_pickup_date:
//			selected = "PickUp";
//			setDate();
//			break;
//		case R.id.txt_delivery_date:
//			selected = "Delivery";
//			setDate();
//			break;
//		case R.id.txt_pickup_time:
//			selected = "PickUp";
//			setTime();
//			break;
//
//		case R.id.txt_delivery_time:
//			selected = "Delivery";
//			setTime();
//			break;
//
//		case R.id.layout_household_items:
//
//			Fragment fragment = new Cloths();
//			callForNextFragment(fragment);
//			break;
//
//		case R.id.img_eco:
//
//			imgEco.setVisibility(View.GONE);
//			imgEcoPressed.setVisibility(View.VISIBLE);
//
//			editor.putString("serviceTypeEco", "Eco Detergent");
//			editor.commit();
//			getSharedPreference();
//			break;
//
//		case R.id.img_eco_pressed:
//
//			imgEco.setVisibility(View.VISIBLE);
//			imgEcoPressed.setVisibility(View.GONE);
//
//			editor.putString("serviceTypeEco", "Eco Detergent");
//			editor.commit();
//			getSharedPreference();
//			break;
//
//		case R.id.img_sfs:
//
//			imgSfs.setVisibility(View.GONE);
//			imgSfsPressed.setVisibility(View.VISIBLE);
//			editor.putString("serviceTypeScent", "Scent Free Softner");
//			editor.commit();
//			getSharedPreference();
//			break;
//
//		case R.id.img_sfs_pressed:
//
//			imgSfs.setVisibility(View.VISIBLE);
//			imgSfsPressed.setVisibility(View.GONE);
//			editor.putString("serviceTypeScent", "");
//			editor.commit();
//			getSharedPreference();
//			break;
//
//		case R.id.btn_confirm_order:
//
//			confirmOrderDialog();
//
//			break;
//
//		}
//	}
//
//	private void confirmOrderDialog() {
//
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//				getActivity());
//		alertDialogBuilder.setTitle("Add More Order");
//		alertDialogBuilder
//				.setMessage("Do you want to order more?")
//				.setCancelable(false)
//				.setPositiveButton("Yes",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int id) {
//								// if this button is clicked, close
//								// current activity
//								CommanMethods.myCustomToast(getActivity(),
//										"Work in Progress");
//							}
//						})
//				.setNegativeButton("No", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						// if this button is clicked, just close
//						// the dialog box and do nothing
//
//						savePreferences();
//
//						Fragment fragment = null;
//						fragment = new OrderSummery();
//						callForNextFragment(fragment);
//						dialog.cancel();
//					}
//				});
//
//		// create alert dialog
//		AlertDialog alertDialog = alertDialogBuilder.create();
//
//		// show it
//		alertDialog.show();
//
//	}
//
//	protected void savePreferences() {
//
//		String txtClothweight = txtWeight.getText().toString();
//		String txtPickUpdt = txtPickUpDate.getText().toString();
//		String txtPickUptm = txtPickUpTime.getText().toString();
//		// String txtDelivery = btnDeliveryDate.getText().toString();
//
//		editor.putString("Clothweight", txtClothweight);
//		editor.putString("PickUpdt", txtPickUpdt);
//		editor.putString("PickUptm", txtPickUptm);
//		// editor.putString("Delivery", txtDelivery);
//
//		editor.commit();
//
//	}
//
//	private void callForNextFragmentBack(Fragment fragment) {
//
//		FragmentManager fragmentManager = ((MyApplication) getActivity()
//				.getApplication()).getManager();
//		FragmentTransaction ft = fragmentManager.beginTransaction();
//		Stack<Fragment> stk = ((MyApplication) getActivity().getApplication())
//				.getFragmentStack();
//		if (stk.size() > 0) {
//			stk.lastElement().onPause();
//			ft.remove(stk.lastElement());
//			stk.push(fragment);
//
//		}
//
//		ft.add(R.id.frame_container, fragment).commit();
//
//	}
//
//	private void callForNextFragment(Fragment fragment) {
//
//		FragmentManager fragmentManager = ((MyApplication) getActivity()
//				.getApplication()).getManager();
//		FragmentTransaction ft = fragmentManager.beginTransaction();
//		Stack<Fragment> stk = ((MyApplication) getActivity().getApplication())
//				.getFragmentStack();
//		if (stk.size() > 0) {
//			stk.lastElement().onPause();
//			ft.hide(stk.lastElement());
//			stk.push(fragment);
//			FragmentManager fm = getFragmentManager(); // or
//														// 'getSupportFragmentManager();'
//			int count = fm.getBackStackEntryCount();
//			for (int i = 0; i < count; ++i) {
//				fm.popBackStack();
//			}
//		}
//
//		ft.add(R.id.frame_container, fragment).commit();
//
//	}
//
//	private void setTime() {
//
//		new TimePickerDialog(getActivity(), timePickerListener,
//				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
//				.show();
//	}
//
//	public void setDate() {
//
//		new DatePickerDialog(getActivity(), mDateSetListener,
//				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
//				cal.get(Calendar.DAY_OF_MONTH)).show();
//	}
//
//	private DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener() {
//
//		public void onDateSet(DatePicker view, int year, int monthOfYear,
//				int dayOfMonth) {
//			// TODO Auto-generated method stub
//			mYear = year;
//			mMonth = monthOfYear + 1;
//			mDay = dayOfMonth;
//
//			updateDateAccordingToType();
//
//		}
//	};
//
//	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
//		public void onTimeSet(TimePicker view, int selectedHour,
//				int selectedMinute) {
//			mHour = selectedHour;
//			mMin = selectedMinute;
//
//			updateTimeAccordingToType();
//		}
//	};
//
//	private static String pad(int c) {
//		if (c >= 10)
//			return String.valueOf(c);
//		else
//			return "0" + String.valueOf(c);
//	}
//
//	protected void updateTimeAccordingToType() {
//
//		if (mHour < 12) {
//
//			if (selected.equalsIgnoreCase("PickUp")
//					|| selected.equalsIgnoreCase("default")) {
//				txtPickUpTime
//						.setText(new StringBuilder().append(pad(mHour))
//								.append(":").append(pad(mMin)).append(" ")
//								.append("AM"));
//
//			} else {
//				txtDeliveryTime
//						.setText(new StringBuilder().append(pad(mHour))
//								.append(":").append(pad(mMin)).append(" ")
//								.append("AM"));
//
//			}
//
//		} else {
//
//			int currentHour = mHour - 12;
//			if (currentHour == 0) {
//
//				if (selected.equalsIgnoreCase("PickUp")
//						|| selected.equalsIgnoreCase("default")) {
//					txtPickUpTime.setText(new StringBuilder()
//							.append(pad(mHour)).append(":").append(pad(mMin))
//							.append(" ").append("PM"));
//
//				} else {
//					txtDeliveryTime.setText(new StringBuilder()
//							.append(pad(mHour)).append(":").append(pad(mMin))
//							.append(" ").append("PM"));
//
//				}
//
//			} else {
//
//				if (selected.equalsIgnoreCase("PickUp")
//						|| selected.equalsIgnoreCase("default")) {
//
//					txtPickUpTime.setText(new StringBuilder()
//							.append(pad(currentHour)).append(":")
//							.append(pad(mMin)).append(" ").append("PM"));
//
//				} else {
//					txtDeliveryTime.setText(new StringBuilder()
//							.append(pad(currentHour)).append(":")
//							.append(pad(mMin)).append(" ").append("PM"));
//
//				}
//
//			}
//
//		}
//
//	}
//
//	protected void updateTime() {
//
//		if (mHour < 12) {
//
//			txtPickUpTime.setText(new StringBuilder().append(pad(mHour))
//					.append(":").append(pad(mMin)).append(" ").append("AM"));
//
//			txtDeliveryTime.setText(new StringBuilder().append(pad(mHour))
//					.append(":").append(pad(mMin)).append(" ").append("AM"));
//
//		} else {
//
//			int currentHour = mHour - 12;
//			if (currentHour == 0) {
//				txtPickUpTime
//						.setText(new StringBuilder().append(pad(mHour))
//								.append(":").append(pad(mMin)).append(" ")
//								.append("PM"));
//
//				txtDeliveryTime
//						.setText(new StringBuilder().append(pad(mHour))
//								.append(":").append(pad(mMin)).append(" ")
//								.append("PM"));
//
//			} else {
//				txtPickUpTime.setText(new StringBuilder()
//						.append(pad(currentHour)).append(":").append(pad(mMin))
//						.append(" ").append("PM"));
//
//				txtDeliveryTime.setText(new StringBuilder()
//						.append(pad(currentHour)).append(":").append(pad(mMin))
//						.append(" ").append("PM"));
//
//			}
//
//		}
//
//	}
//
//	protected void updateDateAccordingToType() {
//
//		String year = "";
//		String month = "";
//		String day = "";
//
//		if (mMonth < 10) {
//			month = "0" + mMonth;
//		} else {
//			month = "" + mMonth;
//		}
//		if (mDay < 10) {
//			day = "0" + mDay;
//		} else {
//			day = "" + mDay;
//		}
//		if (mYear < 10) {
//			year = "0" + mYear;
//		} else {
//			year = "" + mYear;
//		}
//
//		if (selected.equalsIgnoreCase("PickUp")
//				|| selected.equalsIgnoreCase("default")) {
//			txtPickUpDate.setText(new StringBuilder().append(day).append("-")
//					.append(month).append("-").append(year));
//
//		} else if (selected.equalsIgnoreCase("Delivery")) {
//			txtDeliveryDate.setText(new StringBuilder().append(day).append("-")
//					.append(month).append("-").append(year));
//		}
//
//	}
//
//	protected void updateDate() {
//
//		String year = "";
//		String month = "";
//		String day = "";
//
//		if (mMonth < 10) {
//			month = "0" + mMonth;
//		} else {
//			month = "" + mMonth;
//		}
//		if (mDay < 10) {
//			day = "0" + mDay;
//		} else {
//			day = "" + mDay;
//		}
//		if (mYear < 10) {
//			year = "0" + mYear;
//		} else {
//			year = "" + mYear;
//		}
//
//		txtPickUpDate.setText(new StringBuilder().append(day).append("-")
//				.append(month).append("-").append(year));
//
//		txtDeliveryDate.setText(new StringBuilder().append(day).append("-")
//				.append(month).append("-").append(year));
//
//	}
//
//}
