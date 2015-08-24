package com.washhous.laundryapp;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

public class OrderList extends Fragment {
	private View rootView;
	private ExpandListAdapter ExpAdapter;
	private ArrayList<Group> ExpListItems;
	private ExpandableListView ExpandList;
	private SharedPreferences sharedPref;
	private String serviceName = "";
	private String totalQuantity = "";
	private String serviceType = "";
	private String clothWeight = "";
	private String pickupDate = "";
	private String pickupTime = "";
	private String deliveryDate = "";
	private Button btnOrderList;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.activity_order_list, container,
				false);

		setHasOptionsMenu(true);
		initControls();
		getSharedPreference();
		ExpListItems = ((MyApplication) getActivity().getApplication())
				.addOrderSummery(serviceName, serviceType, totalQuantity,
						clothWeight, pickupDate, pickupTime, deliveryDate);

		ExpAdapter = new ExpandListAdapter(getActivity(), ExpListItems);
		ExpandList.setAdapter(ExpAdapter);

		return rootView;
	}

	private void getSharedPreference() {

		sharedPref = getActivity().getSharedPreferences("laundry", 0);
		serviceName = sharedPref.getString("serviceName", "");
		totalQuantity = sharedPref.getString("clothTotal", "0");
		serviceType = sharedPref.getString("serviceType", "");
		clothWeight = sharedPref.getString("Clothweight", "");
		pickupDate = sharedPref.getString("PickUpdt", "");
		pickupTime = sharedPref.getString("PickUptm", "");
		deliveryDate = sharedPref.getString("Delivery", "");

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.order_list, menu);
	}

	private void initControls() {
		ExpandList = (ExpandableListView) rootView.findViewById(R.id.exp_list);

	}
}
