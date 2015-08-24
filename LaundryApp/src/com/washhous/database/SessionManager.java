package com.washhous.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.washhous.laundryapp.SignInOption;
import com.washhous.menudrawer.MainActivity;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREF_NAME = "AndroidHivePref";
	public static boolean isload ;
	
	// All Shared Preferences Keys
	public static final String IS_LOGIN = "IsLoggedIn";
	
	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "email_id";
	
	// Email address (make variable public to access from outside)
	public static final String KEY_PASS = "pass";
/*	// Email address (make variable public to access from outside)
	public static final String KEY_TYPE = "LOGINTYPE";
	public static final String KEY_id = "uid";
	public static final String KEYn_UNAME = "Uname";*/
	
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String name, String pass,String loginType,String uid,String NAME){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		// Storing name in pref
		editor.putString(KEY_NAME, name);
		//editor.putString(KEYn_UNAME, NAME);
		
		// Storing email in pref
		editor.putString(KEY_PASS, pass);
		/*editor.putString(KEY_TYPE, loginType);
		editor.putString(KEY_id, uid);*/
		
		// commit changes
		editor.commit();
	}	
	
	public void createlogin(String email_id,String password)
	{
		
		editor.putString(KEY_NAME, email_id);
		//editor.putString(KEYn_UNAME, NAME);
		
		// Storing email in pref
		editor.putString(KEY_PASS, password);
		/*editor.putString(KEY_TYPE, loginType);
		editor.putString(KEY_id, uid);*/
		
		// commit changes
		editor.commit();
		
	}
	public void address(String address_key,String address_value)
	{
		
		editor.putString(address_key, address_value);
		//editor.putString(KEYn_UNAME, NAME);
		
		// Storing email in pref
		//editor.putString(KEY_PASS, password);
		/*editor.putString(KEY_TYPE, loginType);
		editor.putString(KEY_id, uid);*/
		
		// commit changes
		editor.commit();
		
	}
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public void checkLogin(){
		// Check login status
		if(!this.isLoggedIn()){
			// user is not logged in redirect him to Login Activity
		/*	Intent i = new Intent(_context, Login.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// Staring Login Activity
			_context.startActivity(i);*/
		}
		
	
		
		
	}
	
	public String getemailid()
	{
		
		
		return pref.getString(KEY_NAME, "");
	}
	public String getaddress(String key_name)
	{
		
		
		return pref.getString(key_name, "");
	}
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		
		// user email id
		user.put(KEY_PASS, pref.getString(KEY_PASS, null));
		//user.put(KEY_TYPE, pref.getString(KEY_TYPE, null));
		
		// return user
		return user;
	}
	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
	/*	// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, SignInOption.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Staring Login Activity
		_context.startActivity(i);*/
	}
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}

	public String weightvalue(String key_name) {
		// TODO Auto-generated method stub
		return pref.getString(key_name, "0");
	}
	
}
