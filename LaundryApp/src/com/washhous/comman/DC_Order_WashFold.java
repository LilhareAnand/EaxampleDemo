package com.washhous.comman;

public class DC_Order_WashFold {

	String Eco_Detergent;
	String Scent_Free;
	String Weight_Est_Count;
	String item_Count;
	String Inventory_Count;
	String Pickup_Date;
	String Delivery_Date;
	String Paid;
	String OrderId;
	String ObjectId;
	String totalcalculation;
    public String getTotalcalculation() {
		return totalcalculation;
	}

	public void setTotalcalculation(String totalcalculation) {
		this.totalcalculation = totalcalculation;
	}

	public String getTotalvalu() {
		return totalvalu;
	}

	public void setTotalvalu(String totalvalu) {
		this.totalvalu = totalvalu;
	}

	String totalvalu;
	public DC_Order_WashFold() {

	}

	public DC_Order_WashFold(String eco_Detergent, String scent_Free,
			String weight_Est_Count, String household_Count,
			String inventory_Count, String pickup_Date, String delivery_Date,
			String paid, String orderId, String objectId) {
		super();
		Eco_Detergent = eco_Detergent;
		Scent_Free = scent_Free;
		Weight_Est_Count = weight_Est_Count;
		item_Count = household_Count;
		Inventory_Count = inventory_Count;
		Pickup_Date = pickup_Date;
		Delivery_Date = delivery_Date;
		Paid = paid;
		OrderId = orderId;
		ObjectId = objectId;
	}

	public String getEco_Detergent() {
		return Eco_Detergent;
	}

	public void setEco_Detergent(String eco_Detergent) {
		Eco_Detergent = eco_Detergent;
	}

	public String getScent_Free() {
		return Scent_Free;
	}

	public void setScent_Free(String scent_Free) {
		Scent_Free = scent_Free;
	}

	public String getWeight_Est_Count() {
		return Weight_Est_Count;
	}

	public void setWeight_Est_Count(String weight_Est_Count) {
		Weight_Est_Count = weight_Est_Count;
	}

	public String getItem_Count() {
		return item_Count;
	}

	public void setItem_Count(String household_Count) {
		item_Count = household_Count;
	}

	public String getInventory_Count() {
		return Inventory_Count;
	}

	public void setInventory_Count(String inventory_Count) {
		Inventory_Count = inventory_Count;
	}

	public String getPickup_Date() {
		return Pickup_Date;
	}

	public void setPickup_Date(String pickup_Date) {
		Pickup_Date = pickup_Date;
	}

	public String getDelivery_Date() {
		return Delivery_Date;
	}

	public void setDelivery_Date(String delivery_Date) {
		Delivery_Date = delivery_Date;
	}

	public String getPaid() {
		return Paid;
	}

	public void setPaid(String paid) {
		Paid = paid;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getObjectId() {
		return ObjectId;
	}

	public void setObjectId(String objectId) {
		ObjectId = objectId;
	}

}
