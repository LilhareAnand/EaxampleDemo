package com.washhous.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.washhous.comman.DC_InsertFields;

public class SqlHelper {

	private Context context;
	private SQLiteDatabase db;
   SessionManager sessionmanger;
	public SqlHelper(Context context) {
		this.context = context;
		sessionmanger=new SessionManager(context);
		DBManager openHelper = new DBManager(context);
		db = openHelper.getWritableDatabase();
	}

	public SqlHelper() {

	}

	public void InserHouseholdtValues(DC_InsertFields contact) {

		ContentValues values = new ContentValues();
		values.put(DBManager.HOUSEHOLD_ITEMNAME, contact.getItemName());
		values.put(DBManager.HOUSEHOLD_PRICE, contact.getPrice());
		values.put(DBManager.ITEM_COUNT, 0);
		// Inserting Row
		db.insert(DBManager.TABLE_HOUSEHOLD_ITEM, null, values);
		// Closing database connection
	}

	public void InserIronValues(DC_InsertFields contact) {

		ContentValues values = new ContentValues();
		values.put(DBManager.IRON_ITEMNAME, contact.getItemName());
		values.put(DBManager.IRON_PRICE, contact.getPrice());
		values.put(DBManager.ITEM_COUNT, 0);
		// Inserting Row
		db.insert(DBManager.TABLE_IRON_ITEMS, null, values);
		// Closing database connection
	}

	public void InserDryCleanValues(DC_InsertFields contact) {

		ContentValues values = new ContentValues();
		values.put(DBManager.DRYCLEAN_ITEMNAME, contact.getItemName());
		values.put(DBManager.DRYCLEAN_PRICE, contact.getPrice());
		values.put(DBManager.ITEM_COUNT, 0);
		// Inserting Row
		db.insert(DBManager.TABLE_DRYCLEAN_ITEMS, null, values);
		// Closing database connection
	}

	public void InserInventoryValues(DC_InsertFields contact) {

		ContentValues values = new ContentValues();
		values.put(DBManager.INVENTORY_ITEMNAME, contact.getItemName());
		values.put(DBManager.INVENTORY_PRICE, contact.getPrice());
		values.put(DBManager.ITEM_COUNT, 0);
		// Inserting Row
		db.insert(DBManager.TABLE_INVENTORY_ITEMS, null, values);
		// Closing database connection
	}

