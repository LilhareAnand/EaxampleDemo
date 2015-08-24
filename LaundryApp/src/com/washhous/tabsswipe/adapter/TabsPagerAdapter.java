package com.washhous.tabsswipe.adapter;

import com.washhous.tabsswipe.DryCleanFragment;
import com.washhous.tabsswipe.WashFoldFragment;
import com.washhous.tabsswipe.WashIronFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new WashFoldFragment();
		case 1:
			// Games fragment activity
			return new WashIronFragment();
		case 2:
			// Movies fragment activity
			return new DryCleanFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
