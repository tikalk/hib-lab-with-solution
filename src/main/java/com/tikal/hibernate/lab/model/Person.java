package com.tikal.hibernate.lab.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@NamedQuery(name="findByMinAge", query="select p from Person p where p.age > :minAge order by p.name")
@DiscriminatorColumn(name="person_type", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("P")
public class Person {
	@Id
	@GeneratedValue
	private Integer id;

	@NaturalId(mutable=true)
	private String name;

	@NotNull
	private int age;
	
	private Address address;

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="person_id")
	private Set<Car> cars;

	Person() {
		this(null);
	}

	public Person(String name) {
		this.name = name;
		address = new Address();
	}

	public Person(String name, int age) {
		this(name);
		this.age=age;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Integer getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Integer id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Car> getCars() {
		return cars;
	}

	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}
	
	public void addCar(Car car){
		if(cars == null)
			cars = new HashSet<Car>();
		cars.add(car);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Person other = (Person) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
