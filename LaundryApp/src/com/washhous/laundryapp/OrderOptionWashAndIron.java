package com.washhous.laundryapp;

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
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.parse.ParseObject;
import com.washhous.comman.SharedDataPrefrence;
import com.washhous.database.SessionManager;
import com.washhous.database.SqlHelper;
import com.washhous.menudrawer.MainActivity;

public class OrderOptionWashAndIron extends FragmentActivity{
	SharedDataPrefrence mSharedDataPrefrence;
	private ImageView imgWashAndIronEcoPressed;
	private ImageView imgWashAndIronScentPressed;
	private SharedPreferences sharedPref;
	private Editor editor;
	private int ironCount;
	private TextView txtIronCount;
	private LinearLayout btnLayout;
	private ImageButton imgBtnBack;
	private boolean ironDone;
	private SqlHelper helper;
	protected boolean washIronEcoPressed;
	protected boolean washIronscent;
	SessionManager sessionManager;
	private Calendar cal;
	private int mDay;
	private int mMonth;
	private int mYear;
	private int mHour;
	private int mMin;
	private String selected = "default";
	private TextView txtPickUpDate;
	private TextView txtDeliveryDate;

	// private static int weightCount = 0;
	static final int TIME_DIALOG_ID = 999;

	  String  formated_time="";
		//Timepicker
		  static final int TIME_DIALOG_ID1 = 2;
		  private int hours, min;
		  String year = "";
			String month = "";
			String day = "";
	
