package com.washhous.laundryapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class HouseHoldcustomAdapter extends BaseAdapter{
	LayoutInflater inflater;
	Context context;
	public ArrayList<String>cloth_name;
	public HouseHoldcustomAdapter(Context activity,
			ArrayList<String> name_cloth) {
		// TODO Auto-generated constructor stub
		
		context=activity;
		cloth_name=name_cloth;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cloth_name.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		PhraseHolder holder;
		final int pos = position;
		if (row == null) {

			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			row = inflater.inflate(R.layout.price_list_item_count, parent, false);

			// row = inflater.inflate(layoutResID, parent, false);
			holder = new PhraseHolder();

			holder.plusButton = (Button) row
					.findViewById(R.id.plusbutton);
			holder.minusButton = (Button) row.findViewById(R.id.minusbutton);
			holder.itemName = (TextView) row.findViewById(R.id.wash_and_fold_item_name);
			holder.valueName=(TextView)row.findViewById(R.id.wash_and_fold_result_value);

			row.setTag(holder);
		} else {
			holder = (PhraseHolder) row.getTag();

		}

		holder.itemName.setText(cloth_name.get(position));
		
		

		return row;
	}

	private static class PhraseHolder {
		Button plusButton,minusButton;
		TextView itemName,valueName;

	}
}

