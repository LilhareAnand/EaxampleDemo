package com.washhous.tabsswipe;

import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.washhous.database.SessionManager;
import com.washhous.laundryapp.R;
import com.washhous.tabsswipe.adapter.TabsPagerAdapter;

public class OrderSummaryNew extends FragmentActivity implements
		ActionBar.TabListener ,OnClickListener{
	
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	String ss;

	double washFoldTotal,washIronTotal,dryCleanTotal,finalTotal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_summary_new);
		actionBar = getActionBar();
		//commting by ananand
		LayoutInflater li=getLayoutInflater();
		View view=li.inflate(R.layout.actionbar_order_summary2, null);
		ImageButton btn_img_back_order_summary=(ImageButton)view.findViewById(R.id.btn_img_back_order_summary);
		btn_img_back_order_summary.setOnClickListener(this);
		TextView title_grandtotal=(TextView)view.findViewById(R.id.title_grandtotal);
		
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		    
		    
		new CalTask(actionBar,title_grandtotal).execute();
		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	
	class CalTask extends AsyncTask<String, String, String>{
		public  double sumDryCln=0,sumWashFold=0,sumWashIron=0,grandTotal=0;
		ActionBar myActionbar;
		TextView title_grandtotal;
		public CalTask(ActionBar acttionbar,TextView title_grandtotal){
			myActionbar=acttionbar;
			this.title_grandtotal=title_grandtotal;
		}
		
		
		public List<ParseObject> houseHoldCountObject;
		public List<ParseObject> ironCountObject;
		public List<ParseObject> dryCleanCountObject;
		ProgressDialog pg;
		SessionManager mySession=new SessionManager(OrderSummaryNew.this);
		@Override
		protected  void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pg.dismiss();
			Log.d("", "result post"+result);
			String ss[]=result.split(",");
			for(int i=0;i<ss.length;i++){
				String afterIndex=ss[i].substring(ss[i].indexOf(".")+1, ss[i].length());
				if(afterIndex.length()>2){
					ss[i]=ss[i].substring(0, ss[i].indexOf(".")+3);
				}
			}
			String[] tabs = { "Wash & Fold\n     ("+ss[0]+")", "Wash & Iron\n      ("+ss[1]+")", 
					"Dry Cleaning\n      ("+ss[2]+")" };
			title_grandtotal.setText("Total: $"+ss[3]);

			//myActionbar.setHomeButtonEnabled(false);
			myActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			// Adding Tabs
			for (String tab_name : tabs) {
				myActionbar.addTab(myActionbar.newTab().setText(tab_name).setTabListener(OrderSummaryNew.this));
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pg=new ProgressDialog(OrderSummaryNew.this);
			pg.setMessage("Please wait...");
			pg.show();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			ParseQuery<ParseObject> houseHoldItems = new ParseQuery<ParseObject>("Order_WashFold");
			ParseQuery<ParseObject> washIrcon = new ParseQuery<ParseObject>("Order_WashIron");
			ParseQuery<ParseObject> dryCleanItems = new ParseQuery<ParseObject>("Order_DryClean");
			houseHoldItems.whereEqualTo("user_id" , mySession.getemailid());
			washIrcon.whereEqualTo("user_id" , mySession.getemailid());
			dryCleanItems.whereEqualTo("user_id" , mySession.getemailid());
			try {
				houseHoldCountObject = houseHoldItems.find();
				for (ParseObject washfoldItems : houseHoldCountObject) {
					double sum1=0;
					sum1=Double.parseDouble(washfoldItems.get("total_sum").toString());
					sumWashFold=sumWashFold+sum1;
				}
				
				ironCountObject = washIrcon.find();
				for (ParseObject washIronItems : ironCountObject) {
					double sum2=0;
					sum2=Double.parseDouble(washIronItems.get("total_sum").toString());
					sumWashIron=sumWashIron+sum2;
				}
				dryCleanCountObject = dryCleanItems.find();
				for (ParseObject drtCleanItems : dryCleanCountObject) {
					double sum3=0;
					sum3=Double.parseDouble(drtCleanItems.get("total_sum").toString());
					sumDryCln=sumDryCln+sum3;
					
					//hi
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			grandTotal=sumWashFold+sumWashIron+sumDryCln;
			grandTotal=Math.floor(grandTotal * 100) / 100;
			return sumWashFold+","+sumWashIron+","+sumDryCln+","+grandTotal;
		}
		
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_img_back_order_summary:
			finish();
			break;

		default:
			break;
		}
	}
}
