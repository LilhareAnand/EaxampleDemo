package com.washhouse.editorders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseObject;
import com.washhous.database.SessionManager;
import com.washhous.database.SqlHelper;
import com.washhous.laundryapp.MyApplication;
import com.washhous.laundryapp.OrderSummery;
import com.washhous.laundryapp.R;
import com.washhous.menudrawer.MainActivity;

public class EditDryCleaningOrder extends Activity {
	private SharedPreferences sharedPref;
	private Editor editor;
	private int dryCleanCount;
	private TextView txtDryCount;
	private LinearLayout dryLayout;
	private ImageButton imgBtnBack;
	private boolean dryDone;
	private SqlHelper helper;
	private Calendar cal;
	private int mDay;
	private int mMonth;
	private int mYear;
	private int mHour;
	private int mMin;
	private String selected = "default";
	private TextView txtPickUpDate;
	private TextView txtDeliveryDate;
	private int orderId;
	// private static int weightCount = 0;
	static final int TIME_DIALOG_ID = 999;
	  String  formated_time="";
		//Timepicker
		  static final int TIME_DIALOG_ID1 = 2;
		  private int hours, min;
		  String year = "";
			String month = "";
			String day = "";
			  SessionManager sessionManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_option_dry_clean);
		initControls();
		helper = new SqlHelper(getApplicationContext());
		sharedPref = getSharedPreferences("laundry", 0);
		editor = sharedPref.edit();
		sessionManager= new SessionManager(EditDryCleaningOrder.this);
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar_all_options,null);
		imgBtnBack = (ImageButton) mCustomView.findViewById(R.id.btn_img_back);
		imgBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditDryCleaningOrder.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent);
			}
		});

		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
		cal = Calendar.getInstance();
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mMonth = cal.get(Calendar.MONTH) + 1;
		mYear = cal.get(Calendar.YEAR);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMin = cal.get(Calendar.MINUTE);
		updateDate();
		getCounts();
		
		//get data from parse
		new GetDataDryClean(txtPickUpDate,txtDeliveryDate).execute();
	}

	private void insertOrderData() {
		ParseObject orderHead = new ParseObject("Order_DryClean");
		orderHead
				.put("DryClean_Count", txtDryCount.getText().toString().trim());
		orderHead.put("Pickup_Date", txtPickUpDate.getText().toString().trim());
		orderHead.put("Delivery_Date", txtDeliveryDate.getText().toString()
				.trim());
		orderHead.put("Paid", "No");
		String emaild=sessionManager.getemailid();
		orderHead.put("user_id", emaild);
		
		orderHead.put("OrderId", String.valueOf(MyApplication.orderId));
		orderHead.saveInBackground();
		

	}

	private void updateDate() {

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
		//adding currentTime 
		String currentTime=getcurrentTime();
		txtPickUpDate.setText(new StringBuilder().append(day).append("-")
				.append(month).append("-").append(year)+" "+currentTime);
		
		
		//addding code for increase date delivery date by 2 anand.
		String currentdate=year+"-"+month+"-"+day;
        String deliverydate= addingTwoday(currentdate);
		//txtDeliveryDate.setText(new StringBuilder().append(day).append("-")
				//.append(month).append("-").append(year));
		
		txtDeliveryDate.setText(deliverydate+" "+currentTime);

	}

	
	//this fuction use for increase two day.
		private String addingTwoday(String currentdate) {
			// TODO Auto-generated method stub
			String currentdateStr="";
			//String dt = "2012-01-04";  // Start date
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    Calendar c = Calendar.getInstance();
		    try {
				c.setTime(sdf.parse(currentdate));
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		    c.add(Calendar.DATE, 2);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
		    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		    currentdateStr = sdf1.format(c.getTime()); 
			
			return currentdateStr;
		}
		private String getcurrentTime() {
			// TODO Auto-generated method stub
			String currentTime;
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1h"));
	        Date currentLocalTime = cal.getTime();
	        DateFormat date = new SimpleDateFormat("HH:mm a"); 
			currentTime=date.format(currentLocalTime).toString();
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
		         
		         formated_time=new StringBuilder().append(hours).append(':')
			                .append(minutes).append(" ").append(timeSet).toString();
		          
			
		}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getCounts();

	}

	private void initControls() {
		TableLayout contact_table = (TableLayout) findViewById(R.id.tblMainDry);
		txtPickUpDate = (TextView) findViewById(R.id.pickvalue);
		txtDeliveryDate = (TextView) findViewById(R.id.deliveryvalue);
		txtDryCount = (TextView) findViewById(R.id.txt_dry_value);
		final View row2 = contact_table.getChildAt(2);
		row2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selected = "PickUp";
				setDate();
				setTimedate();
			}
		});

		final View row3 = contact_table.getChildAt(3);
		row3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selected = "Delivery";
				setDate();
				setTimedate();
			}
		});
		dryLayout = (LinearLayout) findViewById(R.id.linear_confirm_orderdry);
		dryLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!txtDryCount.getText().toString().equals("0")) {

					confirmOrderDialog();
				} else {
					Toast.makeText(getApplicationContext(),
							"Please select Dry Clean items", 1000).show();
				}
			}
		});

		final View row1 = contact_table.getChildAt(1);
		row1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(EditDryCleaningOrder.this,
						EditDryCleanList.class);
				startActivity(intent);

			}
		});

	}
	protected void setTimedate() {
		// TODO Auto-generated method stub
		
		new TimePickerDialog(EditDryCleaningOrder.this, timeListener, hours, min, false).show();
	}
	protected void setDate() {

		new DatePickerDialog(EditDryCleaningOrder.this, mDateSetListener,
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

	//adding dialog box for timing picker by ananand
		 private TimePickerDialog.OnTimeSetListener timeListener = 
			        new TimePickerDialog.OnTimeSetListener() {

			            @Override
			            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			                hours = hourOfDay;
			                min = minute;
			                updateTime12(hours,min);
			              
			            }
			        
			    };
	protected void confirmOrderDialog() {

		final Dialog dialog = new Dialog(EditDryCleaningOrder.this);
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
				Intent intent = new Intent(EditDryCleaningOrder.this,
						OrderSummery.class);
				startActivity(intent);
				insertOrderData();
				insertOrderDetails();
				MyApplication.generatedOrderId();
				dialog.dismiss();
			}
		});
		Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditDryCleaningOrder.this,
						MainActivity.class);
				startActivity(intent);

				dialog.dismiss();
				finish();
			}
		});
		dialog.show();

	}

	protected void insertOrderDetails() {

		for (int i = 0; i < MyApplication.dryCleaningItemsCount.size(); i++) {
			ParseObject orderHead = new ParseObject("DryClean_Item");
			orderHead.put("OrderId", String.valueOf(MyApplication.orderId));
			orderHead.put("DryClean_Items", MyApplication.dryCleaningItemsCount
					.get(i).getItemName());
			Log.d("Items", "Items "+MyApplication.dryCleaningItemsCount
					.get(i).getItemName());
			if (MyApplication.dryCleaningItemsCount.get(i).getCount() == null) {
				orderHead.put("Count", "0");

			} else {
				orderHead.put("Count",
						MyApplication.dryCleaningItemsCount.get(i).getCount());
				MyApplication.dryCleaningItemsCount.get(i).setCount("0");
			}

			orderHead.put("Price", MyApplication.dryCleaningItemsCount.get(i)
					.getPrice());

			orderHead.saveInBackground();
		}

	}

	protected void updateDateAccordingToType() {

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

		  String currentTime=getcurrentTime();
          if (selected.equalsIgnoreCase("PickUp")
  				|| selected.equalsIgnoreCase("default")) {
  			
  			txtPickUpDate.setText(new StringBuilder().append(day).append("-")
  					.append(month).append("-").append(year)+" "+formated_time);
  			//Increament time of deliveryDate; anand
  			String currentdate=year+"-"+month+"-"+day;
  	        String deliverydate= addingTwoday(currentdate);
  	       
  	        txtDeliveryDate.setText(deliverydate+" "+formated_time);
  		} else if (selected.equalsIgnoreCase("Delivery")) {
  			txtDeliveryDate.setText(new StringBuilder().append(day).append("-")
  					.append(month).append("-").append(year)+" "+formated_time);
  		}


	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(EditDryCleaningOrder.this,
				MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	private void getCounts() {
		// TODO Auto-generated method stub

		dryCleanCount = Integer.parseInt(sharedPref.getString("dryItemCount",""));
		setText();

	}

	private void setText() {

		txtDryCount.setText("" + dryCleanCount);
	}

	class GetDataDryClean extends AsyncTask<String, String, String>{

		TextView txtPickUpDate,txtDeliveryDate;
		GetDataDryClean(TextView txtPickUpDate,TextView txtDeliveryDate){
			this.txtPickUpDate=txtPickUpDate;
			this.txtDeliveryDate=txtDeliveryDate;
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		
	}
}
