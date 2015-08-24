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

import com.washhous.comman.SharedDataPrefrence;
import com.washhous.database.SessionManager;
import com.washhous.database.SqlHelper;
import com.washhous.dataclasses.InventoryCheckList;
import com.washhous.laundryapp.InventoryListPage;
import com.washhous.laundryapp.R;

public class AdapterInventoryList extends BaseAdapter {

	private Activity context;
	private List<InventoryCheckList> inventoryItems = new ArrayList<InventoryCheckList>();
	private static LayoutInflater inflater = null;
	ViewHolder holder = null;
	SharedPreferences sharedPref;
	Editor editor;
	protected int totalCount = 0;
	SqlHelper helper;
    SessionManager sessionManger;
	SharedDataPrefrence prefrence=new SharedDataPrefrence();
	public AdapterInventoryList(Activity context,
			List<InventoryCheckList> inventoryCheckLists) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inventoryItems = inventoryCheckLists;
		helper = new SqlHelper(context);
		sharedPref = context.getSharedPreferences("laundry", 0);
		editor = sharedPref.edit();
		sessionManger = new SessionManager(context);	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return inventoryItems.size();
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
		final int pos=position;
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

		itemString = inventoryItems.get(position).getItemName().toString();
		holder.txtItem.setText(itemString);

		String itemName = inventoryItems.get(position).getItemName().toString()
				.trim();

		//int inventoryid = helper.getInventoryidByItemName(itemName);

		//int cartcount = helper.getInventoryCountById("" + inventoryid);

		//holder.txtValue.setText("" + cartcount);
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
				String itemName = inventoryItems.get(position).getItemName()
						.toString().trim();

				int inventoryid = helper.getInventoryidByItemName(itemName);

				if (!a.equals("") && !a.equals("0")) {

					int cartcount = helper.getInventoryCountById(""
							+ inventoryid);

				

					totalCount = totalCount - 1;

					int count = Integer.parseInt(a) - 1;

					String str = String.valueOf(count);

						tvItemCount.setText(str);

						helper.updateInventoryCountByHouseholdItemId(
								inventoryid, cartcount);

						editor.putString("inventoryCount",
								String.valueOf(totalCount));
						sessionManger.address("inventorycount", String.valueOf(totalCount));
						editor.commit();
						if(pos==0){
							prefrence.setInvertoryShirt(context, str);
						}else if(pos==1){
							prefrence.setInvertorySkirt(context, str);
						}else if(pos==2){
							prefrence.setInvertorySweater(context, str);
						}else if(pos==3){
							prefrence.setInvertoryTrousers(context, str);
						}else if(pos==4){
							prefrence.setInvertoryCoat(context, str);
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
				String itemName = inventoryItems.get(position).getItemName()
						.toString().trim();

				int inventoryid = helper.getInventoryidByItemName(itemName);

				int cartcount = helper.getInventoryCountById("" + inventoryid);
				// int cartcount = Integer.parseInt(a);
				int count = Integer.parseInt(a) + 1;
				// int cartcount = Integer.parseInt(a);
				totalCount = totalCount + 1;

				String str = String.valueOf(count);
				tvItemCount.setText(str);

				helper.updateInventoryCountByHouseholdItemId(inventoryid,
						cartcount);

				sessionManger.address("inventorycount", String.valueOf(totalCount));
				if(pos==0){
					prefrence.setInvertoryShirt(context, str);
				}else if(pos==1){
					prefrence.setInvertorySkirt(context, str);
				}else if(pos==2){
					prefrence.setInvertorySweater(context, str);
				}else if(pos==3){
					prefrence.setInvertoryTrousers(context, str);
				}else if(pos==4){
					prefrence.setInvertoryCoat(context, str);
				}
			}
		});

		return vi;
	}

}
