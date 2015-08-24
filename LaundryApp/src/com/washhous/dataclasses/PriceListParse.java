package com.washhous.dataclasses;

public class PriceListParse {

	String clothItem;
	String LaundrypriceRate;
	String DryCleaningpriceRate;

	public PriceListParse(String clothItem, String laundrypriceRate,
			String dryCleaningpriceRate) {
		super();
		this.clothItem = clothItem;
		LaundrypriceRate = laundrypriceRate;
		DryCleaningpriceRate = dryCleaningpriceRate;
	}

	public PriceListParse() {
		super();
	}

	public String getClothItem() {
		return clothItem;
	}

	public void setClothItem(String clothItem) {
		this.clothItem = clothItem;
	}

	public String getLaundrypriceRate() {
		return LaundrypriceRate;
	}

	public void setLaundrypriceRate(String laundrypriceRate) {
		LaundrypriceRate = laundrypriceRate;
	}

	public String getDryCleaningpriceRate() {
		return DryCleaningpriceRate;
	}

	public void setDryCleaningpriceRate(String dryCleaningpriceRate) {
		DryCleaningpriceRate = dryCleaningpriceRate;
	}

}
