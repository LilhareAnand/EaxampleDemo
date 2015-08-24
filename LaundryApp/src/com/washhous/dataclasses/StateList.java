package com.washhous.dataclasses;

public class StateList {

	String countryId;
	String stateName;

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public StateList(String countryId, String stateName) {
		super();
		this.countryId = countryId;
		this.stateName = stateName;
	}

	public StateList() {
		super();
	}

}
