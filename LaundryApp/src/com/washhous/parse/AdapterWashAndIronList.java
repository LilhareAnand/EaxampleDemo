package com.washhous.parse;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.washhous.comman.SharedDataPrefrence;
import com.washhous.database.SessionManager;
import com.washhous.database.SqlHelper;
import com.washhous.dataclasses.IornItems;
import com.washhous.laundryapp.R;
import com.washhous.laundryapp.WashAndIronListPage;

public class AdapterWashAndIronList extends BaseAdapter {
	SharedDataPrefrence mSharedDataPrefrence;
	int  seprateCount[];
	private Activity context;
	private List<IornItems> washAndIronItems = new ArrayList<IornItems>();
	private static LayoutInflater inflater = null;
	ViewHolder holder = null;
	SharedPreferences sharedPref;
	Editor editor;
	SqlHelper helper;
	SessionManager sessionManager;
	protected int totalCount = 0;

	public AdapterWashAndIronList(WashAndIronListPage washAndIronPageList,List<IornItems> iornItemsList) {
		context = washAndIronPageList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		washAndIronItems = iornItemsList;
		helper = new SqlHelper(context);
		sharedPref = context.getSharedPreferences("laundry", 0);
		editor = sharedPref.edit();
		seprateCount=new int[washAndIronItems.size()];
		mSharedDataPrefrence=new SharedDataPrefrence();
		sessionManager=new SessionManager(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return washAndIronItems.size();
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
			holder.txtValue = (TextView) vi.findViewById(R.id.wash_and_fold_item_count);
			holder.btnMinus = (Button) vi.findViewById(R.id.minusbutton);
			holder.btnPlus = (Button) vi.findViewById(R.id.plusbutton);
			vi.setTag(holder);

		} else {
			holder = (ViewHolder) vi.getTag();
		}
		String item_name=washAndIronItems.get(position).getItemName();
		if(washAndIronItems.get(position).getItemName().equals("Wool/Down coat"))
		{
			itemString = "" + washAndIronItems.get(position).getItemName() + "($ "+ washAndIronItems.get(position).getPrice()+" "+"and UP" + ")";
		}
		else
		{
			itemString = "" + washAndIronItems.get(position).getItemName() + "($ "+ washAndIronItems.get(position).getPrice() + ")";
		}
		itemString = "" + washAndIronItems.get(position).getItemName() + "($ "+ washAndIronItems.get(position).getPrice() + ")";
		holder.txtItem.setTag(position);
		holder.txtItem.setText(itemString);
		String itemName = washAndIronItems.get(position).getItemName().toString().trim();
		///int ironid = helper.getIronidByItemName(itemName);
		//int cartcount = helper.getIronCountById("" + ironid);
		//holder.txtValue.setText("" + cartcount);
		holder.btnMinus.setTag(position);
		holder.btnMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Button view3 = (Button) v;
				View v1 = (View) v.getParent();
				TextView tvItemCount = (TextView) v1.findViewById(R.id.wash_and_fold_item_count);
				String a = tvItemCount.getText().toString();
				int position = (Integer)v.getTag();//Integer.parseInt((String) v.getTag());
				String itemName = washAndIronItems.get(position).getItemName().toString().trim();

				int ironid = helper.getIronidByItemName(itemName);
				if (!a.equals("") && !a.equals("0")) {
					Log.d("", a);
					int cartcount = helper.getIronCountById("" + ironid);
					// int cartcount = Integer.parseInt(a);
					totalCount = totalCount - 1;

					int count = Integer.parseInt(a) - 1;

					String str = String.valueOf(count);

					seprateCount[position] = seprateCount[position] - 1;

						
						tvItemCount.setText(str);
						helper.updateIronCountByHouseholdItemId(ironid,count);
						//sessionManager.address("ironItemCount",String.valueOf(cartcount));
						//editor.commit();

					sessionManager.address("ironItemCount",
							String.valueOf(totalCount));
				}
				mSharedDataPrefrence.setWashIronOrderItemCount(context,position, seprateCount[position]);
				mSharedDataPrefrence.setWashIronOrderItemName(context,position, itemName);
				mSharedDataPrefrence.setWashIronOrderItemPrice(context, position, washAndIronItems.get(position).getPrice());
				Log.d("Items info ", "Item Info "+itemName+","+seprateCount[position]+",");
			}
		});

		holder.btnPlus.setTag(position);
		holder.btnPlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button view = (Button) v;
				View v1 = (View) v.getParent();
				TextView tvItemCount = (TextView) v1
						.findViewById(R.id.wash_and_fold_item_count);
				String a = tvItemCount.getText().toString();
				//int position = Integer.parseInt((String) v.getTag());
				int position = (Integer)v.getTag();
				String itemName = washAndIronItems.get(position).getItemName().toString().trim();
				int ironid = helper.getIronidByItemName(itemName);
				int cartcount = helper.getIronCountById("" + ironid);
				seprateCount[position] = seprateCount[position] + 1;
				int count = Integer.parseInt(a) + 1;
				// int cartcount = Integer.parseInt(a);
				totalCount = totalCount + 1;
				String str = String.valueOf(count);
				tvItemCount.setText(str);
				sessionManager.address("ironItemCount",
						String.valueOf(totalCount));
				helper.updateIronCountByHouseholdItemId(ironid, cartcount);
				//editor.putString("ironItemCount", String.valueOf(seprateCount[position]));
				//editor.commit();
				//sessionManager.address("ironItemCount",String.valueOf(cartcount));
				mSharedDataPrefrence.setWashIronOrderItemCount(context,position, seprateCount[position]);
				mSharedDataPrefrence.setWashIronOrderItemName(context,position, itemName);
				mSharedDataPrefrence.setWashIronOrderItemPrice(context, position, washAndIronItems.get(position).getPrice());
				Log.d("Items info ", "Item Info "+itemName+","+seprateCount[position]+",");
			}
		});
		return vi;
	}
}
