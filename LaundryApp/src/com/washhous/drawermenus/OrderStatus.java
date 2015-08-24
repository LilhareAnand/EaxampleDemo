package com.washhous.drawermenus;

import java.util.List;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.washhous.database.SessionManager;

import android.content.Context;

public class OrderStatus {
	Context ctx;
	public List<ParseObject> houseHoldCountObject;
	SessionManager sesstion;
	public List<ParseObject> ironCountObject;
	public List<ParseObject> dryCleanCountObject;
	public OrderStatus(Context ctx)
	{
		sesstion=new SessionManager(ctx);
		this.ctx=ctx;
	}

	
	public boolean IswashandfoldeOrder()
	{
		boolean status=false;
		try {
			ParseQuery<ParseObject> houseHoldItems = new ParseQuery<ParseObject>(
					"Order_WashFold");
			String email=sesstion.getemailid();
			houseHoldItems.whereEqualTo("user_id",email);
			houseHoldCountObject = houseHoldItems.find();
			int count=houseHoldCountObject.size();
		   if(count!=0)
		   {
			   status=true;
		   }
		} catch (com.parse.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return status;
	}
	public boolean IswashandIronOrder()
	{
		
		
		boolean status=false;
		try {
			ParseQuery<ParseObject> ironItems = new ParseQuery<ParseObject>(
					"Order_WashIron");
			String email=sesstion.getemailid();
			ironItems.whereEqualTo("user_id",email);
			ironCountObject = ironItems.find();
			int count=ironCountObject.size();
		   if(count!=0)
		   {
			   status=true;
		   }
		} catch (com.parse.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		return status;
	}
	public boolean IsdrycleaningOrder()
	{
		
		boolean status=false;
		try {
			ParseQuery<ParseObject> dryCleanItems = new ParseQuery<ParseObject>(
					"Order_DryClean");

			String email=sesstion.getemailid();
			dryCleanItems.whereEqualTo("user_id",email);
			dryCleanCountObject = dryCleanItems.find();
			int count=dryCleanCountObject.size();
		   if(count!=0)
		   {
			   status=true;
		   }
		} catch (com.parse.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		
		return status;
	}
	
}
