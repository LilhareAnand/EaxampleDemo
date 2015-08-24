package com.washhous.parse;

import java.util.ArrayList;
import java.util.List;

import com.washhous.comman.DC_InsertFields;
import com.washhous.laundryapp.R;
import com.washhous.parse.AdapterWashAndIron.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterHouseHoldDialog extends BaseAdapter {
	Context con;
	List<DC_InsertFields> list = new ArrayList<DC_InsertFields>();
	private LayoutInflater inflater;
	private ViewHolder holder;
	private float subTotal;
    TextView totalvalue;
    int pos;
    float finalTotla=0;
	public AdapterHouseHoldDialog(Context con, List<DC_InsertFields> datalist, TextView total_household) {
		// TODO Auto-generated constructor stub
		this.con = con;
		this.list = datalist;
		inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		totalvalue=total_household;
		pos=list.size();
		for(int i=0;i<list.size();i++){
			try {
				int qty = Integer.parseInt(list.get(i).getCount());
				Float price = Float.parseFloat(list.get(i).getPrice());
				Float floatQty = Float.parseFloat(list.get(i).getCount());
				if (qty != 0) {

					subTotal = price * floatQty;
				    finalTotla=subTotal+finalTotla;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			totalvalue.setText("$" + subTotal);
			
		}
		totalvalue.setText("$ "+finalTotla);
	}

	public AdapterHouseHoldDialog(Activity act,
			List<DC_InsertFields> filterList) {
		// TODO Auto-generated constructor stub
		this.con = act;
		this.list = filterList;
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
		TextView txtPrice;
		TextView txtQty;
		TextView txtSubTotal;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		if (convertView == null) {

			vi = inflater.inflate(R.layout.householdlistitem, null);
			holder = new ViewHolder();
			holder.txtItemName = (TextView) vi
					.findViewById(R.id.tv_house_hold_item);
			holder.txtQty = (TextView) vi.findViewById(R.id.tv_house_hold_qty);
			holder.txtPrice = (TextView) vi
					.findViewById(R.id.tv_house_hold_price);
			holder.txtSubTotal = (TextView) vi
					.findViewById(R.id.tv_house_hold_subtotal);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		holder.txtItemName.setText(list.get(position).getItemName());
		holder.txtPrice.setText(list.get(position).getPrice());
		holder.txtQty.setText(list.get(position).getCount());
		subTotal = 0.0f;
		try {
			int qty = Integer.parseInt(list.get(position).getCount());
			Float price = Float.parseFloat(list.get(position).getPrice());
			Float floatQty = Float.parseFloat(list.get(position).getCount());
			if (qty != 0) {

				subTotal = price * floatQty;
			    finalTotla=subTotal+finalTotla;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.txtSubTotal.setText("$" + subTotal);
//        if((pos-1)==position)
//        {
//        	//totalvalue.setText("$ "+finalTotla);
//        }
		return vi;
	}
}
