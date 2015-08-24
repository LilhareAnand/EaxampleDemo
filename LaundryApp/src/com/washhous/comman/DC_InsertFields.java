package com.washhous.comman;

public class DC_InsertFields {

	String itemName;
	String price;
	String count;
	String OrderId;

	public DC_InsertFields() {
		super();
	}

	public DC_InsertFields(String itemName, String price, String count,
			String orderId) {
		super();
		this.itemName = itemName;
		this.price = price;
		this.count = count;
		OrderId = orderId;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
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

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

}
