package com.washhous.dataclasses;

public class InventoryCheckList {
	String itemName;
	String price;

	public InventoryCheckList(String itemName, String price) {
		super();
		this.itemName = itemName;
		this.price = price;
	}

	public InventoryCheckList() {
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
