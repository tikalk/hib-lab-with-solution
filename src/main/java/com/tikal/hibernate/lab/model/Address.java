package com.tikal.hibernate.lab.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {
	@Column
	@NotNull
	private int houseNo;
	private String street;
	private String city;

	Address() {
	}

	public Address(int houseNo, String street, String city) {
		super();
		this.houseNo = houseNo;
		this.street = street;
		this.city = city;
	}

	public int getHouseNo() {
		return houseNo;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

}
