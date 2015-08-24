package com.washhous.menudrawer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.washhous.comman.RoundedImageView;
import com.washhous.laundryapp.R;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
        
		LinearLayout LinLayoutUserInfo=(LinearLayout)convertView.findViewById(R.id.linLayoutUserInfo);
		LinearLayout linLayoutMenus=(LinearLayout)convertView.findViewById(R.id.linLayoutMenus);
		RoundedImageView imgUser=(RoundedImageView)convertView.findViewById(R.id.imgUser);
		TextView txtUserName=(TextView)convertView.findViewById(R.id.txtUserName);
		ImageView icon=(ImageView)convertView.findViewById(R.id.icon);
		TextView counter=(TextView)convertView.findViewById(R.id.counter);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title); 
             
        if(position==0){
        	LinLayoutUserInfo.setVisibility(View.VISIBLE);
        	linLayoutMenus.setVisibility(View.GONE);
        	imgUser.setImageResource(R.drawable.test);
        	txtUserName.setText("Demo User"); 
        }else{
        	LinLayoutUserInfo.setVisibility(View.GONE);
        	linLayoutMenus.setVisibility(View.VISIBLE);
        	icon.setImageResource(navDrawerItems.get(position).getIcon());
        	txtTitle.setText(navDrawerItems.get(position).getTitle());
        	if(navDrawerItems.get(position).getCounterVisibility()){
        		counter.setBackgroundResource(R.drawable.red);
        		counter.setText(navDrawerItems.get(position).getCount());
        	}
        }
        
        
        // displaying count
         
        return convertView;
	}

}
