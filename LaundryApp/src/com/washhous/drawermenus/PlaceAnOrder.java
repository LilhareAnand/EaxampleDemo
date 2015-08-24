package com.washhous.drawermenus;

import java.util.Stack;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.washhous.comman.CommanMethods;
import com.washhous.comman.DC_Order_WashFold;
import com.washhous.laundryapp.InventoryListPage;
import com.washhous.laundryapp.MyApplication;
import com.washhous.laundryapp.OrderOptionDryCleaning;
import com.washhous.laundryapp.OrderOptionWashAndFold;
import com.washhous.laundryapp.OrderOptionWashAndIron;
import com.washhous.laundryapp.OrderSummery;
import com.washhous.laundryapp.R;
import com.washhous.menudrawer.MainActivity;
import com.washhous.tabsswipe.OrderSummaryNew;

public class PlaceAnOrder extends Fragment implements OnClickListener {

	private View rootView;
	Stack<Fragment> stk;
	private TextView txtChooseOptions;
	private TextView txtChooseMultipleOptions;
	private TextView txtwashandFold;
	private TextView txtDryClean;
	private TextView txtWashAndIron;
	private TextView txtOptionTitle;
	private TextView txtOptions;

	private String fontPath;
	private ImageView imgWashAndFold;
	private ImageView imgWashAndIron;
	private ImageView imgDryClean;
	private ImageView imgWashAndFoldPressed;
	private ImageView imgWashAndIronPressed;
	private ImageView imgDryCleanPressed;
	String clickedCircle = "Wash & Fold";
	private Button btnPriceList;
    ImageButton cartImageview;   // change by anand
	SharedPreferences sharedPref;
    boolean Iswashandfold=false,Iswashiron=false,Isdryclaning=false;   //change by anand
	Editor editor;
    OrderStatus orderstatus;
    public ProgressDialog pDialog;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.activity_place_an_order,
				container, false);
		orderstatus=new OrderStatus(getActivity());
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View mCustomView = mInflater.inflate(R.layout.actionbar_place_an_order,
				null);
		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.title_text);
		mTitleTextView.setText("Place An Order");
		ImageButton imageButton = (ImageButton) mCustomView
				.findViewById(R.id.btn_img_back);
		cartImageview=(ImageButton)mCustomView.findViewById(R.id.btn_img_cart);
		
		if (CommanMethods.isConnected(getActivity())) {

			new RemoteDataTask().execute();
		}
		
		
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// Fragment fragment;
				// fragment = new OrderOption();
				// callForNextFragment(fragment);

				if (clickedCircle.equals("Wash & Fold")) {
					Intent i = new Intent(getActivity(),
							OrderOptionWashAndFold.class);
					startActivity(i);
				} else if (clickedCircle.equals("Wash And Iorn")) {
					Intent i = new Intent(getActivity(),
							OrderOptionWashAndIron.class);
					startActivity(i);

				} else if (clickedCircle.equals("Dry Clean")) {
					Intent i = new Intent(getActivity(),
							OrderOptionDryCleaning.class);
					startActivity(i);

				}

			}
		});
		ImageButton imageButtonCart = (ImageButton) mCustomView
				.findViewById(R.id.btn_img_cart);
		imageButtonCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(getActivity(), OrderSummery.class);
				startActivity(intent);
			}
		});
		getActivity().getActionBar().setCustomView(mCustomView);
		getActivity().getActionBar().setDisplayShowCustomEnabled(true);
		fontPath = "fonts/Bariol_Regular.otf";
		initcontrol();
		setInitialMenuImage();
		stk = ((MyApplication) getActivity().getApplication())
				.getFragmentStack();
		try {
			if (stk.size() == 3) {
				for (int i = 3; i > 0; i--) {
					stk.remove(0);
				}
			} else if (stk.size() >= 2) {
				for (int i = 0; i < stk.size(); i++) {
					stk.remove(i);
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		if (stk.size() == 0) {
			((MyApplication) getActivity().getApplication())
					.insertFragment(this);
		}

		sharedPref = getActivity().getSharedPreferences("laundry", 0);
		editor = sharedPref.edit();
		sharedPref.edit().remove("inventoryDone");
		sharedPref.edit().remove("householdCount");

		editor.putString("dryItemCount", "0");
		editor.commit();
	
		setDefaultService();

		return rootView;
	}

	private void setInitialMenuImage() {

		imgWashAndFold.setVisibility(View.GONE);
		imgWashAndFoldPressed.setVisibility(View.VISIBLE);
		imgWashAndIronPressed.setVisibility(View.GONE);
		imgDryCleanPressed.setVisibility(View.GONE);

	}

	private void setDefaultService() {
		// TODO Auto-generated method stub
		sharedPref = getActivity().getSharedPreferences("laundry", 0);
		editor.putString("serviceName", "Wash & Fold");
		editor.commit();

	}

	private void initcontrol() {
		// TODO Auto-generated method stub
		txtChooseOptions = (TextView) rootView
				.findViewById(R.id.txt_choose_option);
		txtChooseMultipleOptions = (TextView) rootView
				.findViewById(R.id.txt_choose_multiple_option);
		txtwashandFold = (TextView) rootView
				.findViewById(R.id.txt_wash_and_fold);
		txtDryClean = (TextView) rootView.findViewById(R.id.txt_dry_clean);
		txtWashAndIron = (TextView) rootView
				.findViewById(R.id.txt_wash_and_iorn);
		txtOptionTitle = (TextView) rootView
				.findViewById(R.id.text_option_title);
		txtOptions = (TextView) rootView.findViewById(R.id.text_options);

		btnPriceList = (Button) rootView.findViewById(R.id.btn_price_list);
		imgWashAndFold = (ImageView) rootView
				.findViewById(R.id.img_wash_and_fold);
		imgWashAndIron = (ImageView) rootView
				.findViewById(R.id.img_wash_and_iorn);
		imgDryClean = (ImageView) rootView.findViewById(R.id.img_dry_clean);

		imgWashAndFoldPressed = (ImageView) rootView
				.findViewById(R.id.img_wash_and_fold_pressed);
		imgWashAndIronPressed = (ImageView) rootView
				.findViewById(R.id.img_wash_and_iorn_pressed);
		imgDryCleanPressed = (ImageView) rootView
				.findViewById(R.id.img_dry_clean_pressed);

		imgWashAndFold.setOnClickListener(this);
		imgWashAndIron.setOnClickListener(this);
		imgDryClean.setOnClickListener(this);

		imgWashAndFoldPressed.setOnClickListener(this);
		imgWashAndIronPressed.setOnClickListener(this);
		imgDryCleanPressed.setOnClickListener(this);
		btnPriceList.setOnClickListener(this);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath);
		// Applying font
		txtChooseOptions.setTypeface(tf);
		txtChooseMultipleOptions.setTypeface(tf);
		txtwashandFold.setTypeface(tf);
		txtDryClean.setTypeface(tf);
		txtWashAndIron.setTypeface(tf);
		txtOptionTitle.setTypeface(tf);
		txtOptions.setTypeface(tf);
	}

	@Override
	public void onClick(View v) {

		Fragment fragment = null;

		switch (v.getId()) {
		case R.id.img_wash_and_fold:
			clickedCircle = "Wash & Fold";
			imgWashAndFold.setVisibility(View.GONE);
			imgWashAndIron.setVisibility(View.VISIBLE);
			imgDryClean.setVisibility(View.VISIBLE);

			imgWashAndFoldPressed.setVisibility(View.VISIBLE);
			imgWashAndIronPressed.setVisibility(View.GONE);
			imgDryCleanPressed.setVisibility(View.GONE);

			txtOptionTitle.setText("Wash & Fold");
			editor.putString("serviceName", "Wash & Fold");
			editor.commit();

			break;

		case R.id.img_wash_and_iorn:
			clickedCircle = "Wash And Iorn";
			txtOptionTitle.setText("Wash And Iorn");

			imgWashAndFold.setVisibility(View.VISIBLE);
			imgWashAndFoldPressed.setVisibility(View.GONE);

			imgWashAndIron.setVisibility(View.GONE);
			imgWashAndIronPressed.setVisibility(View.VISIBLE);

			imgDryClean.setVisibility(View.VISIBLE);
			imgDryCleanPressed.setVisibility(View.GONE);

			txtOptionTitle.setText("Wash And Iorn");
			editor.putString("serviceName", "Wash & Iron");
			editor.commit();
			break;
		case R.id.img_dry_clean:
			clickedCircle = "Dry Clean";
			imgWashAndFold.setVisibility(View.VISIBLE);
			imgWashAndFoldPressed.setVisibility(View.GONE);

			imgWashAndIron.setVisibility(View.VISIBLE);
			imgWashAndIronPressed.setVisibility(View.GONE);

			imgDryClean.setVisibility(View.GONE);
			imgDryCleanPressed.setVisibility(View.VISIBLE);

			txtOptionTitle.setText("Dry Clean");
			editor.putString("serviceName", "Dry Clean");
			editor.commit();
			break;
		case R.id.btn_price_list:
			Intent priceList = new Intent(getActivity(), PriceList.class);
			startActivity(priceList);
			break;

		case R.id.img_wash_and_fold_pressed:

			imgWashAndFold.setVisibility(View.GONE);
			imgWashAndIron.setVisibility(View.VISIBLE);
			imgDryClean.setVisibility(View.VISIBLE);

			imgWashAndFoldPressed.setVisibility(View.VISIBLE);
			imgWashAndIronPressed.setVisibility(View.GONE);
			imgDryCleanPressed.setVisibility(View.GONE);

			txtOptionTitle.setText("Wash And Fold");
			editor.putString("serviceName", "Wash & Fold");
			editor.commit();

			break;
		// case R.id.btn_confirm_order:
		// ShowConfirmDialog();
		// break;
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.place_an_order, menu);

	}

	private void ShowConfirmDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder.setTitle("Add More Order");
		alertDialogBuilder
				.setMessage("Do you want to close the application?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	private void callForNextFragment(Fragment fragment) {

		// FragmentManager fragmentManager = ((MyApplication) getActivity()
		// .getApplication()).getManager();
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		Stack<Fragment> stk = ((MyApplication) getActivity().getApplication())
				.getFragmentStack();
		if (stk.size() > 0) {
			stk.lastElement().onPause();
			ft.hide(stk.lastElement());
			stk.push(fragment);
			FragmentManager fm = getFragmentManager(); // or
														// 'getSupportFragmentManager();'
			int count = fm.getBackStackEntryCount();
			for (int i = 0; i < count; ++i) {
				fm.popBackStack();
			}
		}

		ft.add(R.id.frame_container, fragment).commit();

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

				Iswashandfold=orderstatus.IswashandfoldeOrder();
				Iswashiron=orderstatus.IswashandIronOrder();
				Isdryclaning=orderstatus.IsdrycleaningOrder();

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				if(Isdryclaning==true || Iswashandfold==true ||Iswashiron==true)
				{
					cartImageview.setVisibility(View.VISIBLE);
				}
				else
				{
					cartImageview.setVisibility(View.INVISIBLE);
				}
				if (pDialog != null && pDialog.isShowing()) {
					pDialog.dismiss();
				}

			}
		}	
	
}
