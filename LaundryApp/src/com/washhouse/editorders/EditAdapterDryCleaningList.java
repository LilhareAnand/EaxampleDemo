package com.washhouse.editorders;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.washhous.database.SqlHelper;
import com.washhous.dataclasses.DryCleaningItems;
import com.washhous.laundryapp.DryCleanPageList;
import com.washhous.laundryapp.MyApplication;
import com.washhous.laundryapp.R;

public class EditAdapterDryCleaningList extends BaseAdapter {

	private Context context;
	private List<DryCleaningItems> dryCleaningItems = new ArrayList<DryCleaningItems>();
	private static LayoutInflater inflater = null;
	ViewHolder holder = null;
	SharedPreferences sharedPref;
	Editor editor;
	SqlHelper helper;
	protected int totalCount = 0;

	public EditAdapterDryCleaningList(Activity dryCleanPageList,
			List<DryCleaningItems> dryCleaningItemsList) {

		context = dryCleanPageList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		helper = new SqlHelper(context);
		dryCleaningItems = dryCleaningItemsList;
		sharedPref = context.getSharedPreferences("laundry", 0);
		editor = sharedPref.edit();

		MyApplication.dryCleaningItemsCount = dryCleaningItemsList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dryCleaningItems.size();
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
		TextView txtItem;
		Button btnMinus;
		TextView txtValue;
		Button btnPlus;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String itemString = "";
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.price_list_item_count, null);
			holder = new ViewHolder();
			holder.txtItem = (TextView) vi.findViewById(R.id.txt_item);
			holder.txtValue = (TextView) vi
					.findViewById(R.id.wash_and_fold_item_count);
			holder.btnMinus = (Button) vi.findViewById(R.id.minusbutton);
			holder.btnPlus = (Button) vi.findViewById(R.id.plusbutton);
			vi.setTag(holder);

		} else {
			holder = (ViewHolder) vi.getTag();
		}

		itemString = "" + dryCleaningItems.get(position).getItemName() + "($ "
				+ dryCleaningItems.get(position).getPrice() + ")";
		holder.txtItem.setText(itemString);

		String itemName = dryCleaningItems.get(position).getItemName()
				.toString().trim();

		holder.btnMinus.setTag(position + "");
		holder.btnMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Button view3 = (Button) v;
				View v1 = (View) v.getParent();
				TextView tvItemCount = (TextView) v1
						.findViewById(R.id.wash_and_fold_item_count);
				String a = tvItemCount.getText().toString();
				int position = Integer.parseInt((String) v.getTag());
				String itemName = dryCleaningItems.get(position).getItemName()
						.toString().trim();

				if (!a.equals("") && !a.equals("0")) {
					totalCount = totalCount - 1;
					int count = Integer.parseInt(a) - 1;
					String str = String.valueOf(count);
					tvItemCount.setText(str);
					addCount(itemName, str);
					editor.putString("dryItemCount", String.valueOf(totalCount));
					editor.commit();
				}
			}
		});

		holder.btnPlus.setTag(position + "");
		holder.btnPlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button view = (Button) v;
				View v1 = (View) v.getParent();
				TextView tvItemCount = (TextView) v1
						.findViewById(R.id.wash_and_fold_item_count);
				String a = tvItemCount.getText().toString();

				int position = Integer.parseInt((String) v.getTag());
				String itemName = dryCleaningItems.get(position).getItemName()
						.toString().trim();

				int count = Integer.parseInt(a) + 1;
				// int cartcount = Integer.parseInt(a);
				totalCount = totalCount + 1;
				String str = String.valueOf(count);
				tvItemCount.setText(str);
				addCount(itemName, str);
				editor.putString("dryItemCount", String.valueOf(totalCount));
				editor.commit();

			}
		});
		return vi;
	}

	protected void addCount(String itemName, String count) {

		for (int i = 0; i < MyApplication.dryCleaningItemsCount.size(); i++) {

			if (MyApplication.dryCleaningItemsCount.get(i).getItemName()
					.equals(itemName)) {

				MyApplication.dryCleaningItemsCount.get(i).setCount(count);
			}
		}
	}
}