	public List<DC_InsertFields> getHouseholdData() {

		List<DC_InsertFields> houseHoldList = new ArrayList<DC_InsertFields>();

		String query = "SELECT * FROM " + DBManager.TABLE_HOUSEHOLD_ITEM;

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					DC_InsertFields houseHoldData = new DC_InsertFields();
					houseHoldData.setItemName(cursor.getString(1));
					houseHoldData.setPrice(cursor.getString(2));
					houseHoldData.setCount(cursor.getString(3));
					houseHoldList.add(houseHoldData);

				} while (cursor.moveToNext());
			}

		}
		return houseHoldList;

	}

	public int getHouseholdCount() {

		int householdCount = 0;
		String query = "SELECT * FROM " + DBManager.TABLE_HOUSEHOLD_ITEM;

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					householdCount = householdCount
							+ Integer.parseInt(cursor.getString(3));

				} while (cursor.moveToNext());
			}

		}
		return householdCount;

	}

	public int getHouseholdCountById(String id) {

		int householdCount = 0;
		String query = "SELECT " + DBManager.ITEM_COUNT + " FROM "
				+ DBManager.TABLE_HOUSEHOLD_ITEM + " WHERE "
				+ DBManager.HOUSEHOLD_ID + "= '" + id + "'";

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					householdCount = householdCount
							+ Integer.parseInt(cursor.getString(0));

				} while (cursor.moveToNext());
			}

		}
		return householdCount;

	}

	public int getIronCountById(String id) {
		int householdCount = 0;
		String query = "SELECT " + DBManager.ITEM_COUNT + " FROM "
				+ DBManager.TABLE_IRON_ITEMS + " WHERE " + DBManager.IRON_ID
				+ " = '" + id + "'";
		db.rawQuery(query, null);

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					householdCount = householdCount
							+ Integer.parseInt(cursor.getString(0));

				} while (cursor.moveToNext());
			}

		}
		return householdCount;
	}

	public int getInventoryCountById(String id) {

		int householdCount = 0;
		String query = "SELECT " + DBManager.ITEM_COUNT + " FROM "
				+ DBManager.TABLE_INVENTORY_ITEMS + " WHERE "
				+ DBManager.INVENTORY_ID + "= '" + id + "'";
		db.rawQuery(query, null);

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					householdCount = householdCount
							+ Integer.parseInt(cursor.getString(0));

				} while (cursor.moveToNext());
			}

		}
		return householdCount;
	}

	public int getInventoryCount() {

		int householdCount = 0;
		String query = "SELECT * FROM " + DBManager.TABLE_INVENTORY_ITEMS;
		db.rawQuery(query, null);

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					householdCount = householdCount
							+ Integer.parseInt(cursor.getString(3));

				} while (cursor.moveToNext());
			}

		}
		return householdCount;
	}

	public int getIronCount() {
		int householdCount = 0;
		String query = "SELECT * FROM " + DBManager.TABLE_IRON_ITEMS;
		db.rawQuery(query, null);

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					householdCount = householdCount
							+ Integer.parseInt(cursor.getString(3));

				} while (cursor.moveToNext());
			}
			
		}
		sessionmanger.address("whashironcount",String.valueOf(householdCount));
		return householdCount;
	}

	public int getDrycleanCount() {
		int householdCount = 0;
		String query = "SELECT * FROM " + DBManager.TABLE_DRYCLEAN_ITEMS;
		db.rawQuery(query, null);

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					householdCount = householdCount
							+ Integer.parseInt(cursor.getString(3));

				} while (cursor.moveToNext());
			}

		}
		return householdCount;
	}

	public int getDrycleanCountById(String id) {
		int householdCount = 0;
		String query = "SELECT " + DBManager.ITEM_COUNT + " FROM "
				+ DBManager.TABLE_DRYCLEAN_ITEMS + " WHERE "
				+ DBManager.DRYCLEAN_ID + "='" + id + "'";
		db.rawQuery(query, null);

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					householdCount = householdCount
							+ Integer.parseInt(cursor.getString(0));

				} while (cursor.moveToNext());
			}

		}
		return householdCount;
	}

	public List<DC_InsertFields> getIronData() {

		List<DC_InsertFields> ironList = new ArrayList<DC_InsertFields>();
		String query = "SELECT * FROM " + DBManager.TABLE_IRON_ITEMS;
		db.rawQuery(query, null);

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					DC_InsertFields ironData = new DC_InsertFields();
					ironData.setItemName(cursor.getString(1));
					ironData.setPrice(cursor.getString(2));
					ironData.setCount(cursor.getString(3));
					ironList.add(ironData);
				} while (cursor.moveToNext());

			}
		}
		return ironList;
	}

	public List<DC_InsertFields> getDrycleanData() {

		List<DC_InsertFields> dryCleanList = new ArrayList<DC_InsertFields>();
		String query = "SELECT * FROM " + DBManager.TABLE_DRYCLEAN_ITEMS;
		db.rawQuery(query, null);

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					DC_InsertFields dryCleanData = new DC_InsertFields();
					dryCleanData.setItemName(cursor.getString(1));
					dryCleanData.setPrice(cursor.getString(2));
					dryCleanData.setCount(cursor.getString(3));

					dryCleanList.add(dryCleanData);
				} while (cursor.moveToNext());

			}
		}
		return dryCleanList;
	}
	//delet all item from house iteme
	public void deleteHousholdItem()
	{
		
		db.delete(DBManager.TABLE_HOUSEHOLD_ITEM , null,null);
		
	}

	public List<DC_InsertFields> getInventoryData() {

		List<DC_InsertFields> inventoryList = new ArrayList<DC_InsertFields>();
		String query = "SELECT * FROM " + DBManager.TABLE_INVENTORY_ITEMS;
		db.rawQuery(query, null);

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				do {

					DC_InsertFields inventoryData = new DC_InsertFields();
					inventoryData.setItemName(cursor.getString(1));
					inventoryData.setPrice(cursor.getString(2));
					inventoryData.setCount(cursor.getString(3));

					inventoryList.add(inventoryData);
				} while (cursor.moveToNext());

			}
		}
		return inventoryList;
	}

	public int getHouseholdidByItemName(String itemName) {

		int itemid = 0;
		String query = "SELECT " + DBManager.HOUSEHOLD_ID + " FROM "
				+ DBManager.TABLE_HOUSEHOLD_ITEM + " WHERE "
				+ DBManager.HOUSEHOLD_ITEMNAME + " ='" + itemName + "'";

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				itemid = Integer.parseInt(cursor.getString(0));

			}
		}

		return itemid;
		// TODO Auto-generated method stub

	}

	public int getIronidByItemName(String itemName) {

		int itemid = 0;
		String query = "SELECT " + DBManager.IRON_ID + " FROM "
				+ DBManager.TABLE_IRON_ITEMS + " WHERE "
				+ DBManager.IRON_ITEMNAME + " ='" + itemName + "'";

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				itemid = Integer.parseInt(cursor.getString(0));

			}
		}

		return itemid;
		// TODO Auto-generated method stub

	}
	
	public void deleteAllIronItem()
	{
		db.delete(DBManager.TABLE_IRON_ITEMS,null, null);
	}

	public int getDryCleanidByItemName(String itemName) {

		int itemid = 0;
		String query = "SELECT " + DBManager.DRYCLEAN_ID + " FROM "
				+ DBManager.TABLE_DRYCLEAN_ITEMS + " WHERE "
				+ DBManager.DRYCLEAN_ITEMNAME + " ='" + itemName + "'";

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				itemid = Integer.parseInt(cursor.getString(0));

			}
		}

		return itemid;
		// TODO Auto-generated method stub

	}

	public int getInventoryidByItemName(String itemName) {

		int itemid = 0;
		String query = "SELECT " + DBManager.INVENTORY_ID + " FROM "
				+ DBManager.TABLE_INVENTORY_ITEMS + " WHERE "
				+ DBManager.INVENTORY_ITEMNAME + " ='" + itemName + "'";

		final Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.getCount() > 0) {

			if (cursor.moveToFirst()) {

				itemid = Integer.parseInt(cursor.getString(0));

			}
		}

		return itemid;
		// TODO Auto-generated method stub

	}

	public void updateHouseholdCountByHouseholdItemId(int householdid,
			int totalCount) {
		// TODO Auto-generated method stub

		String query = "UPDATE " + DBManager.TABLE_HOUSEHOLD_ITEM + " SET "
				+ DBManager.ITEM_COUNT + " = '" + totalCount + "' WHERE "
				+ DBManager.HOUSEHOLD_ID + "= " + householdid + "";
		try {
			db.execSQL(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateIronCountByHouseholdItemId(int ironid, int totalCount) {
		// TODO Auto-generated method stub

		String query = "UPDATE " + DBManager.TABLE_IRON_ITEMS + " SET "
				+ DBManager.ITEM_COUNT + " = '" + totalCount + "' WHERE "
				+ DBManager.IRON_ID + "= " + ironid + "";
		try {
			db.execSQL(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateDrycleanCountByHouseholdItemId(int drycleanid,
			int totalCount) {
		// TODO Auto-generated method stub

		String query = "UPDATE " + DBManager.TABLE_DRYCLEAN_ITEMS + " SET "
				+ DBManager.ITEM_COUNT + " = '" + totalCount + "' WHERE "
				+ DBManager.DRYCLEAN_ID + "= " + drycleanid + "";
		try {
			db.execSQL(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateInventoryCountByHouseholdItemId(int inventoryid,
			int totalCount) {
		// TODO Auto-generated method stub

		String query = "UPDATE " + DBManager.TABLE_INVENTORY_ITEMS + " SET "
				+ DBManager.ITEM_COUNT + " = '" + totalCount + "' WHERE "
				+ DBManager.INVENTORY_ID + "= " + inventoryid + "";
		try {
			db.execSQL(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteDataFromAllTables() {

		String queryHousehold = "DELETE FROM " + DBManager.TABLE_HOUSEHOLD_ITEM;
		String queryIron = "DELETE FROM " + DBManager.TABLE_IRON_ITEMS;
		String queryDryClean = "DELETE FROM " + DBManager.TABLE_DRYCLEAN_ITEMS;
		String queryInventory = "DELETE FROM "
				+ DBManager.TABLE_INVENTORY_ITEMS;

		db.execSQL(queryHousehold);
		db.execSQL(queryIron);
		db.execSQL(queryDryClean);
		db.execSQL(queryInventory);

	}

	public void updateHouseholdCount() {

		String query = "UPDATE " + DBManager.TABLE_HOUSEHOLD_ITEM + " SET "
				+ DBManager.ITEM_COUNT + "='0'";
		db.execSQL(query);

	}

	public void updateIronCount() {

		String query = "UPDATE " + DBManager.TABLE_IRON_ITEMS + " SET "
				+ DBManager.ITEM_COUNT + "='0'";
		db.execSQL(query);
	}

	public void updateDryCleanCount() {

		String query = "UPDATE " + DBManager.TABLE_DRYCLEAN_ITEMS + " SET "
				+ DBManager.ITEM_COUNT + "='0'";
		db.execSQL(query);
	}

	public void updateInventoryCount() {

		String query = "UPDATE " + DBManager.TABLE_INVENTORY_ITEMS + " SET "
				+ DBManager.ITEM_COUNT + "='0'";
		db.execSQL(query);
	}

	public void deleteAllInventory() {
		// TODO Auto-generated method stub
		db.delete(DBManager.TABLE_INVENTORY_ITEMS,null,null);
	}

}
