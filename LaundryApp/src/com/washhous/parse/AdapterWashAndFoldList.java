  package com.washhous.parse;

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

import com.washhous.comman.Order_it_status;
import com.washhous.comman.SharedDataPrefrence;
import com.washhous.database.SessionManager;
import com.washhous.database.SqlHelper;
import com.washhous.dataclasses.HouseHoldItem;
import com.washhous.laundryapp.R;
import com.washhous.laundryapp.WashAndFoldListPage;

public class AdapterWashAndFoldList extends BaseAdapter {

	private Activity context;
	private List<HouseHoldItem> washAndFoldItems = new ArrayList<HouseHoldItem>();
	private static LayoutInflater inflater = null;
	ViewHolder holder = null;
	SharedPreferences sharedPref;
	Editor editor;
	protected int totalCount = 0;
	SqlHelper helper;
	SharedDataPrefrence mprefrence;
	int countcal[];
	
SessionManager sessionmanger;
	public AdapterWashAndFoldList(Activity context,
			WashAndFoldListPage washAndFoldListPage,
			List<HouseHoldItem> washAndFoldItemList) {
      
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		washAndFoldItems = washAndFoldItemList;
		helper = new SqlHelper(context);
		sharedPref = context.getSharedPreferences("laundry", 0);
		editor = sharedPref.edit();
		mprefrence = new SharedDataPrefrence();
		sessionmanger = new SessionManager(context);
	     countcal=new int[washAndFoldItems.size()];

	}

	@Override
	public int getCount() {
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
		TextView txtItem;
		Button btnMinus;
		TextView txtValue;
		Button btnPlus;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int pos = position;
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
		holder.btnMinus.setTag(position + "");
		holder.btnPlus.setTag(position + "");

		itemString = "" + washAndFoldItems.get(position).getItemName() + "($ "
				+ washAndFoldItems.get(position).getPrice() + ")";
		holder.txtItem.setText(itemString);

		String itemName = washAndFoldItems.get(position).getItemName()
				.toString().trim();

		//int householdid = helper.getHouseholdidByItemName(itemName);

		//int cartcount = helper.getHouseholdCountById("" + householdid);
		//holder.txtValue.setText("" + cartcount);

		holder.btnMinus.setTag(position + "");
		holder.btnMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				View v1 = (View) v.getParent();
				TextView tvItemCount = (TextView) v1
						.findViewById(R.id.wash_and_fold_item_count);
				String a = tvItemCount.getText().toString();

				int position = Integer.parseInt((String) v.getTag());
				String itemName = washAndFoldItems.get(position).getItemName()
						.toString().trim();

				int householdid = helper.getHouseholdidByItemName(itemName);

				if (!a.equals("") && !a.equals("0")) {

					// int cartcount = Integer.parseInt(a);

						
				
						totalCount = totalCount - 1;

                        int count = Integer.parseInt(a) - 1;
					
						String str = String.valueOf(count);
						tvItemCount.setText(str);

						helper.updateHouseholdCountByHouseholdItemId(
								householdid, count);
sessionmanger.address("housholdcount",String.valueOf(totalCount));
						editor.putString("houseHoldCount",
								String.valueOf(totalCount));
						Order_it_status.setHoushold(String.valueOf(totalCount));
						editor.commit();
						if (pos == 0&&countcal[pos]>0) {
							countcal[pos]--;
							mprefrence.setWashAndFoldHouseHoldItem_polycomfoeter_king(context, countcal[pos]+"");
						} else if (pos == 1&&countcal[pos]>0) {
							countcal[pos]--;
							mprefrence.setWashAndFoldHouseHoldItemployComforterQueen(context, countcal[pos]+"");
						} else if (pos == 2&&countcal[pos]>0) {
							countcal[pos]--;
							mprefrence.setWashAndFoldHouseHoldItemployComfoerterDouble(context, countcal[pos]+"");
						} else if (pos == 3&&countcal[pos]>0) {
							countcal[pos]--;
							mprefrence.setWashAndFoldHouseHoldItemDownnComforterQueen(
											context, countcal[pos]+"");
						} else if (pos == 4&&countcal[pos]>0) {
							countcal[pos]--;
							mprefrence.setWashAndFoldHouseHoldItempillow(context, countcal[pos]+"");
						}

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
				String itemName = washAndFoldItems.get(position).getItemName()
						.toString().trim();

				int householdid = helper.getHouseholdidByItemName(itemName);

				int cartcount = helper.getHouseholdCountById("" + householdid);

				// int cartcount = Integer.parseInt(a);
				int count = Integer.parseInt(a) + 1;
				// int cartcount = Integer.parseInt(a);
				totalCount = totalCount + 1;
				String str = String.valueOf(count);
				//String str = String.valueOf(cartcount);
				tvItemCount.setText(str);
				if (householdid != 0) {

					helper.updateHouseholdCountByHouseholdItemId(householdid,
							cartcount);

				}

				sessionmanger.address("housholdcount",String.valueOf(totalCount));
				if (pos == 0) {
					countcal[pos]++;
					mprefrence.setWashAndFoldHouseHoldItem_polycomfoeter_king(context, countcal[pos]+"");
				} else if (pos == 1) {
					countcal[pos]++;
					mprefrence.setWashAndFoldHouseHoldItemployComforterQueen(context, countcal[pos]+"");
				} else if (pos == 2) {
					countcal[pos]++;
					mprefrence.setWashAndFoldHouseHoldItemployComfoerterDouble(context, countcal[pos]+"");
				} else if (pos == 3) {
					countcal[pos]++;
					mprefrence.setWashAndFoldHouseHoldItemDownnComforterQueen(context, countcal[pos]+"");
				} else if (pos == 4) {
					countcal[pos]++;
					mprefrence.setWashAndFoldHouseHoldItempillow(context, countcal[pos]+"");
				}
			}
		});

		return vi;
	}
}
