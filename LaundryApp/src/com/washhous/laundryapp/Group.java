package com.washhous.laundryapp;

import java.util.ArrayList;

public class Group {

	String name;
	private ArrayList<Child> Items;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Child> getItems() {
		return Items;
	}

	public void setItems(ArrayList<Child> items) {
		Items = items;
	}

}
