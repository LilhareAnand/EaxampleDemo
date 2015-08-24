package com.washhous.dataclasses;

public class DryCleaningItems {
	String itemName;
	String price;
	String Count;

	public DryCleaningItems(String itemName, String price, String count) {
		super();
		this.itemName = itemName;
		this.price = price;
		Count = count;
	}

	public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = count;
	}

	public DryCleaningItems() {
		super();
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
