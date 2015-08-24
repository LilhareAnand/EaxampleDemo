package com.washhous.comman;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedDataPrefrence {

	String WashAndFoldHouseHoldItemployComforterQueen,WashAndFoldHouseHoldItemployComfoerterDouble,WashAndFoldHouseHoldItemDownnComforterQueen,WashAndFoldHouseHoldItempillow;
	public void setWashAndFoldHouseHoldItem_polycomfoeter_king(Activity act,String PolyComforter_King){
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("PolyComforter_King", PolyComforter_King);
		editor.commit();
	}
	
	public String getWashAndFoldHouseHoldItem_polycomfoeter_king(Activity act){
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("PolyComforter_King", "0");
		return item;
	}

	public String getWashAndFoldHouseHoldItemployComforterQueen(Activity act) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("PolyComforter_Queen", "0");
		return item;
	}

	public void setWashAndFoldHouseHoldItemployComforterQueen(Activity act,String PolyComforter_Queen) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("PolyComforter_Queen", PolyComforter_Queen);
		editor.commit();
	}

	public String getWashAndFoldHouseHoldItemployComfoerterDouble(Activity act) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("ComfoerterDouble", "0");
		return item;
	}

	public void setWashAndFoldHouseHoldItemployComfoerterDouble(
			Activity act,String ComfoerterDouble) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("ComfoerterDouble", ComfoerterDouble);
		editor.commit();
	}

	public String getWashAndFoldHouseHoldItemDownnComforterQueen(Activity act) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("DownnComforterQueen", "0");
		return item;
	}

	public void setWashAndFoldHouseHoldItemDownnComforterQueen(Activity act,String DownnComforterQueen) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("DownnComforterQueen", DownnComforterQueen);
		editor.commit();
	}

	public String getWashAndFoldHouseHoldItempillow(Activity act) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("pillow", "0");
		return item;
	}

	public void setWashAndFoldHouseHoldItempillow(
			Activity act,String pillow) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("pillow", pillow);
		editor.commit();
	}
	
	
	public String getInvertoryShirt(Activity act) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("Shirt", "0");
		return item;
	}

	public void setInvertoryShirt(Activity act,String Shirt) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("Shirt", Shirt);
		editor.commit();
	}
	
	public String getInvertorySkirt(Activity act) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("Skirt", "0");
		return item;
	}

	public void setInvertorySkirt(Activity act,String Skirt) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("Skirt", Skirt);
		editor.commit();
	}
	
	public String getInvertorySweater(Activity act) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("Sweater", "0");
		return item;
	}

	public void setInvertorySweater(Activity act,String Sweater) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("Sweater", Sweater);
		editor.commit();
	}
	public String getInvertoryTrousers(Activity act) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("Trousers", "0");
		return item;
	}

	public void setInvertoryTrousers(Activity act,String Trousers) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("Trousers", Trousers);
		editor.commit();
	}
	public String getInvertoryCoat(Activity act) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("Coat", "0");
		return item;
	}

	public void setInvertoryCoat(Activity act,String Coat) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashAndFold", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("Coat", Coat);
		editor.commit();
	}
	
	public int getWashIronOrderItemCount(Activity act,int pos) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashIronOrderItemCount", act.MODE_PRIVATE);
		int item=sharedpreferences.getInt("WashIronOrderItemCount"+pos, 0);
		return item;
	}

	public void setWashIronOrderItemCount(Activity act,int pos,int count) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashIronOrderItemCount", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putInt("WashIronOrderItemCount"+pos, count);
		editor.commit();
	}
	public String getWashIronOrderItemName(Activity act,int pos) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashIronOrderItemCount", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("WashIronOrderItemName"+pos, "");
		return item;
	}

	public void setWashIronOrderItemName(Activity act,int pos,String Name) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashIronOrderItemCount", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("WashIronOrderItemName"+pos, Name);
		editor.commit();
	}
	public String getWashIronOrderItemPrice(Activity act,int pos) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashIronOrderItemCount", act.MODE_PRIVATE);
		String item=sharedpreferences.getString("WashIronOrderItemPrice"+pos, "");
		return item;
	}

	public void setWashIronOrderItemPrice(Activity act,int pos,String Name) {
		SharedPreferences sharedpreferences = act.getSharedPreferences("WashIronOrderItemCount", act.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("WashIronOrderItemPrice"+pos, Name);
		editor.commit();
	}
	
}
