package com.washhous.parse;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.washhous.dataclasses.DryCleaningItems;
import com.washhous.dataclasses.HouseHoldItem;
import com.washhous.drawermenus.PriceList;
import com.washhous.laundryapp.R;

public class AdapterDryCleaning extends BaseAdapter {

	private Context context;
	private List<DryCleaningItems> washAndFoldItems = new ArrayList<DryCleaningItems>();
	private static LayoutInflater inflater = null;
	ViewHolder holder = null;

	public AdapterDryCleaning(PriceList priceList,
			List<DryCleaningItems> dryCleaningItemsList) {

		context = priceList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		washAndFoldItems = dryCleaningItemsList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return washAndFoldItems.size();
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

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if (convertView == null) {

			vi = inflater.inflate(R.layout.price_list_item, null);
			holder = new ViewHolder();
			holder.txtItemName = (TextView) vi
					.findViewById(R.id.wash_and_fold_item_name);
			holder.txtPrice = (TextView) vi
					.findViewById(R.id.wash_and_fold_result_value);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		try {
			String item_name=washAndFoldItems.get(position)
					.getItemName();
			if(item_name.equals("Wool/Down coat"))
			{
				holder.txtItemName.setText(washAndFoldItems.get(position)
						.getItemName());
				holder.txtPrice.setText(washAndFoldItems.get(position).getPrice()+" and Up");
			}
			else
			{
				holder.txtItemName.setText(washAndFoldItems.get(position)
						.getItemName());
				holder.txtPrice.setText(washAndFoldItems.get(position).getPrice());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return vi;
	}

}
