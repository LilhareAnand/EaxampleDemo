package com.washhous.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

	private static final String DB_NAME = "LaundryApp";
	private static final int DATABASE_VERSION = 1;

	// Database Table Name
	public static final String TABLE_HOUSEHOLD_ITEM = "HouseholdItem";
	public static final String TABLE_IRON_ITEMS = "IronItems";
	public static final String TABLE_DRYCLEAN_ITEMS = "DryClean";
	public static final String TABLE_INVENTORY_ITEMS = "InventoryItem";

	// Common column
	public static final String ITEM_COUNT = "Item_Count";

	// Household Item column
	public static final String HOUSEHOLD_ID = "id";
	public static final String HOUSEHOLD_ITEMNAME = "Household_Item_Name";
	public static final String HOUSEHOLD_PRICE = "Household_Price";

	// Iron Item column
	public static final String IRON_ID = "id";
	public static final String IRON_ITEMNAME = "Iron_Item_Name";
	public static final String IRON_PRICE = "Iron_Price";

	// Dry Item column
	public static final String DRYCLEAN_ID = "id";
	public static final String DRYCLEAN_ITEMNAME = "DryClean_Item_Name";
	public static final String DRYCLEAN_PRICE = "DryClean_Price";

	// Inventory Item column
	public static final String INVENTORY_ID = "id";
	public static final String INVENTORY_ITEMNAME = "Inventory_Item_Name";
	public static final String INVENTORY_PRICE = "Inventory_Price";

	private Context context;

	public DBManager(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		createTables(db);
	}

	private void createTables(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String queryHousehold = "CREATE TABLE " + TABLE_HOUSEHOLD_ITEM + " ("
				+ HOUSEHOLD_ID
				+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
				+ HOUSEHOLD_ITEMNAME + " TEXT," + HOUSEHOLD_PRICE + " TEXT,"
				+ ITEM_COUNT + " TEXT) ";

		String queryIron = "CREATE TABLE " + TABLE_IRON_ITEMS + " (" + IRON_ID
				+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
				+ IRON_ITEMNAME + " TEXT," + IRON_PRICE + " TEXT," + ITEM_COUNT
				+ " TEXT) ";

		String queryDryClean = "CREATE TABLE " + TABLE_DRYCLEAN_ITEMS + " ("
				+ DRYCLEAN_ID
				+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
				+ DRYCLEAN_ITEMNAME + " TEXT," + DRYCLEAN_PRICE + " TEXT ,"
				+ ITEM_COUNT + " TEXT) ";

		String queryInventory = "CREATE TABLE " + TABLE_INVENTORY_ITEMS + " ("
				+ INVENTORY_ID
				+ " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
				+ INVENTORY_ITEMNAME + " TEXT," + INVENTORY_PRICE + " TEXT ,"
				+ ITEM_COUNT + " TEXT) ";

		db.execSQL(queryHousehold);
		db.execSQL(queryIron);
		db.execSQL(queryDryClean);
		db.execSQL(queryInventory);
		Log.i("tablecreate", " Table create susssfully");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSEHOLD_ITEM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_IRON_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRYCLEAN_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY_ITEMS);

		onCreate(db);
	}

}
