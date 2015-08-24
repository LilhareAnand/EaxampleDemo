package com.washhous.laundryapp;

import java.util.ArrayList;
import java.util.Stack;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Cloths extends Fragment {
	private String Colth_name[]={"Poly Comorter-King($25.00)","Poly Comforter-Queen($23.00)","Poly Comforter-Double($21.00)","Down Comforter-Queen($29.27)","Pillow($10.00)"};
	private View rootView;
	private Stack<Fragment> stk;
	ListView list;
	public ArrayList<String> name_cloth = new ArrayList<String>();
	private SharedPreferences sharedPref;
	private Editor editor;
	LayoutInflater inflater;
    HouseHoldcustomAdapter housholdadapter;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.activity_cloths, container, false);
		list=(ListView)rootView.findViewById(R.id.list);
		for (int i = 0; i < Colth_name.length; i++) {
			name_cloth .add(Colth_name[i]);
			
		}
		
		housholdadapter = new HouseHoldcustomAdapter(getActivity(),name_cloth);
		list.setAdapter(housholdadapter);
		setHasOptionsMenu(true);
		stk = ((MyApplication) getActivity().getApplication())
				.getFragmentStack();
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.cloths, menu);
	}

}