			 private SimpleDateFormat mFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
			   private SimpleDateFormat mFormatter1 = new SimpleDateFormat("yyyy-MM-dd");	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_option_wash_iron);
		initControls();
		mSharedDataPrefrence=new SharedDataPrefrence();
		sharedPref = getSharedPreferences("laundry", 0);
		editor = sharedPref.edit();
		helper = new SqlHelper(getApplicationContext());
		sessionManager= new SessionManager(OrderOptionWashAndIron.this);
		cal = Calendar.getInstance();
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mMonth = cal.get(Calendar.MONTH) + 1;
		mYear = cal.get(Calendar.YEAR);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMin = cal.get(Calendar.MINUTE);
		
		updateDate();
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar_wash_iron, null);
		imgBtnBack = (ImageButton) mCustomView.findViewById(R.id.btn_img_back_wash_iron);
		imgBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderOptionWashAndIron.this,MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				sessionManager.address("ironItemCount",String.valueOf(0));
				try {
					helper.deleteAllIronItem();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
		getCounts();
	}	

	private void insertData() {
		boolean ECO=sharedPref.getBoolean("washIronEcoPressed", false);
		boolean washIronscent=sharedPref.getBoolean("washIronscent", false);
		
		String total_sum=calculation(ECO,washIronscent);
		String ItemCount=txtIronCount.getText().toString();
		String peekUpadate=txtPickUpDate.getText().toString();
		String deliveryDate=txtDeliveryDate.getText().toString();
		
		ParseObject orderHead = new ParseObject("Order_WashIron");
		orderHead.put("Eco_Detergent", ECO+"");
		orderHead.put("Scent_Free", washIronscent+"");
		orderHead.put("Iron_Count", ItemCount);
		orderHead.put("Pickup_Date", peekUpadate);
		orderHead.put("Delivery_Date", deliveryDate);
		orderHead.put("Paid", "No");
		String emaild=sessionManager.getemailid();
		orderHead.put("user_id", emaild);
		orderHead.put("OrderId", String.valueOf(MyApplication.orderId));
		orderHead.put("total_sum", total_sum);
		orderHead.saveInBackground();
		sessionManager.address("whashironcount","");
		try {
			helper.deleteAllIronItem();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	 private TimePickerDialog.OnTimeSetListener timeListener = 
		        new TimePickerDialog.OnTimeSetListener() {

		            @Override
		            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		                hours = hourOfDay;
		                min = minute;
		                updateTime12(hours,min);
		              
		            }
		        
		    };	
	
	private String getcurrentTime() {
		// TODO Auto-generated method stub
		String currentTime;
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1h"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a"); 
		currentTime=date.format(currentLocalTime).toString();
		return currentTime;
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
		
		
	private void getCounts() {
		// TODO Auto-generated method stub

		ironCount = helper.getIronCount();
        String ironcount=sessionManager.getaddress("ironItemCount");
       // sessionManager.address("ironItemCount",String.valueOf(cartcount));
        txtIronCount.setText("" + ironcount);

	}

	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getCounts();
	}

	private void initControls() {
		// TODO Auto-generated method stub
		TableLayout contact_table = (TableLayout) findViewById(R.id.tblMainIron);

		txtPickUpDate = (TextView) findViewById(R.id.pickvalue);
		txtDeliveryDate = (TextView) findViewById(R.id.deliveryvalue);

		btnLayout = (LinearLayout) findViewById(R.id.linear_confirm_orderwi);
		btnLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!txtIronCount.getText().toString().equals("0")) {

					confirmOrderDialog();
				} else {
					Toast.makeText(getApplicationContext(),
							"Please select Iron items", 1000).show();
				}
			}
		});

		final View row4 = contact_table.getChildAt(4);
		row4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selected = "PickUp";
				 new SlideDateTimePicker.Builder(getSupportFragmentManager())
                 .setListener(listener)
                 .setInitialDate(new Date())
                 //.setMinDate(minDate)
                 //.setMaxDate(maxDate)
                 //.setIs24HourTime(true)
                 //.setTheme(SlideDateTimePicker.HOLO_DARK)
                 .setIndicatorColor(Color.parseColor("#990000"))
                 .build()
                 .show();
			}
		});

		final View row5 = contact_table.getChildAt(5);
		row5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selected = "Delivery";
				  new SlideDateTimePicker.Builder(getSupportFragmentManager())
	                 .setListener(listener)
	                 .setInitialDate(new Date())
	                 //.setMinDate(minDate)
	                 //.setMaxDate(maxDate)
	                 //.setIs24HourTime(true)
	                 //.setTheme(SlideDateTimePicker.HOLO_DARK)
	                 .setIndicatorColor(Color.parseColor("#990000"))
	                 .build()
	                 .show();
			}
		});

		txtIronCount = (TextView) findViewById(R.id.house_hold_valuewi);

		final View row = contact_table.getChildAt(3);
		row.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(OrderOptionWashAndIron.this,
						WashAndIronListPage.class);
				startActivity(intent);
			}
		});

		imgWashAndIronEcoPressed = (ImageView) findViewById(R.id.img_ecowi);
		imgWashAndIronEcoPressed.setImageResource(R.drawable.detergent_normal);
		imgWashAndIronEcoPressed.setOnClickListener(new OnClickListener() {
			int button01pos = 0;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (button01pos == 0) {
					imgWashAndIronEcoPressed.setImageResource(R.drawable.detergent_pressed);
					button01pos = 1;

					editor.putBoolean("washIronEcoPressed", true);
					editor.commit();
				} else if (button01pos == 1) {
					imgWashAndIronEcoPressed.setImageResource(R.drawable.detergent_normal);
					button01pos = 0;

				}
			}
		});
		imgWashAndIronScentPressed = (ImageView) findViewById(R.id.img_sfswi);
		imgWashAndIronScentPressed.setImageResource(R.drawable.free_scent_normal);
		imgWashAndIronScentPressed.setOnClickListener(new OnClickListener() {
			int button01pos = 0;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (button01pos == 0) {
					imgWashAndIronScentPressed
							.setImageResource(R.drawable.free_scent_pressed);
					button01pos = 1;
					editor.putBoolean("washIronscent", true);
					editor.commit();
				} else if (button01pos == 1) {
					imgWashAndIronScentPressed
							.setImageResource(R.drawable.free_scent_normal);
					button01pos = 0;
				}
			}
		});
	}

	protected void setDate() {

		new DatePickerDialog(OrderOptionWashAndIron.this, mDateSetListener,
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH)).show();

	}

	protected void setTimedate() {
		// TODO Auto-generated method stub
		
		new TimePickerDialog(OrderOptionWashAndIron.this, timeListener, hours, min, false).show();
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

	protected void confirmOrderDialog() {

		final Dialog dialog = new Dialog(OrderOptionWashAndIron.this);
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
				Intent intent = new Intent(OrderOptionWashAndIron.this,
						OrderSummery.class);
				startActivity(intent);
				insertData();
				sessionManager.address("ironItemCount",String.valueOf(0));
				dialog.dismiss();
			}
		});
		Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderOptionWashAndIron.this,
						MainActivity.class);
				startActivity(intent);
				try {
					helper.deleteAllIronItem();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();
				finish();
			}
		});
		dialog.show();

		

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

	protected void clearPreference() {
		sharedPref.edit().remove("ironItemCount").commit();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(OrderOptionWashAndIron.this,
				MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		try {
			helper.deleteAllIronItem();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	 private SlideDateTimeListener listener = new SlideDateTimeListener() {

		  @Override
	        public void onDateTimeSet(Date date)
	        {
	           
	            String date1=mFormatter.format(date);
	            String[] spiltstr=date1.split(" ");
	            formated_time=spiltstr[1];
	            String ampmstr=spiltstr[2];
	            String converdate=mFormatter1.format(date);
	            String currentTime=getcurrentTime();
	            if (selected.equalsIgnoreCase("PickUp")
	    				|| selected.equalsIgnoreCase("default")) {
	    			
	    			txtPickUpDate.setText(date1);
	    			//Increament time of deliveryDate; anand
	    			String currentdate=year+"-"+month+"-"+day;
	    	        String deliverydate= addingTwoday(converdate);
	    	       
	    	        txtDeliveryDate.setText(deliverydate+" "+formated_time+" "+ampmstr);
	    		} else if (selected.equalsIgnoreCase("Delivery")) {
	    			txtDeliveryDate.setText(date1);
	    		} 
	            
	            
	            
	            
	            
	        }

	        // Optional cancel listener
	        @Override
	        public void onDateTimeCancel()
	        {
	           
	        }
	    };
		

private String calculation(boolean eCO, boolean washIronscent2) {
        // TODO Auto-generated method stub
        double totalsum=0.00d,sum1=0,sum2 = 0;
        sum1=ironCount*2.99;
        if(eCO==true||washIronscent2==true){
            sum2=ironCount*0.05;
        }
        totalsum=sum1+sum2;
        return totalsum+"";
    }

	
}
