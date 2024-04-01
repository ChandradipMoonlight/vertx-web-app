package com.moonlight.models.sql;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "customer")
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseModel{

	private String firstName;
	private String lastName;
	private Integer age;

	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	private Address address;
}
