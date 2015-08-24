package com.washhous.parse;

import java.util.ArrayList;
import java.util.List;

import com.washhous.comman.DC_InsertFields;
import com.washhous.laundryapp.R;
import com.washhous.parse.AdapterHouseHoldDialog.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterHouseholdDialogInventory extends BaseAdapter {

	Context con;
	List<DC_InsertFields> list = new ArrayList<DC_InsertFields>();
	private LayoutInflater inflater;
	private ViewHolder holder;
	private float subTotal;

	public AdapterHouseholdDialogInventory(Context orderSummery,
			List<DC_InsertFields> datalist) {
		this.con = orderSummery;
		this.list = datalist;
		inflater = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder {
		TextView txtItemName;
		TextView txtQty;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		if (convertView == null) {

			vi = inflater.inflate(R.layout.household_inventory_listitem, null);
			holder = new ViewHolder();
			holder.txtItemName = (TextView) vi
					.findViewById(R.id.tv_house_hold_item);
			holder.txtQty = (TextView) vi.findViewById(R.id.tv_house_hold_qty);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		holder.txtItemName.setText(list.get(position).getItemName());

		holder.txtQty.setText(list.get(position).getCount());
		return vi;
	}

}
