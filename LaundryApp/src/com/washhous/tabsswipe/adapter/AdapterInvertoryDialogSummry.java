package com.washhous.tabsswipe.adapter;

import java.util.ArrayList;

import com.washhous.comman.InventoryModel;
import com.washhous.laundryapp.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterInvertoryDialogSummry extends BaseAdapter {

	Holder mHolder=new Holder();
	ArrayList<InventoryModel> inventoryList;
	Activity act;
	private LayoutInflater inflater;
	public AdapterInvertoryDialogSummry(Activity act,ArrayList<InventoryModel> inventoryList) {
		// TODO Auto-generated constructor stub
		this.act=act;
		this.inventoryList=inventoryList;
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return inventoryList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View vi = inflater.inflate(R.layout.inventory_item_dialog, null);
		mHolder.Name = (TextView) vi.findViewById(R.id.txtName);
		mHolder.Count = (TextView) vi.findViewById(R.id.txtCount);
		
		mHolder.Name.setText(inventoryList.get(arg0).getName());
		mHolder.Count.setText(inventoryList.get(arg0).getCount()+"");
		return vi;
	}

	class Holder{
		
		TextView Name,Count;
	}
}
