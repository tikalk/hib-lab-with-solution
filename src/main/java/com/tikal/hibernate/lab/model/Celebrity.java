package com.tikal.hibernate.lab.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("C")
public class Celebrity extends Person {
	private String famousName;

	Celebrity(){
	}

	public Celebrity(String name) {
		super(name);
	}
	

	public String getFamousName() {
		return famousName;
	}

	public void setFamousName(String famousName) {
		this.famousName = famousName;
	}
}
