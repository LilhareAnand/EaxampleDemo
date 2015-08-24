package com.washhous.dataclasses;

public class HouseHoldItem {
	String itemName;
	String price;

	public HouseHoldItem(String itemName, String price) {
		super();
		this.itemName = itemName;
		this.price = price;
	}

	public HouseHoldItem() {
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
