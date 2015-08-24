package com.washhous.dataclasses;

public class IornItems {

	String itemName;
	String price;

	public IornItems(String itemName, String price) {
		super();
		this.itemName = itemName;
		this.price = price;
	}

	public IornItems() {
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
