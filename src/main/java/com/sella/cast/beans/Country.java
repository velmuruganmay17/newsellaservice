package com.sella.cast.beans;

public class Country{
	 
	int id;
	String countryName; 
 
	public Country(int i, String countryName) {
		super();
		this.id = i;
		this.countryName = countryName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	@Override
	public String toString() {
		return "Country [id=" + id + ", countryName=" + countryName + "]";
	} 
	
	
 
}